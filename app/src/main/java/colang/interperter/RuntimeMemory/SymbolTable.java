package colang;

import java.util.HashMap;

import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.FunNode;

public class SymbolTable {
    public static HashMap<String, FunNode> fun_map = new HashMap<String, FunNode>();
    public static HashMap<String, FunNode> getFunctions() {
        return fun_map;
    }
    public static HashMap<String, Object> variable_map = new HashMap<String, Object>();
    public static HashMap<String, Object> getSymbolTable() {
        return variable_map;
    }
}
