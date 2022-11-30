package colang;

import java.util.HashMap;

public class SymbolTable {
    public static HashMap<String, Object> variable_map = new HashMap<String, Object>();
    public static HashMap<String, Object> getSymbolTable() {
        return variable_map;
    }
}
