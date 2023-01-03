package colang.interperter.Exception.implementations;

import colang.interperter.Exception.CLException;

public class TypeMismatchException extends CLException {
    public TypeMismatchException() {
        super();
    }
    public TypeMismatchException(String s) {
        super(s);
    }
}
