package colang.interperter.CodeExcecutor.implementations;

import colang.interperter.CodeExcecutor.CodeExcecutor;
import colang.interperter.SyntaxTree.SyntaxTree;
import colang.interperter.SyntaxTree.SyntaxTreeVisitor.implementations.CodeRunVisitor;

public class CLCodeExecutor implements CodeExcecutor {
    @Override
    public void execute(SyntaxTree syntax_tree) {
        syntax_tree.root.accept(new CodeRunVisitor());
    } 
}
