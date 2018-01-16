package reserializer.metamodel;

import javax.xml.bind.annotation.XmlAttribute;

public class SchemaObject implements SchemaFieldType, SchemaRootType, SchemaArrayElementType {

    @XmlAttribute()
    public String type;
}
