package colang.interperter.SyntaxTreeVisitor.implementations;

import colang.interperter.RuntimeMemory.SymbolTable;
import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.*;
import colang.interperter.SyntaxTreeVisitor.SyntaxTreeVisitor;
import colang.logging.Logger;

public class CodeRunVisitor implements SyntaxTreeVisitor {
    @Override
    public void visit(BlockNode rootNode) {
        SymbolTable.getInstance().enterScope();
        for (StatementNode statement : rootNode.children) {
            statement.accept(this);
        }
        SymbolTable.getInstance().exitScope();
    }

    @Override
    public void visit(AssignNode assignNode) {
        SymbolTable.getInstance().putVariable(assignNode.identifier, assignNode.calculate());
    }

    @Override
    public void visit(PrintNode printNode) {
        Logger.logOutput(printNode.print_expression.calculate());
    }

    @Override
    public void visit(IfNode ifNode) {
        ExpressionNode expression = (ExpressionNode)ifNode.condition_expression;
        if ((boolean) expression.calculate()) {
            if (ifNode.if_statements != null) {
                ifNode.if_statements.accept(this);
            }
        } else {
            if (ifNode.else_statements != null) {
                ifNode.else_statements.accept(this);
            }
        }
    }

    @Override
    public void visit(WhileNode whileNode) {
        while ((boolean) whileNode.condition_expression.calculate()) {
            if (whileNode.if_statements != null) {
                whileNode.if_statements.accept(this);
            }
        }
    }

    @Override
    public void visit(FunNode funNode) {
        SymbolTable.getInstance()
            .getFunctions()
            .put(funNode.id, funNode);
    } 

    @Override
    public void visit(FunCallNode funCallNode) {
        int ind = 0;
        for (ExpressionNode expr : funCallNode.values) {
            SymbolTable.getInstance().putVariable(
                SymbolTable.getInstance()
                    .getFunctions()
                    .get(funCallNode.id).parameter_ids
                    .get(ind),
                expr.calculate()
            );
            ind++;
        }
        SymbolTable.getInstance()
            .getFunctions()
            .get(funCallNode.id).body
            .accept(this);
    }

    @Override
    public ExpressionNode visit(ExpressionNode expressionNode) {
        return expressionNode;
    }
}
