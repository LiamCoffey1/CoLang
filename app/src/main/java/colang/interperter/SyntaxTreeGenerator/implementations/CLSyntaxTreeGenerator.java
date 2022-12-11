package colang.interperter.SyntaxTreeGenerator.implementations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import colang.interperter.SyntaxTree.SyntaxTree;
import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.*;
import colang.interperter.SyntaxTreeGenerator.SyntaxTreeGenerator;
import colang.interperter.Tokenizer.Tokenizer;
import colang.interperter.Tokenizer.implementations.CLTokenizer;

public class CLSyntaxTreeGenerator extends SyntaxTreeGenerator {

    private static final String FUN = "fun";
    private static final String WHILE = "while";
    private static final String IF = "if";
    private static final String PRINT = "print";
    private static final String LEFT_PAREN = ")";
    private static final String ELSE = "else";

    String variableNamePattern = "^[a-zA-Z_$][a-zA-Z_$0-9]*$";
    String eqPattern = "^[==|>=|<=|>|<]*$";
    String topOpsPattern = "^[*|\\/|&&]*$";
    String btmOpsPattern = "^[+|-|\\|\\|]*$";

    boolean matchesRegex(String regex, String input) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public Tokenizer getTokenizer() {
        return new CLTokenizer();
    }

    @Override
    protected SyntaxTree createNodes() {
        SyntaxTree tree = new SyntaxTree();
        tree.root = createBlock();
        return tree;
    }

    IfNode createIfBranch() {
        IfNode node = new IfNode();
        getNextToken();
        expectAndAdvance("(");
        node.condition_expression = createExpression();
        getNextToken();
        expectAndAdvance(")");
        expect("{");
        node.if_statements = createBlock();
        if (current_node.next != null && current_node.next.token.equals(ELSE)) {
            getNextToken();
            getNextToken();
            expect("{");
            node.else_statements = createBlock();
        }
        return node;
    }

    WhileNode createWhileBranch() {
        WhileNode node = new WhileNode();
        expect("(");
        node.condition_expression = createExpression();
        expect(")");
        node.if_statements = createBlock();
        return node;
    }

    FunNode createFunBranch() {
        FunNode node = new FunNode();
        getNextToken();
        node.id = current_node.token;
        getNextToken();
        getNextToken();
        while (!current_node.token.equals(LEFT_PAREN)) {
            node.parameter_ids.add(current_node.token);
            getNextToken();
        }  
        getNextToken();
        node.body = createBlock();
        return node;
    }

    FunCallNode createFunCallBranch() {
        FunCallNode node = new FunCallNode();
        node.id = current_node.token;
        getNextToken();
        getNextToken();
        while(!current_node.token.equals(LEFT_PAREN)) {
            node.values.add(createExpression());
            getNextToken();
        }  
        return node;
    }

    public BlockNode createBlock() {
        BlockNode node = new BlockNode();
        getNextToken();
        while (current_node != null) {
            if(current_node.token.equals("}")) {
                return node;
            }
            if (current_node.token.equals(PRINT)) {
                PrintNode print_node = new PrintNode();
                getNextToken();
                print_node.print_expression = createExpression();
                node.children.add(print_node);
            }
            else if (current_node.token.equals(IF)) {
                node.children.add(createIfBranch());
            }
            else if (current_node.token.equals(WHILE)) {
                node.children.add(createWhileBranch());
            }
            else if (current_node.token.equals(FUN)) {
                node.children.add(createFunBranch());
            }
            else if (matchesRegex(variableNamePattern, current_node.token) && current_node.next.token.equals("(")) {
                FunCallNode n = createFunCallBranch();
                node.children.add(n);
            }  
            else if (matchesRegex(variableNamePattern, current_node.token)) {
                AssignNode n = createAssignment();
                node.children.add(n);
            }  
            getNextToken();
        }
        return node;
    }

    AssignNode createAssignment() {
        AssignNode expr = new AssignNode();
        expr.identifier = current_node.token;
        getNextToken();
        expr.operator = current_node.token;
        getNextToken();
        expr.value_expression = createExpression();
        return expr;
    }

    ExpressionNode createExpression() {
        ExpressionNode expr = createTerm();
        while (current_node.next != null  && matchesRegex(eqPattern, current_node.next.token)) {
            CompareNode binOppNode = new CompareNode();
            binOppNode.left = expr;
            getNextToken();
            binOppNode.operator = current_node.token;
            getNextToken();
            binOppNode.right = createTerm();
            return binOppNode;
        }
        return expr;
    }

    ExpressionNode createTerm() {
        ExpressionNode expr = createFactor();
        while (current_node.next != null  && matchesRegex(topOpsPattern, current_node.next.token)) {
            BinOppNode binOppNode = new BinOppNode();
            binOppNode.left = expr;
            getNextToken();
            binOppNode.operator = current_node.token;
            getNextToken();
            binOppNode.right = createFactor();
            return binOppNode;
        }
        return expr;
    }

    ExpressionNode createFactor() {
        ExpressionNode expr = (ExpressionNode)createSubExpression();
        while (current_node.next != null  && matchesRegex(btmOpsPattern, current_node.next.token)) {
            BinOppNode binOppNode = new BinOppNode();
            binOppNode.left = expr;
            getNextToken();
            binOppNode.operator = current_node.token;
            getNextToken();
            binOppNode.right = createExpression();
            return binOppNode;
        }
        return expr;
    }

    private ExpressionNode createSubExpression() {
        if(current_node.token.equals("true") || current_node.token.equals("false")) {
            ValueNode node = new ValueNode();
            node.value = Boolean.parseBoolean(current_node.token);
            return node;
        }
        if (matchesRegex(variableNamePattern, current_node.token)) {
            IdNode node = new IdNode();
            node.id = current_node.token;
            return node;
        } else if(current_node.token.equals("(")) {
            getNextToken();
            ExpressionNode node = createExpression();
            getNextToken();
            return node;
        } else {
            ValueNode node = new ValueNode();
            node.value = getObjectFromString(current_node.token);
            return node;
        }
    }

    Object getObjectFromString(String value) {
        try {
            int val =  Integer.parseInt(value);
            return val;
        } catch (NumberFormatException nfe) {
            return value;
        }
    }
    
}
