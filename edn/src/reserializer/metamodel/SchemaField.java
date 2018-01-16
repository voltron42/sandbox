package reserializer.metamodel;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class SchemaField {

    @XmlAttribute()
    public String name;

    @XmlAttribute(name="allow-null")
    public boolean allowNull;

    @XmlElements({
            @XmlElement(type=SchemaMap.class, name="map"),
            @XmlElement(type=SchemaObject.class, name="object"),
            @XmlElement(type=SchemaArray.class, name="array"),
            @XmlElement(type=SchemaString.class, name="string"),
            @XmlElement(type=SchemaNumber.class, name="number"),
            @XmlElement(type=SchemaBoolean.class, name="boolean")
    })
    public SchemaFieldType fieldType;
}
