package colang.interperter.SyntaxTree.SyntaxTreeNode;

import colang.interperter.SyntaxTreeVisitor.SyntaxTreeVisitor;

public interface SyntaxTreeNode {
    void accept(SyntaxTreeVisitor v);
}
