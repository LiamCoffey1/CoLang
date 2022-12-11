package colang.interperter.CodeOptimizer;

import colang.interperter.SyntaxTree.SyntaxTree;

public interface CodeOptimzer {
    void optimize(SyntaxTree syntax_tree);
}
