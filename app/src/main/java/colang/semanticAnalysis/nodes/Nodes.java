package colang.semanticAnalysis.nodes;

import java.util.ArrayList;

import colang.SymbolTable;
import colang.semanticAnalysis.TreeNodeVisitor;

public class Nodes {
    
    public static abstract class VisitableNode {
        abstract void accept(TreeNodeVisitor v);
     }

     public static class BlockNode extends VisitableNode {
        public ArrayList<StatementNode> children = new ArrayList<StatementNode>();
        public void accept(TreeNodeVisitor v) {
           v.visit(this);
        }
    }

    public static class StatementNode extends VisitableNode {
        String type;

        @Override
        public void accept(TreeNodeVisitor v) {}
    }

    public static class AssignNode extends StatementNode {
        public String identifier;
        public ExpressionNode value_expression;
        public void accept(TreeNodeVisitor v) {
            v.visit(this);
        }
    }

    public static class PrintNode extends StatementNode {
        public ExpressionNode print_expression;
        @Override
        public void accept(TreeNodeVisitor v) { 
            v.visit(this);
        }
    }

    public static class IfNode extends StatementNode {
        public ExpressionNode condition_expression;
        public BlockNode if_statements;
        public BlockNode else_statements;

        @Override
        public void accept(TreeNodeVisitor v) { 
            v.visit(this);
        }
    }

    public static class ExpressionNode extends VisitableNode {
        public String type;

        public Object calculate() {
            return null;
        }

        @Override
        public void accept(TreeNodeVisitor v) { 
        }
    }

    public static class BinOppNode extends ExpressionNode {
        public String operator;
        public ExpressionNode left;
        public ExpressionNode right;

        @Override
        public Object calculate() {
            return left.calculate().toString() + right.calculate().toString();
        }
    }

    public static class ValueNode extends ExpressionNode {
        public Object value;

        @Override
        public
        Object calculate() {
            return value;
        }
    }

    public static class IdNode extends ExpressionNode {
        public String id;

        @Override
        public Object calculate() {
            return SymbolTable.getSymbolTable().get(id);
        }
    }
}
