package colang.interperter.LangObject.implementations;

import colang.interperter.LangObject.LangObject;
import colang.interperter.Type.CLType;

public class StringObject extends LangObject {

    public StringObject(CLType type, Object value) {
        super(type, value);
        children.put("length", new LangObject(CLType.NUMBER, value.toString().length()));
        functions.put("element", (index) -> {
            Integer element = Integer.parseInt(index.get(0).toString());
            return new LangObject(CLType.STRING, value.toString().charAt(element));
        });
    }
    
}
