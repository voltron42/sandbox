package com.jurassic.grammar.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum UsageEnum {
	@XmlEnumValue(value="me") ME, 
	@XmlEnumValue(value="glossary") GLOSSARY, 
	@XmlEnumValue(value="family") FAMILY, 
	@XmlEnumValue(value="all") ALL,
	;
}
