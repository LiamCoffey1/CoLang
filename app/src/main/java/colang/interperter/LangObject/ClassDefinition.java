package colang.interperter.LangObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.FunNode;
import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.VariableDeclarationNode;

public class ClassDefinition {
    public String className;
    public FunNode constructor;
    public HashMap<String, FunNode> functions = new HashMap<String, FunNode>();
    public List<VariableDeclarationNode> assignments = new ArrayList<VariableDeclarationNode>();
}
