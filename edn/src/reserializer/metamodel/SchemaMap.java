package reserializer.metamodel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class SchemaMap implements SchemaFieldType, SchemaRootType, SchemaArrayElementType {

    @XmlElement(name="key-type")
    public SchemaMapKey key;

    @XmlElement(name="value-type")
    public SchemaMapValue value;

    @XmlElements({
            @XmlElement(type=SchemaXmlWrap.class, name="xml-wrap"),
            @XmlElement(type=SchemaXmlElement.class, name="xml-element"),
            @XmlElement(type=SchemaXmlIgnore.class, name="xml-ignore")
    })
    public SchemaXmlObjectSpec xmlSpec;

    @XmlElements({
            @XmlElement(type=SchemaJsonElement.class, name="json"),
            @XmlElement(type=SchemaJsonIgnore.class, name="json-ignore")
    })
    public SchemaJsonSpec jsonSpec;

}
