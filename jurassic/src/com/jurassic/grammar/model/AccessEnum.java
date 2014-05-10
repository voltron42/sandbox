package com.jurassic.grammar.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum AccessEnum {
	@XmlEnumValue(value="thing") THING, 
	@XmlEnumValue(value="type") TYPE,
	;
}
