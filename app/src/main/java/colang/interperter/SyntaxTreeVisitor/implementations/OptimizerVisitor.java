package colang.interperter.SyntaxTreeVisitor.implementations;

import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.*;
import colang.interperter.SyntaxTreeVisitor.SyntaxTreeVisitor;

public class OptimizerVisitor implements SyntaxTreeVisitor {

    @Override
    public void visit(BlockNode blockNode) {
        for (StatementNode statement : blockNode.children) {
            statement.accept(this);
        }
    }

    @Override
    public ExpressionNode visit(ExpressionNode expressionNode) {
        if (expressionNode instanceof BinOppNode) {
            BinOppNode binOppNode = (BinOppNode) expressionNode;
            if (binOppNode.left instanceof ValueNode && binOppNode.right instanceof ValueNode) {
                ValueNode valueNode = new ValueNode();
                valueNode.value = expressionNode.calculate();
                return valueNode;
            }
        } else if(expressionNode instanceof CompareNode) {
            CompareNode cmpNode = (CompareNode)expressionNode;
            cmpNode.left = visit(cmpNode.left);
            cmpNode.right = visit(cmpNode.right);
            return cmpNode;
        }
        return expressionNode;
    }

    @Override
    public void visit(AssignNode assignNode) {
        assignNode.value_expression.accept(this);
    }

    @Override
    public void visit(PrintNode printNode) {
        printNode.print_expression.accept(this);
    }

    @Override
    public void visit(IfNode ifNode) {
        visit((ExpressionNode) ifNode.condition_expression);
        ifNode.if_statements.accept(this);
        if (ifNode.else_statements != null) {
            ifNode.else_statements.accept(this);
        }
    }

    @Override
    public void visit(WhileNode whileNode) {
        whileNode.if_statements.accept(this);
    }

    @Override
    public void visit(FunNode funNode) {
        funNode.body.accept(this);
    }

    @Override
    public void visit(FunCallNode funCallNode) {
    }
}
