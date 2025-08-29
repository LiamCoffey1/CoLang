package colang.interperter.Tokenizer;

public enum TokenType {
    BOOLEAN("(true)|(false)"),
    BLOCK(""),
    STRING(""),
    IF("if"),
    VIEW("view"),
    ELSE("else"),
    NEW("new"),
    COMPONENT("component"),
    XML(""),
    WHILE("while"),
    VAR("var"),
    CLASS("class"),
    FOREACH("foreach"),
    LAMDA("lamda"),
    CONSTRUCTOR("init"),
    IN("in"),
    DOT("\\."),
    PRINT("print"),
    FUN("fun"),
    SET("set"),
    RETURN("return"),
    LEFT_BOX("\\["),
    RIGHT_BOX("\\]"),
    LEFT_CURLY("\\{"),
    RIGHT_CURLY("\\}"),
    LEFT_PAREN("\\("),
    RIGHT_PAREN("\\)"),
    COMMA(","),
    BOOLEAN_OPERATOR("(&&)|(\\|\\|)"),
    TERM_OPERATOR("\\+|-"),
    FACTOR_OPERATOR("\\*|/"),
    ASSIGNMENT_OPERATOR("=|(\\+=)|(-=)|(\\/=)|(\\*=)"),
    EQUALITY_OPERATOR("(==)|(!=)|(>=)|>|<|(<=)"),
    IDENTIFIER("[a-zA-Z_$][a-zA-Z0-9]+"),
    NUMBER("[0-9]*");

    public String value;

    TokenType(String regex) {
        this.value = regex;
    }

    public String toString() {
        return value;
    }

}