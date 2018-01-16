package reserializer.metamodel;

import javax.xml.bind.annotation.XmlAttribute;

public class SchemaXmlElement implements SchemaXmlPrimitiveSpec, SchemaXmlObjectSpec {

    @XmlAttribute()
    public String label;

    @XmlAttribute(name="show-if-null")
    public boolean showIfNull;
}
