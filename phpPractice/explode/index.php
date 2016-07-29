<?php

include "splode.php";

$map = array(
	"a" => 1,
	"b" => 2,
	"i-e" => 3
);

extract($map);

var_dump($GLOBALS);

?>
