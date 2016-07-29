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

class XMLParser3 {
  static function parse(string $raw) {
    $xml = new XMLReader();
    $xml->xml($raw);
    $assoc = self::xml2assoc($xml, "root");
    $xml->close();
    return $assoc[0];
  }

  private static function xml2assoc($xml, $name) {
      print "<ul>\n";

      $tree = null;
      print("I'm inside " . $name . "<br>\n");

      while($xml->read()) {
          if($xml->nodeType == XMLReader::END_ELEMENT) {
              print "</ul>\n";
              return $tree;
          } else if($xml->nodeType == XMLReader::ELEMENT) {
              $node = array();

              print("Adding " . $xml->name ."<br>\n");
              $node['tag'] = self::camelCase($xml->name);

              if($xml->hasAttributes) {
                  $attributes = array();
                  while($xml->moveToNextAttribute()) {
                      print("Adding attr " . $xml->name ." = " . $xml->value . "<br>\n");
                      $attributes[self::camelCase($xml->name)] = $xml->value;
                  }
                  $node['attr'] = $attributes;
              }

              if(!$xml->isEmptyElement) {
                  $childs = self::xml2assoc($xml, $node['tag']);
                  $node['childs'] = $childs;
              }

              print($node['tag'] . " added <br>\n");
              $tree[] = $node;
          } else if($xml->nodeType == XMLReader::TEXT) {
              $text = $xml->value;
              $tree[] = $text;
              print "text added = " . $text . "<br>\n";
          }
      }

      print "returning " . count($tree) . " childs<br>\n";
      print "</ul>\n";

      return $tree;
  }

    private static function camelCase(String $name) {
      $name = ucwords($name,"-");
      $name = lcfirst($name);
      $labels = explode('-',$name);
      return implode('',$labels);
    }
}



?>
