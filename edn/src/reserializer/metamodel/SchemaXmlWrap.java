package reserializer.metamodel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SchemaXmlWrap implements SchemaXmlObjectSpec {

    @XmlElement(name="xml-element")
    public SchemaXmlElement element;

    @XmlAttribute()
    public String label;

    @XmlAttribute(name="show-if-null")
    public boolean showIfNull;
}
