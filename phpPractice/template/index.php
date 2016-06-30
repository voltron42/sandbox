<?php

include "tpl.php";

$tpl = 'Hi, my name is {{name}}. I am {{age}} years old.';

$data = array(
	array(
		"name" => "Steve",
		"age" => 7
	),
	array(
		"name" => "Roger",
		"age" => 15
	)
);

foreach($data as $person) {
	echo TemplateEngine::apply($tpl, $person)."\n";
}

?>
