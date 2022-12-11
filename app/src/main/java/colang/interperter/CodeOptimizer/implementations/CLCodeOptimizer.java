package colang.interperter.CodeOptimizer.implementations;

import colang.interperter.CodeOptimizer.CodeOptimzer;
import colang.interperter.SyntaxTree.SyntaxTree;
import colang.interperter.SyntaxTreeVisitor.implementations.OptimizerVisitor;

public class CLCodeOptimizer implements CodeOptimzer {

    @Override
    public void optimize(SyntaxTree syntax_tree) {
        syntax_tree.root.accept(new OptimizerVisitor());
    }
    
}
