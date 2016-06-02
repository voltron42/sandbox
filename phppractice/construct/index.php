<?php

echo "Hello World!";
echo "<br/>\n";

class Dog {
	
	private $name;
	private $age;
	
	public function __construct($name, $age) {
		$this->name = $name;
		$this->age = $age;
	}
	
	public function __toString() {
		return "My name is ".$this->name.". I am ".$this->age." years old.";
	}
}
	
function buildFactory($cls) {
	return $cls::__construct;
}

$factory = buildFactory(Dog);

$kennel = array(
	$factory("Petey", 12),
	$factory("Shado", 5)
);

echo implode("<br/>\n",$kennel);

echo "<br/>\n";

try {
	echo new Dog("sam",9);
} catch (Exception $e) {
	echo $e;
}

?>
