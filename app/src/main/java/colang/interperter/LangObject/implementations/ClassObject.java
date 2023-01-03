package colang.interperter.LangObject.implementations;

import java.util.List;

import colang.interperter.CodeInterperter;
import colang.interperter.CodeExcecutor.CodeExcecutor;
import colang.interperter.CodeExcecutor.implementations.CLCodeExecutor;
import colang.interperter.CodeOptimizer.CodeOptimzer;
import colang.interperter.CodeOptimizer.implementations.CLCodeOptimizer;
import colang.interperter.LangObject.ClassDefinition;
import colang.interperter.LangObject.LangObject;
import colang.interperter.RunContext.ClassMethodContext;
import colang.interperter.RunContext.ContextManager;
import colang.interperter.RunContext.GlobalContext;
import colang.interperter.RuntimeMemory.SymbolTable;
import colang.interperter.SyntaxTree.SyntaxTree;
import colang.interperter.SyntaxTree.SyntaxTreeNode.implementations.CLNodes.FunNode;
import colang.interperter.SyntaxTreeGenerator.SyntaxTreeGenerator;
import colang.interperter.SyntaxTreeGenerator.implementations.CLSyntaxTreeGenerator;
import colang.interperter.Type.CLType;

public class ClassObject extends LangObject {

    ClassDefinition definition;

    public ClassObject(CLType type, Object value) {
        super(type, value);
        String class_name = value.toString();
        definition = SymbolTable.getInstance().getClassDefinitions().get(class_name);
        definition.assignments.forEach(assignment -> {
            this.children.put(assignment.identifier, assignment.value_expression.calculate());
        });
        SyntaxTreeGenerator treeGenerator = new CLSyntaxTreeGenerator();
        CodeOptimzer optimizer = new CLCodeOptimizer();
        CodeExcecutor excecutor = new CLCodeExecutor();
        CodeInterperter interperter = new CodeInterperter(treeGenerator, optimizer, excecutor);
        FunNode func = definition.constructor;
        if(func != null) {
            ContextManager.switchContext(new ClassMethodContext(this));
            SymbolTable.getInstance().enterScope();
            SyntaxTree tree = new SyntaxTree();
            tree.root = func.body;
            interperter.interpert(tree);
            SymbolTable.getInstance().exitScope();
            ContextManager.switchContext(new GlobalContext());
        }
    }

    public ClassObject(CLType type, Object value, List<LangObject> parameters) {
        super(type, value);
        String class_name = value.toString();
        definition = SymbolTable.getInstance().getClassDefinitions().get(class_name);
        definition.assignments.forEach(assignment -> {
            this.children.put(assignment.identifier, assignment.value_expression.calculate());
        });
        SyntaxTreeGenerator treeGenerator = new CLSyntaxTreeGenerator();
        CodeOptimzer optimizer = new CLCodeOptimizer();
        CodeExcecutor excecutor = new CLCodeExecutor();
        CodeInterperter interperter = new CodeInterperter(treeGenerator, optimizer, excecutor);
        FunNode func = definition.constructor;
        if(func != null) {
            ContextManager.switchContext(new ClassMethodContext(this));
            SymbolTable.getInstance().enterScope();
            int i = 0;
            for(String id : func.parameter_ids) {
                SymbolTable.getInstance().putVariable(id, parameters.get(i));
                i++;
            }
            SymbolTable.getInstance();
            SyntaxTree tree = new SyntaxTree();
            tree.root = func.body;
            interperter.interpert(tree);
            SymbolTable.getInstance().exitScope();
            ContextManager.switchContext(new GlobalContext());
        }
    }


    @Override
    public LangObject call(String fun, List<LangObject> input) {
        SyntaxTreeGenerator treeGenerator = new CLSyntaxTreeGenerator();
        CodeOptimzer optimizer = new CLCodeOptimizer();
        CodeExcecutor excecutor = new CLCodeExecutor();
        CodeInterperter interperter = new CodeInterperter(treeGenerator, optimizer, excecutor);
        ContextManager.switchContext(new ClassMethodContext(this));
        SymbolTable.getInstance().enterScope();
        FunNode func = definition.functions.get(fun);
        int ind = 0;
        for (String param_name : func.parameter_ids) {
            SymbolTable.getInstance().putVariable(
                param_name,
                input.get(ind)
            );
            ind++;
        }
        SyntaxTree tree = new SyntaxTree();
        tree.root = definition.functions.get(fun).body;
        interperter.interpert(tree);
        ClassMethodContext current_context = (ClassMethodContext) ContextManager.getContext();
        LangObject return_value = current_context.return_value;
        SymbolTable.getInstance().exitScope();
        ContextManager.switchContext(new GlobalContext());
        return return_value;
    }


}
