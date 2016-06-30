<?php

class XMLParser {
  static function parse(string $xml, string $className = "") {
    if ($className == "") {
      return simplexml_load_string($xml);
    } else {
      return simplexml_load_string($xml, $className);
    }
  }
}

class XMLParser2 {
  static function parse(string $xml) {
    return DOMDocument::loadXML($xml);
  }
}

?>
