<?php

include "enum.php";

class Weekday extends Enum {

	private static $raw = array(
		"Sunday" => 0,
		"Monday" => 1,
		"Tuesday" => 2,
		"Wednesday" => 3,
		"Thursday" => 4,
		"Friday" => 5,
		"Saturday" => 6
	);

	protected static function rawValues() {
		return self::$raw;
	}

	protected static function className() {
		return __CLASS__."";
	}

	protected static function build($arg) {
		return new Weekday($arg);
	}

	private $index;

	private function __construct($index) {
		$this->index = $index;
	}

	function getIndex() {
		return $this->index;
	}

	function __toString() {
		return $this->name()." -- ".$this->getIndex();
	}
}

$values = Weekday::values();
foreach ($values as $weekday) {
	echo $weekday."\n";
}
echo "\n";
echo Weekday::Monday()."\n\n";
echo Weekday::Friday()."\n\n";

echo Weekday::valueOf("Tuesday")."\n\n";

try {
	Weekday::June();
} catch (Exception $e) {
	echo $e->getMessage()."\n\n";
}


?>
