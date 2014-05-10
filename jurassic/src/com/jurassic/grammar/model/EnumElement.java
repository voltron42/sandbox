package com.jurassic.grammar.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class EnumElement {
	private String name;
	private List<ValueWrapper> params;
	
	public EnumElement(String name, List<ValueWrapper> params) {
		this.name = name;
		this.params = params;
	}

	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="with")
	public List<ValueWrapper> getParams() {
		return params;
	}

	public void setParams(List<ValueWrapper> params) {
		this.params = params;
	}
}