package colang.interperter.SyntaxTreeGenerator;

import colang.interperter.SyntaxTree.SyntaxTree;
import colang.interperter.Tokenizer.TokenNode;
import colang.interperter.Tokenizer.TokenType;
import colang.interperter.Tokenizer.Tokenizer;
import colang.logging.Logger;

public abstract class SyntaxTreeGenerator {
    public TokenNode current_node;
    public SyntaxTree syntax_tree;
    protected abstract Tokenizer getTokenizer();
    protected abstract SyntaxTree createNodes();

    public SyntaxTree createTree(String code) {
        current_node = this.getTokenizer().tokenize(code);
        return createNodes();
    }

    public TokenNode getNextToken() {
        if (current_node == null) {
            return null;
        }
        TokenNode temp = current_node;
        current_node = current_node.next;
        return temp;
    }

    public TokenNode expectAndAdvance(TokenType type) {
        expect(type);
        getNextToken();
        return current_node;
    }

    public void expect(TokenType type) {
        if (current_node == null || current_node.type != type) {
            String un_expected = current_node == null ? "EMPTY_STRING" : current_node.token;
            Logger.logError("Unexpected token: " + un_expected + ", expected: " + type.value);
        }
    }
}
