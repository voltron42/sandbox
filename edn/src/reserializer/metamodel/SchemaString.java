package reserializer.metamodel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class SchemaString implements SchemaFieldType, SchemaMapKeyType, SchemaArrayElementType {

    @XmlElements({
            @XmlElement(type=SchemaXmlElement.class, name="xml-element"),
            @XmlElement(type=SchemaXmlAttribute.class, name="xml-attribute"),
            @XmlElement(type=SchemaXmlIgnore.class, name="xml-ignore")
    })
    public SchemaXmlPrimitiveSpec xmlSpec;

    @XmlElements({
            @XmlElement(type=SchemaJsonElement.class, name="json"),
            @XmlElement(type=SchemaJsonIgnore.class, name="json-ignore")
    })
    public SchemaJsonSpec jsonSpec;
}
