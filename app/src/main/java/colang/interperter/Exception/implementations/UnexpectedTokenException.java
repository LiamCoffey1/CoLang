package colang.interperter.Exception.implementations;

import colang.interperter.Exception.CLException;

public class UnexpectedTokenException extends CLException {
    public UnexpectedTokenException() {
        super();
    }
    public UnexpectedTokenException(String s) {
        super(s);
    }
}
