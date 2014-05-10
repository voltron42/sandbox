package com.jurassic.grammar.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Tag {
	private String glossary;

	public Tag(String glossary) {
		this.glossary = glossary;
	}

	@XmlAttribute
	public String getGlossary() {
		return glossary;
	}

	public void setGlossary(String glossary) {
		this.glossary = glossary;
	}
}