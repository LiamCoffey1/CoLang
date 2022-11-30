package colang.codeGenerator;

import colang.SymbolTable;
import colang.semanticAnalysis.TreeNodeVisitor;
import colang.semanticAnalysis.nodes.Nodes.AssignNode;
import colang.semanticAnalysis.nodes.Nodes.BlockNode;
import colang.semanticAnalysis.nodes.Nodes.IfNode;
import colang.semanticAnalysis.nodes.Nodes.PrintNode;
import colang.semanticAnalysis.nodes.Nodes.StatementNode;

public class CodeGenerator {

    public void runCode(BlockNode initial_block) {
        CodeRunVisitor code_visitor = new CodeRunVisitor();
        code_visitor.visit(initial_block);
    }
    
    public class CodeRunVisitor extends TreeNodeVisitor {
        @Override
        public void visit(BlockNode rootNode) {
            for(StatementNode statement : rootNode.children) {
                statement.accept(this);
            }
        }

        @Override
        public void visit(AssignNode assignNode) {
            SymbolTable.getSymbolTable().put(assignNode.identifier, assignNode.value_expression.calculate());
        }

        @Override
        public void visit(PrintNode printNode) {
            System.out.println(printNode.print_expression.calculate());
        }

        @Override
        public void visit(IfNode ifNode) {
            if((boolean)ifNode.condition_expression.calculate()) {
                if(ifNode.if_statements != null) {
                    ifNode.if_statements.accept(this);
                }
            } else {
                if(ifNode.else_statements != null) {
                    ifNode.else_statements.accept(this);
                }
            }
        }
    }
}
