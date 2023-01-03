package colang.interperter.SyntaxTree.SyntaxTreeNode.implementations;

import java.util.ArrayList;
import java.util.List;

import colang.interperter.Exception.implementations.VariableNotFoundException;
import colang.interperter.LangObject.LangObject;
import colang.interperter.LangObject.implementations.ArrayObject;
import colang.interperter.LangObject.implementations.ClassObject;
import colang.interperter.LangObject.implementations.StringObject;
import colang.interperter.Operator.BinaryOperations;
import colang.interperter.RuntimeMemory.SymbolTable;
import colang.interperter.SyntaxTree.SyntaxTreeNode.SyntaxTreeNode;
import colang.interperter.SyntaxTreeVisitor.SyntaxTreeVisitor;
import colang.interperter.Type.CLType;

public class CLNodes {
    public static class BlockNode implements SyntaxTreeNode {
        public ArrayList<StatementNode> children = new ArrayList<StatementNode>();

        public void accept(SyntaxTreeVisitor v) {
            v.visit(this);
        }
    }

    public static class ClassDefinitionNode extends StatementNode {
        public String className;
        public FunNode constructor;
        public ArrayList<FunNode> functions = new ArrayList<FunNode>();
        public ArrayList<VariableDeclarationNode> variables = new ArrayList<VariableDeclarationNode>();

        public void accept(SyntaxTreeVisitor v) {
            v.visit(this);
        }
    }

    public static class VariableDeclarationNode extends StatementNode {
        public String identifier;
        public ExpressionNode value_expression;

        public void accept(SyntaxTreeVisitor v) {
            v.visit(this);
        }
    }

    public static class StatementNode implements SyntaxTreeNode {
        String type;

        public void accept(SyntaxTreeVisitor v) {
        }
    }

    public static class ExpressionStatement extends StatementNode {
        public ExpressionNode expression;

        public void accept(SyntaxTreeVisitor v) {
            v.visit(this);
        }
    }

    public static class AssignNode extends StatementNode {
        public String operator;
        public AccessNode identifier;
        public ExpressionNode value_expression;

        public LangObject calculate() {
            return value_expression.calculate();

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

    public static class ForeachNode extends StatementNode {
        public IdNode identifier;
        public String value_name;
        public BlockNode statements;

        @Override
        public void accept(SyntaxTreeVisitor v) {
            v.visit(this);
        }
    }

    public static class FunNode extends StatementNode {
        public String id;
        public BlockNode body;
        public List<String> parameter_ids = new ArrayList<String>();

        public FunNode() {
        }

        public FunNode(String id, BlockNode body, List<String> parameter_ids) {
            this.id = id;
            this.body = body;
            this.parameter_ids = parameter_ids;
        }

        @Override
        public void accept(SyntaxTreeVisitor v) {
            v.visit(this);
        }
    }

    public static class FunCallNode extends ExpressionNode {
        public String id;
        public LangObject return_value;
        public SyntaxTreeVisitor visitor;
        public List<ExpressionNode> values = new ArrayList<ExpressionNode>();

        @Override
        public void accept(SyntaxTreeVisitor v) {
            this.visitor = v;
            visitor.visit(this);
        }

        public LangObject calculate() {
            accept(visitor);
            return return_value;
        }
    }

    public static class ReturnStatement extends StatementNode {
        public ExpressionNode return_value;

        @Override
        public void accept(SyntaxTreeVisitor v) {
            v.visit(this);
        }
    }

    public static class ExpressionNode implements SyntaxTreeNode {
        public String type;

        public LangObject calculate() {
            return null;
        }

        public void accept(SyntaxTreeVisitor v) {
            v.visit(this);
        }
    }

    public static class BinOppNode extends ExpressionNode {
        public String operator;
        public ExpressionNode left;
        public ExpressionNode right;

        public void accept(SyntaxTreeVisitor v) {
            left.accept(v);
            right.accept(v);
        }

        @Override
        public LangObject calculate() {
            LangObject lefObject = left.calculate();
            LangObject rightObject = right.calculate();
            BinaryOperations.BinaryOperation operation = BinaryOperations.getInstance().getOperation(operator,
                    lefObject, rightObject);
            Object value = operation.operate(lefObject, rightObject);
            return new LangObject(operation.expected_type, value);
        }
    }

    public static class ValueNode extends ExpressionNode {
        public Object value;
        public CLType type;

        @Override
        public LangObject calculate() {
            switch (type) {
                case ARRAY:
                    List<LangObject> values = new ArrayList<LangObject>();
                    for (ExpressionNode expr : (List<ExpressionNode>) value) {
                        values.add(expr.calculate());
                    }
                    return new ArrayObject(type, values);
                case STRING:
                    return new StringObject(type, value.toString().substring(1, value.toString().length() - 1));
                default:
                    return new LangObject(type, value);

            }
        }
    }

    public static class InstanceCreateNode extends ExpressionNode {
        public String className;
        public List<AssignNode> assignments = new ArrayList<AssignNode>();
        public List<ExpressionNode> parameters = new ArrayList<ExpressionNode>();

        @Override
        public LangObject calculate() {
            ClassObject cl;
            if (parameters.size() > 0) {
                List<LangObject> values = new ArrayList<LangObject>();
                for (ExpressionNode expr : parameters) {
                    values.add(expr.calculate());
                }
                cl = new ClassObject(CLType.INSTANCE, className, values);
            } else {
                cl = new ClassObject(CLType.INSTANCE, className);
            }
            for (AssignNode ass : assignments) {
                cl.setProperty(ass.identifier.initial.id, ass.value_expression.calculate());
            }
            return cl;
        }
    }

    public static class AccessNode extends ExpressionNode {
        public AccessNode next;
        public AccessNode previous;
        public IdNode initial;
        public LangObject current;

        @Override
        public LangObject calculate() {
            if (next == null) {
                return initial.calculate();
            }
            previous = this;
            current = initial.calculate();
            next.current = current;
            return next.calculate();
        }
    }

    public static class FunAccessNode extends AccessNode {
        public FunCallNode functionCall;

        @Override
        public LangObject calculate() {
            List<LangObject> parameters = new ArrayList<LangObject>();
            for (ExpressionNode expr : functionCall.values) {
                parameters.add(expr.calculate());
            }
            current = current.call(functionCall.id, parameters);
            if (next == null) {
                return current;
            } else {
                previous = this;
                next.current = current;
                return next.calculate();
            }
        }
    }

    public static class PropertyAccessNode extends AccessNode {
        public String property;

        public String getLastId() {
            PropertyAccessNode pnext = (PropertyAccessNode) next;
            if (next != null) {
                return pnext.getLastId();
            }
            return property;
        }

        @Override
        public LangObject calculate() {
            current = current.property(property);
            if (next == null) {
                return current;
            } else {
                previous = this;
                next.current = current;
                return next.calculate();
            }
        }
    }

    public static class IdNode extends ExpressionNode {
        public String id;

        @Override
        public LangObject calculate() {
            LangObject val = SymbolTable.getInstance().getVariable(id);
            if (val == null) {
                throw new VariableNotFoundException("Invalid var: " + id);
            }
            return val;
        }
    }
}
