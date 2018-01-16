package reserializer.metamodel;

import javax.xml.bind.annotation.XmlAttribute;

public class SchemaJsonElement implements SchemaJsonSpec {

    @XmlAttribute()
    public String label;

    @XmlAttribute(name="show-if-null")
    public boolean showIfNull;
}
