package colang.interperter.SyntaxTreeVisitor.implementations;

import java.util.ArrayList;
import java.util.List;

import colang.interperter.LangObject.ClassDefinition;
import colang.interperter.LangObject.LangObject;
import colang.interperter.LangObject.implementations.ArrayObject;
import colang.interperter.LangObject.implementations.VoidObject;
import colang.interperter.RunContext.ClassMethodContext;
import colang.interperter.RunContext.Context;
import colang.interperter.RunContext.ContextManager;
import colang.interperter.RuntimeMemory.SymbolTable;
import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.*;
import colang.interperter.SyntaxTreeVisitor.SyntaxTreeVisitor;
import colang.interperter.Type.CLType;
import colang.logging.Logger;

public class CodeRunVisitor implements SyntaxTreeVisitor {

    @Override
    public void visit(BlockNode rootNode) {
        for (StatementNode statement : rootNode.children) {
            statement.accept(this);
        }
    }

    @Override
    public void visit(AssignNode assignNode) {
        assignNode.value_expression.accept(this);
        if(assignNode.identifier.next == null) {
            SymbolTable.getInstance().putVariable(assignNode.identifier.initial.id, assignNode.value_expression.calculate());
        } else {
            PropertyAccessNode property = (PropertyAccessNode) assignNode.identifier.next;
            assignNode.identifier.calculate();
            LangObject obj = assignNode.identifier.previous.current;
            obj.setProperty(property.getLastId(), assignNode.value_expression.calculate());
        }
    }

    @Override
    public void visit(PrintNode printNode) {
        printNode.print_expression.accept(this);
        LangObject object = printNode.print_expression.calculate();
        if(object.type == CLType.ARRAY) {
            List<LangObject> expressions = (List<LangObject>) object.value;
            String out = "[";
            int indx = 0;
            for(LangObject expression : expressions) {
                indx++;
                out += expression.value;
                if(indx != expressions.size()) {
                    out += ", ";
                }
            }
            out += "]";
            Logger.logOutput(out);
        } else if(object.type != CLType.VOID) {
            Logger.logOutput(printNode.print_expression.calculate().value);
        }
    }

    @Override
    public void visit(IfNode ifNode) {
        ExpressionNode expression = (ExpressionNode)ifNode.condition_expression;
        ifNode.condition_expression.accept(this);
        if ((boolean) expression.calculate().value) {
            if (ifNode.if_statements != null) {
                SymbolTable.getInstance().enterScope();
                ifNode.if_statements.accept(this);
                SymbolTable.getInstance().exitScope();
            }
        } else {
            if (ifNode.else_statements != null) {
                SymbolTable.getInstance().enterScope();
                ifNode.else_statements.accept(this);
                SymbolTable.getInstance().exitScope();
            }
        }
    }

    @Override
    public void visit(WhileNode whileNode) {
        whileNode.condition_expression.accept(this);
        while ((boolean) whileNode.condition_expression.calculate().value) {
            if (whileNode.if_statements != null) {
                SymbolTable.getInstance().enterScope();
                whileNode.if_statements.accept(this);
                SymbolTable.getInstance().exitScope();
            }
        }
    }

    @Override
    public void visit(FunNode funNode) {
        SymbolTable.getInstance()
            .getFunctions()
            .put(funNode.id, funNode);
    } 

    @Override
    public void visit(FunCallNode funCallNode) {
        int ind = 0;
        FunNode funNode = SymbolTable.getInstance().getFunctions().get(funCallNode.id);
        for (ExpressionNode expr : funCallNode.values) {
            SymbolTable.getInstance().putVariable(
                funNode.parameter_ids.get(ind),
                expr.calculate()
            );
            ind++;
        }
        SymbolTable.getInstance().enterScope();
        for (StatementNode statement : funNode.body.children) {
            if (statement instanceof ReturnStatement) {
                ReturnStatement return_sStatement = (ReturnStatement) statement;
                LangObject r_value = return_sStatement.return_value.calculate();
                funCallNode.return_value = r_value;
                break;
            } else {
                statement.accept(this);
            }
        }
        if(funCallNode.return_value == null) {
            funCallNode.return_value = new VoidObject(CLType.VOID, null);
            if(ContextManager.getContextType() == Context.CLASS_METHOD) {
                ClassMethodContext context = (ClassMethodContext) ContextManager.getContext();
                context.return_value = new VoidObject(CLType.VOID, null);
            }
        }
        SymbolTable.getInstance().exitScope();
    }

    @Override
    public ExpressionNode visit(ExpressionNode expressionNode) {
        if (expressionNode instanceof FunCallNode) {
            expressionNode.accept(this);
        }
        if (expressionNode instanceof BinOppNode) {
            BinOppNode binOppNode = (BinOppNode) expressionNode;
            binOppNode.left.accept(this);
            binOppNode.right.accept(this);
        }
        return expressionNode;
    }

    @Override
    public void visit(ExpressionStatement expressionStatement) {
        expressionStatement.expression.accept(this);
        expressionStatement.expression.calculate();
    }

    @Override
    public void visit(ForeachNode foreachNode) {
        ArrayObject expressions = (ArrayObject)  foreachNode.identifier.calculate();
        ArrayList<LangObject> exprs = (ArrayList<LangObject>) expressions.value;
        for (LangObject object : exprs) {
            SymbolTable.getInstance().enterScope();
            SymbolTable.getInstance().putVariable(foreachNode.value_name, object);
            foreachNode.statements.accept(this);
            SymbolTable.getInstance().exitScope();
        }
    }

    @Override
    public void visit(ClassDefinitionNode classDefinitionNode) {
        ClassDefinition class_def = new ClassDefinition();
        class_def.className = classDefinitionNode.className;
        classDefinitionNode.functions.forEach(function -> {
            class_def.functions.put(function.id, function);
        });
        class_def.assignments = classDefinitionNode.variables;
        class_def.constructor = classDefinitionNode.constructor;
        SymbolTable.getInstance().getClassDefinitions().put(class_def.className, class_def);
    }

    @Override
    public void visit(ReturnStatement returnStatement) {
        if(ContextManager.getContextType() == Context.CLASS_METHOD) {
            ClassMethodContext context = (ClassMethodContext) ContextManager.getContext();
            context.return_value = returnStatement.return_value.calculate();
        }
    }

    @Override
    public void visit(VariableDeclarationNode variableDeclarationNode) {
        SymbolTable.getInstance().putVariable(variableDeclarationNode.identifier, variableDeclarationNode.value_expression.calculate());
        
    }

}
