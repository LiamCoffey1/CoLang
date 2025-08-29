package colang.interperter.LangObject;

import java.util.ArrayList;
import java.util.List;

public class DOM {
        public DOMElement root;
        public DOM() {
            root = new DOMElement();
            root.tagName = "root";
        }
}
