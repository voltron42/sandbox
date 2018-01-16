package reserializer.metamodel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class SchemaArray implements SchemaFieldType, SchemaRootType {

    @XmlElements({
            @XmlElement(type=SchemaMap.class, name="map"),
            @XmlElement(type=SchemaObject.class, name="object"),
            @XmlElement(type=SchemaString.class, name="string"),
            @XmlElement(type=SchemaNumber.class, name="number"),
            @XmlElement(type=SchemaBoolean.class, name="boolean")
    })
    public SchemaArrayElementType elementType;

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
