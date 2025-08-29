package colang.interperter.LangObject;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.events.Attribute;
public class DOMElement {
        public String tagName;
        public String textValue;
        public DOMElement parent;
        public List<Property> attributes = new ArrayList<Property>();
        public List<DOMElement> children = new ArrayList<DOMElement>();
}
