package colang.interperter.CodeExcecutor;

import colang.interperter.SyntaxTree.SyntaxTree;

public interface CodeExcecutor {
    void execute(SyntaxTree syntax_tree);
}
