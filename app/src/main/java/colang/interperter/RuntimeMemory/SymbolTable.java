package colang.interperter.RuntimeMemory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;

import colang.interperter.LangObject.ClassDefinition;
import colang.interperter.LangObject.LangObject;
import colang.interperter.RunContext.ClassMethodContext;
import colang.interperter.RunContext.Context;
import colang.interperter.RunContext.ContextManager;
import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.FunNode;

class Frame {
    private HashMap<String, LangObject> variables = new HashMap<String, LangObject>();

    public void putVariable(String id, LangObject value) {
        variables.put(id, value);
    }

    public LangObject getVariable(String id) {
        return variables.get(id);
    }
}

public class SymbolTable {
    public static HashMap<String, FunNode> fun_map = new HashMap<String, FunNode>();
    public static HashMap<String, ClassDefinition> class_defs = new HashMap<String, ClassDefinition>();
    private static Deque<Frame> frames = new ArrayDeque<Frame>();

    private static final SymbolTable instance = new SymbolTable();

    private SymbolTable() {
        frames.add(new Frame());
    }

    public static SymbolTable getInstance() {
        return instance;
    }

    public HashMap<String, FunNode> getFunctions() {
        return fun_map;
    }

    public HashMap<String, ClassDefinition> getClassDefinitions() {
        return class_defs;
    }

    public void enterScope() {
        frames.add(new Frame());
    }

    public void exitScope() {
        frames.removeLast();
    }

    public void putVariable(String id, LangObject value) {
        if(ContextManager.getContextType() == Context.CLASS_METHOD) {
            ClassMethodContext methodContext = (ClassMethodContext) ContextManager.getContext();
            if (methodContext.getClassInstance().hasProperty(id)) {
                methodContext.getClassInstance().setProperty(id, value);
                return;
            }
        }
        boolean found = false;
        Iterator<Frame> frameIterator = frames.descendingIterator();
        while (frameIterator.hasNext()) {
            Frame frame = frameIterator.next();
            LangObject frame_search_result =frame.getVariable(id);
            if (frame_search_result != null) {
                frame.putVariable(id, value);
               found = true;
            }
        }
        if(!found) {
            frames.getLast().putVariable(id, value);
        }
    }

    public LangObject getVariable(String id) {
        if(ContextManager.getContextType() == Context.CLASS_METHOD) {
            ClassMethodContext methodContext = (ClassMethodContext) ContextManager.getContext();
            if (methodContext.getClassInstance().hasProperty(id)) {
                return methodContext.getClassInstance().property(id);
            }
        }
        Iterator<Frame> frameIterator = frames.descendingIterator();
        while (frameIterator.hasNext()) {
            LangObject frame_search_result = frameIterator.next().getVariable(id);
            if (frame_search_result != null) {
                return frame_search_result;
            }
        }
        return null;
    }
}
