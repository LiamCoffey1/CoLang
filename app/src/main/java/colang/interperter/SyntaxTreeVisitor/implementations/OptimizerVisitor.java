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

    @Override
    public void visit(ExpressionStatement expressionStatement) {
    }

    @Override
    public void visit(ForeachNode foreachNode) {
    }

    @Override
    public void visit(ClassDefinitionNode classDefinitionNode) {
        
    }

    @Override
    public void visit(ReturnStatement returnStatement) {
        
    }

    @Override
    public void visit(VariableDeclarationNode variableDeclarationNode) {
        
    }
}
