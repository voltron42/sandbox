package com.jurassic.grammar.model;

import javax.xml.bind.annotation.XmlAttribute;

public class SimpleValue implements ValueDefinition {
	private String value;

	public SimpleValue(String value) {
		this.value = value;
	}

	@XmlAttribute(name="value")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
