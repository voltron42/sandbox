<?php

include "xml.php";

interface Node {
	function getValue();
}


$xml = '<document title="hi" from="there" to="steve">'
.'<body id="1">dave</body>'
.'okay'
.'<head id="3">Monkeys</head>'
.'not okay'
.'<body id="2">thomas</body>'
.'<new-node/>'
.'<node new-id="hi again"/>'
.'</document>';

echo "<PRE>";
echo "parser 1: <br/>\n";
$obj1 = XMLParser::parse($xml, "Note");
var_dump($obj1);
echo "<br/>\n";
echo "parser 2: <br/>\n";
$obj2 = XMLParser2::parse($xml);
var_dump($obj2);
echo "<br/>\n";
echo "parser 3: <br/>\n";
$obj3 = XMLParser3::parse($xml);
var_dump($obj3);
echo "<br/>\n";
echo "</PRE>";

?>
