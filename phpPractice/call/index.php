<?php

include "splode.php";

class A {
	public static function __callStatic($name, $arguments) {
    return $name;
  }
}

$list = array();
foreach (array("a","b") as $letter) {
	foreach (array(1, 2) as $value) {
		$call = "fn_".$letter."_".$value;
		$list[] = A::$call();
	}
}

var_dump($list);

?>
