package com.jurassic.grammar.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum Changable {
	@XmlEnumValue(value="can") CAN, 
	@XmlEnumValue(value="cannot") CANNOT,
	;
}
