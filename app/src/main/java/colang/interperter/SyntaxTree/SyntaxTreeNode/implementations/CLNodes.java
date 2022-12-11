package colang.interperter.SyntaxTree.SyntaxTreeNode.implementations;

import java.util.ArrayList;
import java.util.List;

import colang.SymbolTable;
import colang.interperter.SyntaxTree.SyntaxTreeNode.SyntaxTreeNode;
import colang.interperter.SyntaxTree.SyntaxTreeVisitor.SyntaxTreeVisitor;
import colang.logging.Logger;

public class CLNodes {
    public static class BlockNode implements SyntaxTreeNode {
        public ArrayList<StatementNode> children = new ArrayList<StatementNode>();
        public void accept(SyntaxTreeVisitor v) {
           v.visit(this);
        }
    }

    public static class StatementNode implements SyntaxTreeNode {
        String type;
        public void accept(SyntaxTreeVisitor v) {}
    }

    public static class AssignNode extends StatementNode {
        public String operator;
        public String identifier;
        public ExpressionNode value_expression;

        public Object calculate() {
            if(operator.equals("=")) {
                return value_expression.calculate();
            }
            Object expr_value = SymbolTable.getSymbolTable().get(identifier);
            if(expr_value == null) {
                Logger.logError("Invalid var: " + identifier);
            }
            int val = Integer.parseInt(expr_value.toString());
            if(operator.equals("+=")) {
                return val +=  Integer.parseInt(value_expression.calculate().toString());
            } else if(operator.equals("*=")) {
                return val *=  Integer.parseInt(value_expression.calculate().toString());
            } else if(operator.equals("/=")) {
                return val /=  Integer.parseInt(value_expression.calculate().toString());
            } else if(operator.equals("-=")) {
                return val -=  Integer.parseInt(value_expression.calculate().toString());
            }
            return null;
        }

        public void accept(SyntaxTreeVisitor v) {
            v.visit(this);
        }
    }

    public static class PrintNode extends StatementNode {
        public ExpressionNode print_expression;
        @Override
        public void accept(SyntaxTreeVisitor v) { 
            v.visit(this);
        }
        public PrintNode createNode() { 
            return this;
        }
    }

    public static class IfNode extends StatementNode {
        public SyntaxTreeNode condition_expression;
        public SyntaxTreeNode if_statements;
        public SyntaxTreeNode else_statements;

        @Override
        public void accept(SyntaxTreeVisitor v) { 
            v.visit(this);
        }
    }

    public static class WhileNode extends StatementNode {
        public ExpressionNode condition_expression;
        public BlockNode if_statements;

        @Override
        public void accept(SyntaxTreeVisitor v) { 
            v.visit(this);
        }
    }

    public static class FunNode extends StatementNode {
        public String id;
        public BlockNode body;
        public List<String> parameter_ids = new ArrayList<String>();

        @Override
        public void accept(SyntaxTreeVisitor v) { 
            v.visit(this);
        }
    }

    public static class FunCallNode extends StatementNode {
        public String id;
        public List<ExpressionNode> values = new ArrayList<ExpressionNode>();

        @Override
        public void accept(SyntaxTreeVisitor v) { 
            v.visit(this);
        }
    }

    public static class ExpressionNode implements SyntaxTreeNode {
        public String type;
        public Object calculate() {
            return null;
        }
        public void accept(SyntaxTreeVisitor v) { }
    }

    public static class BinOppNode extends ExpressionNode {
        public String operator;
        public ExpressionNode left;
        public ExpressionNode right;

        @Override
        public Object calculate() {
            if(operator.equals("&&") || operator.equals("||")) {
                boolean leftb = Boolean.parseBoolean(left.calculate().toString());
                boolean rightb = Boolean.parseBoolean(right.calculate().toString());
                return operator.equals("&&") ? leftb && rightb :leftb || rightb;
            }
            int lefti = Integer.parseInt(left.calculate().toString());
            int righti = Integer.parseInt(right.calculate().toString());
            if(operator.equals("+")) {
                return lefti + righti;
            } else if(operator.equals("*")) {
                return lefti * righti;
            } else if(operator.equals("/")) {
                return lefti / righti;
            } else if(operator.equals("-")) {
                return lefti - righti;
            }
            return null;
        }
    }

    public static class CompareNode extends ExpressionNode {
        public String operator;
        public ExpressionNode left;
        public ExpressionNode right;

        @Override
        public Object calculate() {
            int lefti = Integer.parseInt(left.calculate().toString());
            int righti = Integer.parseInt(right.calculate().toString());
            if(operator.equals("==")) {
                return lefti == righti;
            } else if(operator.equals(">")) {
                return lefti > righti;
            } else if(operator.equals("<")) {
                return lefti < righti;
            } else if(operator.equals(">=")) {
                return lefti >= righti;
            } else if(operator.equals("<=")) {
                return lefti <= righti;
            }
            return null;
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
            Object val = SymbolTable.getSymbolTable().get(id);
            if(val == null) {
                Logger.logError("Invalid var: " + id);
                val.toString();
            }
            return val;
        }
    }
}
