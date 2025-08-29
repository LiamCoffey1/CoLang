package colang.interperter.Tokenizer.implementations;

import colang.interperter.Tokenizer.TokenNode;
import colang.interperter.Tokenizer.TokenType;
import colang.interperter.Tokenizer.Tokenizer;

public class CLTokenizer implements Tokenizer {

    @Override
    public TokenNode tokenize(String token) {
        return createTokenList(token);
    }

    public static TokenNode createTokenList(String un_code) {
        TokenNode list = new TokenNode(TokenType.BLOCK, null, null);
        String current_token = "";
        String code = un_code.concat(" ");
        boolean parsing_string = false;
        boolean parsing_xml = false;
        boolean multiline_comment = false;
        for (char c : code.toCharArray()) {
            if (current_token.equals("/*")) {
                multiline_comment = true;
                current_token = "";
            }
            if(multiline_comment) {
                if (c == '*' && !current_token.equals("*")) {
                    current_token += c;
                }
                if (c == '/') {
                    current_token += c;
                }
                if (current_token.equals("*/")) {
                    current_token = "";
                    multiline_comment = false;
                }
            } else {
                if (current_token.equals("//")) 
                    break;
                if (parsing_string) {
                    if (c == '"') {
                        parsing_string = false;
                        current_token += c;
                        list = insertToken(list, TokenType.STRING, current_token);
                        current_token = "";
                    } else {
                        current_token += c;
                    }
                } else if (parsing_xml) {
                        if (c == '$') {
                            parsing_xml = false;
                            current_token += c;
                            list = insertToken(list, TokenType.XML, current_token);
                            current_token = "";
                        } else {
                            current_token += c;
                        }
                } else if (c == ' ') {
                    if (current_token.length() > 0) {
                        list = insertToken(list, current_token);
                        current_token = "";
                    }
                } else if (c == ',' || c == '[' || c == ']' || c == '.' || c == '(' || c == ')') {
                    if (current_token.length() > 0) {
                        list = insertToken(list, current_token);
                    }
                    list = insertToken(list, "" + c);
                    current_token = "";
                } else if (c == '"') {
                    parsing_string = true;
                    current_token += c;
                } else if (c == '$') {
                    parsing_xml = true;
                    current_token += c;
                } else {
                    current_token += c;
                }
            }
        }
        return list;
    }

    public static TokenNode insertToken(TokenNode list, TokenType type, String val) {
        TokenNode temp = list;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = new TokenNode(type, val, null);
        return list;
    }

    public static TokenNode insertToken(TokenNode list, String val) {
        for(TokenType t : TokenType.values()) {
            if(val.matches(t.value)) {
                return insertToken(list, t, val);
            }
        }
        return insertToken(list, null, val);
    }
    
}
