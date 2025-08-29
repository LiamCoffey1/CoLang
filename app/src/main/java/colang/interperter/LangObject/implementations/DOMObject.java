package colang.interperter.LangObject.implementations;

import colang.interperter.LangObject.DOM;
import colang.interperter.LangObject.LangObject;
import colang.interperter.Type.CLType;

public class DOMObject extends LangObject {

    public DOM dom;

    public DOMObject(CLType type, Object value) {
        super(type, value);
        this.dom = (DOM)value;
        //TODO Auto-generated constructor stub
    }
    
}
