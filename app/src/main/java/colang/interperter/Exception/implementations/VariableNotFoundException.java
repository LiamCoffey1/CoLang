package colang.interperter.Exception.implementations;

import colang.interperter.Exception.CLException;

public class VariableNotFoundException extends CLException {
    public VariableNotFoundException() {
        super();
    }
    public VariableNotFoundException(String s) {
        super(s);
    }
}
