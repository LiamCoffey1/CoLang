package colang.interperter.SyntaxTreeVisitor.implementations;

import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.*;
import colang.interperter.SyntaxTreeVisitor.SyntaxTreeVisitor;

public class OptimizerVisitor implements SyntaxTreeVisitor {

    @Override
    public void visit(BlockNode blockNode) {
        for(StatementNode statement : blockNode.children) {
            statement.accept(this);
        }
    }

    @Override
    public ExpressionNode visit(ExpressionNode expressionNode) {
        if(expressionNode instanceof BinOppNode) {
            BinOppNode binOppNode = (BinOppNode)expressionNode;
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
    public void visit(AssignNode rootNode) {
        rootNode.value_expression.accept(this);
    }

    @Override
    public void visit(PrintNode rootNode) {
        rootNode.print_expression.accept(this);
    }

    @Override
    public void visit(IfNode rootNode) {
        visit((ExpressionNode)rootNode.condition_expression);
        rootNode.if_statements.accept(this);
        rootNode.else_statements.accept(this);
    }

    @Override
    public void visit(WhileNode rootNode) {
        rootNode.if_statements.accept(this);
    }

    @Override
    public void visit(FunNode funCallNode) {
        funCallNode.body.accept(this);
    }

    @Override
    public void visit(FunCallNode funCallNode) {
    }

}
