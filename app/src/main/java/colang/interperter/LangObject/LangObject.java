package colang.interperter.LangObject;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import colang.interperter.Exception.implementations.MethodNotFoundException;
import colang.interperter.Exception.implementations.VariableNotFoundException;
import colang.interperter.LangObject.implementations.StringObject;
import colang.interperter.Type.CLType;

public class LangObject {
    public CLType type;
    public Object value;

    protected HashMap<String, Function<List<LangObject>, LangObject>>functions = new HashMap<String, Function<List<LangObject>, LangObject>>();
    public HashMap<String, LangObject> children = new HashMap<String, LangObject>();

    public LangObject call(String fun, List<LangObject> input) {
        Function<List<LangObject>, LangObject> function = functions.get(fun);
        if(function == null) {
            throw new MethodNotFoundException("Invalid function: " + fun);
        }
        return function.apply(input);
    }

    public LangObject property(String property) {
        LangObject prop = children.get(property);
        if(prop == null) {
            throw new VariableNotFoundException("Invalid property: " + property);
        }
        return prop;
    }

    public boolean hasProperty(String property) {
        LangObject prop = children.get(property);
        return prop != null;
    }

    public boolean hasMethod(String property) {
        return functions.get(property) != null;
    }
    
    public void setProperty(String property, LangObject value) {
        LangObject prop = children.get(property);
        if(prop == null) {
            throw new VariableNotFoundException("Invalid property: " + property);
        }
        children.put(property, value);
    }

    public LangObject(CLType type, Object value) {
        this.type = type;
        this.value = value;
        functions.put("toString", (index) -> {
            return new StringObject(CLType.STRING, value.toString());
        });
    }

    public LangObject(CLType type, Object value, boolean isNew) {
        this.type = type;
        this.value = value;
    }

}