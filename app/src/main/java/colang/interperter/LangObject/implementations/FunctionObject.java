package colang.interperter.LangObject.implementations;

import colang.interperter.LangObject.LangObject;
import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.BlockNode;
import colang.interperter.Type.CLType;

public class FunctionObject extends LangObject {
    public FunctionObject(CLType type, Object value) {
        super(type, value);
    }

    public FunctionObject(CLType type, Object value, BlockNode code) {
        super(type, value);
    }

}