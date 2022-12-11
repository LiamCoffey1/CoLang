package colang.interperter.Tokenizer;

public class TokenNode {
    public String token;
    public TokenNode next;

    public TokenNode(String token, TokenNode next) {
        this.token = token;
        this.next = next;
    }
}
