package colang.interperter.RunContext;

public class GlobalContext implements RunContext {

    @Override
    public Context getContext() {
        return Context.GLOBAL;
    }
}
