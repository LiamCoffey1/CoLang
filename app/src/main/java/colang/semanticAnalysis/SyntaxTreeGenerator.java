package colang.semanticAnalysis;

import java.util.regex.Pattern;

import colang.lexicalAnalysis.TokenNode;
import colang.semanticAnalysis.nodes.Nodes.AssignNode;
import colang.semanticAnalysis.nodes.Nodes.BinOppNode;
import colang.semanticAnalysis.nodes.Nodes.BlockNode;
import colang.semanticAnalysis.nodes.Nodes.ExpressionNode;
import colang.semanticAnalysis.nodes.Nodes.IdNode;
import colang.semanticAnalysis.nodes.Nodes.IfNode;
import colang.semanticAnalysis.nodes.Nodes.PrintNode;
import colang.semanticAnalysis.nodes.Nodes.ValueNode;

public class SyntaxTreeGenerator {

    TokenNode current_node;
    String regex = "^[a-zA-Z_$][a-zA-Z_$0-9]*$";
    Pattern variableNamePattern = Pattern.compile(regex);

    public SyntaxTreeGenerator(TokenNode current_node) {
        this.current_node = current_node;
    }

    TokenNode getNextToken() {
        if (current_node == null)
            return null;
        TokenNode temp = current_node;
        current_node = current_node.next;
        return temp;
    }

    IfNode createIfBranch() {
        IfNode node = new IfNode();
        getNextToken();
        getNextToken();
        node.condition_expression = createExpression();
        getNextToken();
        getNextToken();
        node.if_statements = createBlock();
        return node;
    }

    public BlockNode createBlock() {
        BlockNode node = new BlockNode();
        getNextToken();
        while (current_node != null) {
            if(current_node.token.equals("}")) {
                return node;
            }
            if (current_node.token.equals("print")) {
                PrintNode print_node = new PrintNode();
                getNextToken();
                print_node.print_expression = createExpression();
                node.children.add(print_node);
            }
            else if (current_node.token.equals("if")) {
                node.children.add(createIfBranch());
            }
            else if (variableNamePattern.matcher(current_node.token).matches()) {
                AssignNode n = createAssignment();
                node.children.add(n);
            }  
            getNextToken();
        }
        return node;
    }

    AssignNode createAssignment() {
        AssignNode node = new AssignNode();
        node.identifier = current_node.token;
        getNextToken();
        getNextToken();
        node.value_expression = createExpression();
        return node;
    }

    ExpressionNode createExpression() {
        if(current_node.next != null && current_node.next.token.equals("+")) {
            BinOppNode binOppNode = new BinOppNode();
            binOppNode.left = createTerm();
            getNextToken();
            binOppNode.operator = "+";
            getNextToken();
            binOppNode.right = createExpression();
            return binOppNode;
        } else {
            return createTerm();
        }
    }

    ExpressionNode createTerm() {
        if (variableNamePattern.matcher(current_node.token).matches()) {
            IdNode node = new IdNode();
            node.id = current_node.token;
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