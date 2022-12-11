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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SyntaxTreeGenerator treeGenerator = new CLSyntaxTreeGenerator();
        CodeOptimzer optimizer = new CLCodeOptimizer();
        CodeExcecutor excecutor = new CLCodeExecutor();
        CodeInterperter interperter = new CodeInterperter(treeGenerator, optimizer, excecutor);
        while (sc.hasNextLine()) {
            String code = sc.nextLine();
            try {
                interperter.interpert(code);
            } catch (Exception e) {
                continue;
            }
        }
        sc.close();
    }
}
