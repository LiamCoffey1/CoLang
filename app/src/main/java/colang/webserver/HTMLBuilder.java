package colang.webserver;

public class HTMLBuilder {
    StringBuilder stringBuilder;
    private static final HTMLBuilder instance = new HTMLBuilder();

    private HTMLBuilder() {
        stringBuilder = new StringBuilder();
    }

    public static HTMLBuilder getInstance() {
        return instance;
    }

    public void write(String html) {
        stringBuilder.append(html);
    }
    
    public String getAndDispose() {
        String html = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        return html;
    }

}
