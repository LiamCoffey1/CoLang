package colang.interperter.LangObject.implementations;

import java.util.List;

import colang.interperter.LangObject.LangObject;
import colang.interperter.Type.CLType;

public class ArrayObject extends LangObject {

    public ArrayObject(CLType type, Object value) {
        super(type, value);
        List<LangObject> list = (List<LangObject>)value;
        updateLength();
        functions.put("add", (param) -> {
            LangObject element = param.get(0);
            list.add(element);
            updateLength();
            return this;
        });
        functions.put("get", (param) -> {
            Integer element = Integer.parseInt(param.get(0).value.toString());
            return list.get(element);
        });
    }

    void updateLength() {
        List<LangObject> list = (List<LangObject>)value;
        children.put("length", new LangObject(CLType.NUMBER, list.size()));
    }
    
}
