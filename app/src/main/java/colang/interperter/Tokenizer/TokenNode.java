package colang.interperter.Tokenizer;

public class TokenNode {
    public String token;
    public TokenType type;
    public TokenNode next;

    public TokenNode(TokenType type, String token, TokenNode next) {
        this.type = type;
        this.token = token;
        this.next = next;
    }
}
