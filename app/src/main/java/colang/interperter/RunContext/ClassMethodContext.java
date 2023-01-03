package colang.interperter.RunContext;

import colang.interperter.LangObject.LangObject;
import colang.interperter.LangObject.implementations.ClassObject;

public class ClassMethodContext implements RunContext {

    public ClassObject class_instance;
    public LangObject return_value;

    public ClassObject getClassInstance() {
        return class_instance;
    }

    public ClassMethodContext(ClassObject class_instance) {
        this.class_instance = class_instance;
    }

    @Override
    public Context getContext() {
        return Context.CLASS_METHOD;
    }
    
}
