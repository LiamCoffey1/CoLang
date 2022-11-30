package colang.semanticAnalysis;

import colang.semanticAnalysis.nodes.Nodes.AssignNode;
import colang.semanticAnalysis.nodes.Nodes.BlockNode;
import colang.semanticAnalysis.nodes.Nodes.IfNode;
import colang.semanticAnalysis.nodes.Nodes.PrintNode;

public abstract class TreeNodeVisitor {
    public abstract void visit(BlockNode rootNode);
    public abstract void visit(AssignNode rootNode);
    public abstract void visit(PrintNode rootNode);
    public abstract void visit(IfNode rootNode);
 }

