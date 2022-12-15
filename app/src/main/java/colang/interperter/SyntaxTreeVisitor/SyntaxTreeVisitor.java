package colang.interperter.SyntaxTreeVisitor;

import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.*;

public interface SyntaxTreeVisitor {
    public abstract void visit(BlockNode blockNode);
    public abstract void visit(AssignNode rootNode);
    public abstract void visit(PrintNode rootNode);
    public abstract void visit(IfNode rootNode);
    public abstract void visit(WhileNode rootNode);
    public abstract void visit(FunNode funCallNode);
    public abstract void visit(FunCallNode funCallNode);
    public abstract ExpressionNode visit(ExpressionNode expressionNode);
}


