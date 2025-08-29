package colang.interperter.Operator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import colang.interperter.LangObject.LangObject;
import colang.interperter.Type.CLType;
import colang.logging.Logger;

public class BinaryOperations {

    private static BinaryOperations instance = null;
    private HashMap<String, List<BinaryOperation>> statement_nodes_map = new HashMap<String, List<BinaryOperation>>();

    Function<Object, Boolean> booleanConverter = (bool) -> Boolean.parseBoolean(bool.toString());
    Function<Object, String> stringConverter = (str) -> str.toString();
    Function<Object, Integer> numberConverter = (num) -> Integer.parseInt(num.toString());

    private BinaryOperations() {
        initializeCreationMap();
    }

    public static BinaryOperations getInstance() {
        if(instance == null) {
            instance = new BinaryOperations();
        }
        return instance;
    }

    void putInMap(String key, BinaryOperation... operations) {
        statement_nodes_map.put(key, Arrays.asList(operations));
    }

    private void initializeCreationMap() {
        putInMap("&&", new BinaryOperation<Boolean, Boolean>(CLType.BOOLEAN, (left, right) -> left && right, booleanConverter));
        putInMap("||", new BinaryOperation<Boolean, Boolean>(CLType.BOOLEAN, (left, right) -> left || right, booleanConverter));
        putInMap("+",
            new BinaryOperation<Integer, Integer>(CLType.NUMBER, (left, right) -> left + right, numberConverter),
            new BinaryOperation<String, String>(CLType.STRING, (left, right) -> {
                return left.concat(right);
            }
            , stringConverter)
        );
        
        putInMap("-", new BinaryOperation<Integer, Integer>(CLType.NUMBER, (left, right) -> left - right, numberConverter));
        putInMap("*", new BinaryOperation<Integer, Integer>(CLType.NUMBER, (left, right) -> left * right, numberConverter));
        putInMap("/", new BinaryOperation<Integer, Integer>(CLType.NUMBER, (left, right) -> left / right, numberConverter));
        putInMap("==",
            new BinaryOperation<Integer, Boolean>(CLType.NUMBER, (left, right) -> left == right, numberConverter),
            new BinaryOperation<String, Boolean>(CLType.STRING, (left, right) -> left.equals(right), stringConverter)
        );
        putInMap("!=",
            new BinaryOperation<Integer, Boolean>(CLType.NUMBER, (left, right) -> left != right, numberConverter),
            new BinaryOperation<String, Boolean>(CLType.STRING, (left, right) -> !left.equals(right), stringConverter)
        );
        putInMap(">",
            new BinaryOperation<Integer, Boolean>(CLType.NUMBER, (left, right) -> left > right, numberConverter),
            new BinaryOperation<String, Boolean>(CLType.STRING, (left, right) -> left.compareTo(right) > 0, stringConverter)
        );
        putInMap("<",
            new BinaryOperation<Integer, Boolean>(CLType.NUMBER, (left, right) -> left < right, numberConverter),
            new BinaryOperation<String, Boolean>(CLType.STRING, (left, right) -> left.compareTo(right) < 0, stringConverter)
        );
        putInMap(">=",
            new BinaryOperation<Integer, Boolean>(CLType.NUMBER, (left, right) -> left >= right, numberConverter),
            new BinaryOperation<String, Boolean>(CLType.STRING, (left, right) -> left.compareTo(right) > 0 || left.compareTo(right)  == 0, stringConverter)
        );
        putInMap("<=",
            new BinaryOperation<Integer, Boolean>(CLType.NUMBER, (left, right) -> left <= right, numberConverter),
            new BinaryOperation<String, Boolean>(CLType.STRING, (left, right) -> left.compareTo(right) < 0 || left.compareTo(right)  == 0, stringConverter)
        );
    }

    public BinaryOperation getOperation(String operator, LangObject left, LangObject right) {
        List<BinaryOperation> operations = statement_nodes_map.get(operator);
        if(operations == null) {
            return null;
        }
        for(BinaryOperation operation : operations) {
            if(operation.matchesType(left, right)) {
                return operation;
            }
        }
        Logger.logError("Type mismatch for operator " + operator + " " + left.type.name() + ", " + right.type.name());
        return null;
    }

    public class BinaryOperation<IN, OUT> {
        public CLType expected_type;
        public CLType return_type;
        BiFunction<IN, IN, OUT> operation;
        Function<Object, IN> converter;

        public  BinaryOperation(CLType expected_type, BiFunction<IN, IN, OUT> operation, Function<Object, IN> converter) {
            this.expected_type = expected_type;
            this.operation = operation;
            this.converter = converter;
        }

        public OUT operate(LangObject left, LangObject right) {
            return operation.apply(converter.apply(left.value), converter.apply(right.value));
        }

        public boolean matchesType(LangObject left, LangObject right) {
            return left.type == expected_type && right.type == expected_type;
        }
    }

}
