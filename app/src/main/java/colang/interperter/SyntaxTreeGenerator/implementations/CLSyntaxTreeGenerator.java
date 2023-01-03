package colang.interperter.SyntaxTreeGenerator.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import colang.interperter.SyntaxTree.SyntaxTree;
import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.*;
import colang.interperter.SyntaxTreeGenerator.SyntaxTreeGenerator;
import colang.interperter.Tokenizer.TokenNode;
import colang.interperter.Tokenizer.TokenType;
import colang.interperter.Tokenizer.Tokenizer;
import colang.interperter.Tokenizer.implementations.CLTokenizer;
import colang.interperter.Type.CLType;

public class CLSyntaxTreeGenerator extends SyntaxTreeGenerator {

    private HashMap<TokenType, Supplier<StatementNode>> statement_nodes_map = new HashMap<TokenType, Supplier<StatementNode>>();

    public Tokenizer getTokenizer() {
        return new CLTokenizer();
    }

    @Override
    protected SyntaxTree createNodes() {
        SyntaxTree tree = new SyntaxTree();
        initializeCreationMap();
        tree.root = createBlock();
        return tree;
    }

    private void initializeCreationMap() {
        statement_nodes_map.put(TokenType.CLASS, () -> createClass());
        statement_nodes_map.put(TokenType.PRINT, () -> createPrintNode());
        statement_nodes_map.put(TokenType.IF, () -> createIfBranch());
        statement_nodes_map.put(TokenType.WHILE, () -> createWhileBranch());
        statement_nodes_map.put(TokenType.FUN, () -> createFunBranch());
        statement_nodes_map.put(TokenType.RETURN, () -> createReturnStatement());
        statement_nodes_map.put(TokenType.VAR, () -> createVariableDeclaration());
        statement_nodes_map.put(TokenType.FOREACH, () -> createForEachBranch());
        statement_nodes_map.put(TokenType.IDENTIFIER, () -> createAssignment());
    }

    private BlockNode createBlock() {
        BlockNode node = new BlockNode();
        getNextToken();
        while (current_node != null) {
            if (current_node.type == TokenType.RIGHT_CURLY) {
                return node;
            } else {
                Supplier<StatementNode> statementNode = statement_nodes_map.get(current_node.type);
                if (statementNode == null) {
                    node.children.add(createExpressionStatement());
                } else {
                    node.children.add(statementNode.get());
                }
            }
            getNextToken();
        }
        return node;
    }

    private ClassDefinitionNode createClass() {
        ClassDefinitionNode node = new ClassDefinitionNode();
        getNextToken();
        node.className = current_node.token;
        getNextToken();
        getNextToken();
        while (current_node.type != TokenType.RIGHT_CURLY) {
            if (current_node.type == TokenType.FUN) {
                node.functions.add(createFunBranch(node.className));
            }
            if (current_node.type == TokenType.VAR) {
                node.variables.add((VariableDeclarationNode) createVariableDeclaration());
            }
            if (current_node.type == TokenType.CONSTRUCTOR) {
                node.constructor = createConstructor();
            }
            getNextToken();
        }
        return node;
    }

    private IfNode createIfBranch() {
        IfNode node = new IfNode();
        getNextToken();
        expectAndAdvance(TokenType.LEFT_PAREN);
        node.condition_expression = createExpression();
        getNextToken();
        expectAndAdvance(TokenType.RIGHT_PAREN);
        expect(TokenType.LEFT_CURLY);
        node.if_statements = createBlock();
        if (current_node.next != null && equalsTokenValue(current_node.next, TokenType.ELSE)) {
            getNextToken();
            expect(TokenType.RIGHT_CURLY);
            node.else_statements = createBlock();
        }
        return node;
    }

    private ForeachNode createForEachBranch() {
        ForeachNode node = new ForeachNode();
        getNextToken();
        expectAndAdvance(TokenType.IN);
        IdNode id_node = new IdNode();
        id_node.id = current_node.token;
        node.identifier = id_node;
        getNextToken();
        getNextToken();
        node.value_name = current_node.token;
        getNextToken();
        getNextToken();
        node.statements = createBlock();
        return node;
    }

    private WhileNode createWhileBranch() {
        WhileNode node = new WhileNode();
        getNextToken();
        expect(TokenType.LEFT_PAREN);
        node.condition_expression = createExpression();
        expect(TokenType.RIGHT_PAREN);
        getNextToken();
        node.if_statements = createBlock();
        return node;
    }

    private FunNode createFunBranch() {
        return createFunBranch("main");
    }

    private FunNode createConstructor() {
        FunNode node = new FunNode();
        getNextToken();
        getNextToken();
        while (!equalsTokenValue(current_node, TokenType.RIGHT_PAREN)) {
            node.parameter_ids.add(current_node.token);
            if (nextTokenEquals(TokenType.COMMA)) {
                getNextToken();
            }
            getNextToken();
        }
        getNextToken();
        node.body = createBlock();
        return node;
    }

    private FunNode createFunBranch(String owner) {
        FunNode node = new FunNode();
        getNextToken();
        node.id = current_node.token;
        getNextToken();
        getNextToken();
        while (!equalsTokenValue(current_node, TokenType.RIGHT_PAREN)) {
            node.parameter_ids.add(current_node.token);
            if (nextTokenEquals(TokenType.COMMA)) {
                getNextToken();
            }
            getNextToken();
        }
        getNextToken();
        node.body = createBlock();
        return node;
    }

    private FunCallNode createFunCallBranch() {
        FunCallNode node = new FunCallNode();
        node.id = current_node.token;
        getNextToken();
        getNextToken();
        while (!equalsTokenValue(current_node, TokenType.RIGHT_PAREN)) {
            node.values.add(createExpression());
            if (nextTokenEquals(TokenType.COMMA)) {
                getNextToken();
            }
            getNextToken();
        }
        return node;
    }

    private PrintNode createPrintNode() {
        PrintNode print_node = new PrintNode();
        getNextToken();
        print_node.print_expression = createExpression();
        return print_node;
    }

    private ReturnStatement createReturnStatement() {
        ReturnStatement print_node = new ReturnStatement();
        getNextToken();
        print_node.return_value = createExpression();
        return print_node;
    }

    boolean nextTokenEquals(TokenType token) {
        return current_node.next != null && current_node.next.type.name() == token.name();
    }

    private VariableDeclarationNode createVariableDeclaration() {
        VariableDeclarationNode expr = new VariableDeclarationNode();
        getNextToken();
        expr.identifier = current_node.token;
        getNextToken();
        getNextToken();
        expr.value_expression = createExpression();
        return expr;
    }

    private StatementNode createAssignment() {
        AssignNode expr = new AssignNode();
        IdNode idNode = new IdNode();
        idNode.id = current_node.token;
        expr.identifier = createAccessor(idNode, true);
        if (!nextTokenEquals(TokenType.ASSIGNMENT_OPERATOR)) {
            return createExpressionStatement();
        }
        getNextToken();
        expr.operator = current_node.token;
        getNextToken();
        expr.value_expression = createExpression();
        return expr;
    }

    private StatementNode createExpressionStatement() {
        ExpressionStatement expr = new ExpressionStatement();
        expr.expression = createExpression();
        return expr;
    }

    private ExpressionNode createArray() {
        getNextToken();
        ValueNode node = new ValueNode();
        List<ExpressionNode> objects = new ArrayList<ExpressionNode>();
        while (current_node.type != TokenType.RIGHT_BOX) {
            objects.add(createExpression());
            if (current_node.next != null && current_node.next.type != TokenType.RIGHT_BOX) {
                getNextToken();
                expect(TokenType.COMMA);
            }
            getNextToken();
        }
        node.type = CLType.ARRAY;
        node.value = objects;
        return node;
    }

    private ExpressionNode createNewInstance() {
        getNextToken();
        InstanceCreateNode instanceCreateNode = new InstanceCreateNode();
        instanceCreateNode.className = current_node.token;
        getNextToken();
        getNextToken();
        while (!equalsTokenValue(current_node, TokenType.RIGHT_PAREN)) {
            instanceCreateNode.parameters.add(createExpression());
            if (nextTokenEquals(TokenType.COMMA)) {
                getNextToken();
            }
            getNextToken();
        }
        getNextToken();
        if (equalsTokenValue(current_node, TokenType.LEFT_CURLY)) {
            getNextToken();
            while (!equalsTokenValue(current_node, TokenType.RIGHT_CURLY)) {
                AssignNode nAssignNode = new AssignNode();
                AccessNode accessNode = new AccessNode();
                IdNode idNode = new IdNode();
                idNode.id = current_node.token;
                accessNode.initial = idNode;
                nAssignNode.identifier = accessNode;
                getNextToken();
                nAssignNode.operator = "=";
                getNextToken();
                nAssignNode.value_expression = createExpression();
                instanceCreateNode.assignments.add(nAssignNode);
                getNextToken();
            }
        }
        return instanceCreateNode;
    }

    private ExpressionNode createExpression() {
        if (current_node.type == TokenType.LEFT_BOX) {
            return createArray();
        }
        if (current_node.type == TokenType.NEW) {
            return createNewInstance();
        }
        ExpressionNode expr = createEquality();
        while (nextTokenEquals(TokenType.BOOLEAN_OPERATOR)) {
            BinOppNode binOppNode = new BinOppNode();
            binOppNode.left = expr;
            getNextToken();
            binOppNode.operator = current_node.token;
            getNextToken();
            binOppNode.right = createEquality();
            expr = binOppNode;
        }
        return expr;
    }

    private ExpressionNode createEquality() {
        ExpressionNode expr = createTerm();
        while (nextTokenEquals(TokenType.EQUALITY_OPERATOR)) {
            BinOppNode binOppNode = new BinOppNode();
            binOppNode.left = expr;
            getNextToken();
            binOppNode.operator = current_node.token;
            getNextToken();
            binOppNode.right = createTerm();
            expr = binOppNode;
        }
        return expr;
    }

    private ExpressionNode createTerm() {
        ExpressionNode expr = createFactor();
        while (nextTokenEquals(TokenType.FACTOR_OPERATOR)) {
            BinOppNode binOppNode = new BinOppNode();
            binOppNode.left = expr;
            getNextToken();
            binOppNode.operator = current_node.token;
            getNextToken();
            binOppNode.right = createFactor();
            expr = binOppNode;
        }
        return expr;
    }

    private ExpressionNode createFactor() {
        ExpressionNode expr = (ExpressionNode) createSubExpression();
        while (nextTokenEquals(TokenType.TERM_OPERATOR)) {
            BinOppNode binOppNode = new BinOppNode();
            binOppNode.left = expr;
            getNextToken();
            binOppNode.operator = current_node.token;
            getNextToken();
            binOppNode.right = createSubExpression();
            expr = binOppNode;
        }
        return expr;
    }

    private AccessNode createAccessor(IdNode initial_id, boolean instance_only) {
        return createAccessor(initial_id);
    }

    private AccessNode createAccessor(IdNode initial_id) {
        AccessNode accessNode = new AccessNode();
        accessNode.initial = initial_id;
        accessNode.next = null;
        AccessNode temp = accessNode;
        while (nextTokenEquals(TokenType.DOT)) {
            getNextToken();
            getNextToken();
            if (nextTokenEquals(TokenType.LEFT_PAREN)) {
                FunCallNode fNode = new FunCallNode();
                fNode.id = current_node.token;
                getNextToken();
                getNextToken();
                while (current_node.type != TokenType.RIGHT_PAREN) {
                    fNode.values.add(createExpression());
                    getNextToken();
                }
                FunAccessNode newnode = new FunAccessNode();
                newnode.functionCall = fNode;
                temp.next = newnode;
                temp = temp.next;
            } else {
                PropertyAccessNode newnode = new PropertyAccessNode();
                newnode.property = current_node.token;
                temp.next = newnode;
                temp = temp.next;
            }
        }
        return accessNode;
    }

    private ExpressionNode createSubExpression() {
        ValueNode node = new ValueNode();
        switch (current_node.type) {
            case BOOLEAN:
                node.type = CLType.BOOLEAN;
                node.value = Boolean.parseBoolean(current_node.token);
                return node;
            case STRING:
                node.type = CLType.STRING;
                node.value = current_node.token;
                return node;
            case NUMBER:
                node.type = CLType.NUMBER;
                node.value = Integer.parseInt(current_node.token);
                return node;
            case IDENTIFIER:
                if (nextTokenEquals(TokenType.LEFT_PAREN)) {
                    return createFunCallBranch();
                } else {
                    IdNode id_node = new IdNode();
                    id_node.id = current_node.token;
                    if (nextTokenEquals(TokenType.DOT)) {
                        return createAccessor(id_node);
                    }
                    return id_node;
                }
            default:
                break;
        }
        if (equalsTokenValue(current_node, TokenType.LEFT_PAREN)) {
            getNextToken();
            ExpressionNode expr_node = createExpression();
            getNextToken();
            return expr_node;
        }
        return null;
    }

    private boolean equalsTokenValue(TokenNode value, TokenType tokenType) {
        return value != null && value.type == tokenType;
    }

}
