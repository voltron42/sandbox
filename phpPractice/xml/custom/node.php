<?php

class Name {
  private $space;
  private $local;

  function __construct(string $space, string $local) {
    $this->space = $space;
    $this->local = $local;
  }

  function getSpace() {
    return $this->space;
  }

  function getLocal() {
    return $this->Local;
  }

  function __toString() {
    return $this->space.":".$this->local;
  }
}

class Attribute {
  private $name;
  private $value;

  function __construct(Name $name, string $value) {
    $this->name = $name;
    $this->value = $value;
  }

	function getName() {
		return $this->name;
	}

	function getValue() {
		return $this->value;
	}
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

	function __construct(Name $name) {
		$this->name = $name;
		$this->attributes = array();
		$this->children = array();
	}

	function addChild(Node $node) {
		array_push($this->children, $node);
	}

	function setAttr(Attribute $attr) {
		$this->attributes[$attr->getName()] = $attr;
	}

	function unsetAttr(string $name) {
		unset($this->attributes[$attr->getName()]);
	}

  function getAttributes()

	function getValue() {
		return array(
			"name"=>$this->name,
			"attrs"=>$this->attributes,
			"children"=>$this->children
		);
	}
}


?>
