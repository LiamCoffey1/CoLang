package colang.interperter.Tokenizer.implementations;

import colang.interperter.Tokenizer.TokenNode;
import colang.interperter.Tokenizer.Tokenizer;

public class CLTokenizer implements Tokenizer {

    @Override
    public TokenNode tokenize(String token) {
        return createTokenList(token);
    }

    public static TokenNode createTokenList(String un_code) {
        TokenNode list = new TokenNode("block", null);
        String current_token = "";
        String code = un_code.concat(" ");
        boolean parsing_string = false;
        for (char c : code.toCharArray()) {
            if(parsing_string) {
                if(c == '"') {
                    parsing_string = false;
                    current_token += c;
                    list = insertToken(list, current_token);
                    current_token = "";
                } else {
                    current_token += c;
                }
            } else if (c == ' ') {
                if (current_token.length() > 0) {
                    list = insertToken(list, current_token);
                    current_token = "";
                }
            } else if (c == ';' || c == ',' || c == '(' || c == ')' ||  c == '{' ||  c == '}') {
                if (current_token.length() > 0) {
                    list = insertToken(list, current_token);
                }
                list = insertToken(list, "" + c);
                current_token = "";
            } else if (c == '"') {
                parsing_string = true;
                current_token += c;
            } else {
                current_token += c;
            }
        }
        return list;
    }

    public static TokenNode insertToken(TokenNode list, String val) {
        TokenNode temp = list;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = new TokenNode(val, null);
        return list;
    }
    
}
