package colang.interperter.RuntimeMemory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.FunNode;

public class SymbolTable {
    public static HashMap<String, FunNode> fun_map = new HashMap<String, FunNode>();
    private Deque<Frame> frames = new ArrayDeque<Frame>();
    private static SymbolTable instance = null;
    
    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }
    private SymbolTable() {}

    private class Frame {
        private HashMap<String, Object> variables = new HashMap<String, Object>();

        public void putVariable(String id, Object value) {
            variables.put(id, value);
        }

        public Object getVariable(String id) {
            return variables.get(id);
        }
    }

    public HashMap<String, FunNode> getFunctions() {
        return fun_map;
    }

    public void enterScope() {
        frames.add(new Frame());
    }

    public void exitScope() {
        frames.removeLast();
    }

    public void putVariable(String id, Object value) {
        frames.getLast().putVariable(id, value);
    }

    public Object getVariable(String id) {
        Iterator<Frame> frameIterator = frames.descendingIterator();
        while (frameIterator.hasNext()) {
            Object frame_search_result = frameIterator.next().getVariable(id);
            if (frame_search_result != null) {
                return frame_search_result;
            }
        }
        return null;
    }
}
