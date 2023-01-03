package colang.interperter.RunContext;

public class ContextManager {
    static RunContext currentContext = new GlobalContext();
    public static RunContext getContext() {
        return currentContext;
    }
    public static Context getContextType() {
        return currentContext.getContext();
    }
    public static RunContext switchContext(RunContext context) {
        currentContext = context;
        return currentContext;
    }
}
