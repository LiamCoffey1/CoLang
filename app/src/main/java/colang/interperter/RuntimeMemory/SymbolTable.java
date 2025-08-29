package colang.interperter.RuntimeMemory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;

import colang.interperter.LangObject.ClassDefinition;
import colang.interperter.LangObject.ComponentDefinition;
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
    private static HashMap<String, FunNode> fun_map = new HashMap<String, FunNode>();
    private static HashMap<String, ClassDefinition> class_defs = new HashMap<String, ClassDefinition>();
    private static HashMap<String, ComponentDefinition> component_defs = new HashMap<String, ComponentDefinition>();
    private static Deque<Frame> frames = new ArrayDeque<Frame>();

    private static final SymbolTable instance = new SymbolTable();

    private SymbolTable() {
        frames.add(new Frame());
    }

    public static SymbolTable getInstance() {
        return instance;
    }

    public FunNode getFunctionByName(String name) {
        if(getVariable(name) != null) {

        }
        return fun_map.get(name);
    }

    public FunNode createFunction(String name, FunNode value) {
        return fun_map.put(name, value);
    }

    public HashMap<String, ClassDefinition> getClassDefinitions() {
        return class_defs;
    }

    public HashMap<String, ComponentDefinition> getComponents() {
        return component_defs;
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
