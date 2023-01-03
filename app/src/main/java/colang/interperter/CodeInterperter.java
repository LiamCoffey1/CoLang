package colang.interperter;

import colang.interperter.CodeExcecutor.CodeExcecutor;
import colang.interperter.CodeOptimizer.CodeOptimzer;
import colang.interperter.SyntaxTree.SyntaxTree;
import colang.interperter.SyntaxTreeGenerator.SyntaxTreeGenerator;

public class CodeInterperter {
    private SyntaxTreeGenerator treeGenerator;
    private CodeOptimzer optimizer;
    private CodeExcecutor excecutor;
    private SyntaxTree syntax_tree;

    public CodeInterperter(SyntaxTreeGenerator treeGenerator, CodeOptimzer optimizer, CodeExcecutor excecutor) {
        this.excecutor = excecutor;
        this.optimizer = optimizer;
        this.treeGenerator = treeGenerator;
    }

    public void interpert(String code) {
        syntax_tree = treeGenerator.createTree(code);
        optimizer.optimize(syntax_tree);
        excecutor.execute(syntax_tree);
    }

    public void interpert(SyntaxTree tree) {
        excecutor.execute(tree);
    }
}