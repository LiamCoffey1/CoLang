package colang;

import java.util.Scanner;

import colang.interperter.CodeInterperter;
import colang.interperter.CodeExcecutor.CodeExcecutor;
import colang.interperter.CodeExcecutor.implementations.CLCodeExecutor;
import colang.interperter.CodeOptimizer.CodeOptimzer;
import colang.interperter.CodeOptimizer.implementations.CLCodeOptimizer;
import colang.interperter.SyntaxTreeGenerator.SyntaxTreeGenerator;
import colang.interperter.SyntaxTreeGenerator.implementations.CLSyntaxTreeGenerator;

public class App {

    private static final  boolean DEBUG_MODE = true;
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SyntaxTreeGenerator treeGenerator = new CLSyntaxTreeGenerator();
        CodeOptimzer optimizer = new CLCodeOptimizer();
        CodeExcecutor excecutor = new CLCodeExecutor();
        CodeInterperter interperter = new CodeInterperter(treeGenerator, optimizer, excecutor);
        String codeBuffer = "";
        System.out.println("******* -- COLANG -- *********");
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            try {
                if(line.equals("/r")) {
                    interperter.interpert(codeBuffer);
                    codeBuffer = "";
                } else if(line.equals("/c")) {
                    codeBuffer = "";
                } else {
                    codeBuffer += line + " ";
                }
            } catch (Exception e) {
                if(DEBUG_MODE) {
                    e.printStackTrace();
                    codeBuffer = "";
                }
            }
        }
        sc.close();
    }
}
