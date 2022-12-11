package colang.logging;
public class Logger {
   public static void logOutput(Object to_log) {
        System.out.println("--- CoLang --- OUTPUT: " + to_log);
   }
   public static void logError(Object to_log) {
    System.err.println("--- CoLang --- ERROR: " + to_log);
    }
}
