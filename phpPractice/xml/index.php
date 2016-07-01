<?php

include "xml.php";

interface Node {
	function getValue();
}


$xml = '<document title="hi" from="there" to="steve"><body id="1">dave</body><head id="3">Monkeys</head><body id="2">thomas</body></document>';

$obj1 = XMLParser::parse($xml, "Note");
$obj2 = XMLParser2::parse($xml);

var_dump($obj1);
var_dump($obj2);

?>
