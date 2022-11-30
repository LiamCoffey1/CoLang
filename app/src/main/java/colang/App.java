package colang;

import java.util.Scanner;

import colang.codeGenerator.CodeGenerator;
import colang.lexicalAnalysis.Lexer;
import colang.lexicalAnalysis.TokenNode;
import colang.semanticAnalysis.SyntaxTreeGenerator;
import colang.semanticAnalysis.nodes.Nodes.BlockNode;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String code = sc.nextLine();
            TokenNode token_list_root = Lexer.createTokenList(code);
            BlockNode tree = new SyntaxTreeGenerator(token_list_root).createBlock();
            new CodeGenerator().runCode(tree);
        }
        sc.close();
    }
}
