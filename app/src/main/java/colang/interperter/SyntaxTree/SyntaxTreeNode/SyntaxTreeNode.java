package colang.interperter.SyntaxTree.SyntaxTreeNode;

import colang.interperter.SyntaxTree.SyntaxTreeVisitor.SyntaxTreeVisitor;

public interface SyntaxTreeNode {
    void accept(SyntaxTreeVisitor v);
}
