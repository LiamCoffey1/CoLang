package colang.interperter.Exception.implementations;

import colang.interperter.Exception.CLException;

public class MethodNotFoundException extends CLException {
    public MethodNotFoundException() {
        super();
    }
    public MethodNotFoundException(String s) {
        super(s);
    }
}
