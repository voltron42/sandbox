<?php

include "xml.php";

interface Node {
	function getValue();
}

class TextNode implements Node {
	private $text;

	function __construct(string $text) {
		$this->text = $text;
	}

	function getValue() {
		return $this->text;
	}
}

class XmlNode implements Node {
	private $nodeName;
	private $attributes;
	private $children;

	function __construct(string $name) {
		$this->name = $name;
		$this->attributes = array();
		$this->children = array();
	}

	function addChild(Node $node) {
		array_push($this->children, $node);
	}

	function setAttr(string $name, string $value) {
		$this->attributes[$name] = $value;
	}

	function unsetAttr(string $name) {
		unset($this->attributes[$name]);
	}

	function getValue() {
		return array(
			"name"=>$this->name,
			"attrs"=>$this->attributes,
			"children"=>$this->children
		);
	}

}

$xml = '<document title="hi" from="there" to="steve"><body id="1">dave</body><head id="3">Monkeys</head><body id="2">thomas</body></document>';

$obj1 = XMLParser::parse($xml, "Note");
$obj2 = XMLParser2::parse($xml);

var_dump($obj1);
var_dump($obj2);

?>
