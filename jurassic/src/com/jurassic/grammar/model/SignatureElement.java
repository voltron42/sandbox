package com.jurassic.grammar.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SignatureElement {
	private String name;
	private TypeDefinition type;
	private ValueWrapper value;
	
	public SignatureElement(String name, TypeDefinition type, ValueWrapper value) {
		super();
		this.name = name;
		this.setType(type);
		this.value = value;
	}
	
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="of")
	public TypeDefinition getType() {
		return type;
	}

	public void setType(TypeDefinition type) {
		this.type = type;
	}

	@XmlElement(name="default")
	public ValueWrapper getValue() {
		return value;
	}
	
	public void setValue(ValueWrapper value) {
		this.value = value;
	}
}
