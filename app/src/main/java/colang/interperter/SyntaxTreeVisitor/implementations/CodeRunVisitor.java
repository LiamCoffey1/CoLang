package colang.interperter.SyntaxTreeVisitor.implementations;

import colang.interperter.RuntimeMemory.SymbolTable;
import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.*;
import colang.interperter.SyntaxTreeVisitor.SyntaxTreeVisitor;
import colang.logging.Logger;

public class CodeRunVisitor implements SyntaxTreeVisitor {
    @Override
    public void visit(BlockNode rootNode) {
        for(StatementNode statement : rootNode.children) {
            statement.accept(this);
        }
    }

    @Override
    public void visit(AssignNode assignNode) {
        SymbolTable.getSymbolTable().put(assignNode.identifier, assignNode.calculate());
    }

    @Override
    public void visit(PrintNode printNode) {
        Logger.logOutput(printNode.print_expression.calculate());
    }

    @Override
    public void visit(IfNode ifNode) {
        ExpressionNode expression = (ExpressionNode)ifNode.condition_expression;
        if ((boolean) expression.calculate()) {
            if(ifNode.if_statements != null) {
                ifNode.if_statements.accept(this);
            }
        } else {
            if(ifNode.else_statements != null) {
                ifNode.else_statements.accept(this);
            }
        }
    }

    @Override
    public void visit(WhileNode rootNode) {
        while ((boolean) rootNode.condition_expression.calculate()) {
            if(rootNode.if_statements != null) {
                rootNode.if_statements.accept(this);
            }
        }
    }

    @Override
    public void visit(FunNode funNode) {
        SymbolTable.getFunctions().put(funNode.id, funNode);
    }

    @Override
    public void visit(FunCallNode funCallNode) {
        int ind = 0;
        for(ExpressionNode expr : funCallNode.values) {
            SymbolTable.getSymbolTable().put(SymbolTable.getFunctions().get(funCallNode.id).parameter_ids.get(ind), expr.calculate());
            ind++;
        }
        SymbolTable.getFunctions().get(funCallNode.id).body.accept(this);
    }

    @Override
    public ExpressionNode visit(ExpressionNode expressionNode) {
        return expressionNode;
    }
}
