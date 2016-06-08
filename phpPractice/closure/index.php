<?php

echo "Hello World!";
echo "<br/>\n";

function lt($num) {
	echo "building lt < $num";
	echo "<br/>\n";
	return function($i) use ($num) {
		echo "testing $i < $num";
		echo "<br/>\n";
		return $i < $num;
	};
}

function filter($arr, $fn) {
	$out = array();
	foreach($arr as $item) {
		if($fn($item)) {
			$out[] = $item;
		}
	}
	return $out;
}

$data = array(1,2,3,4,5,6);

echo implode(",",$data);

echo "<br/>\n";

echo implode(",",filter($data,lt(4)));

echo "<br/>\n";

echo implode(",",filter($data,function($i){
	return $i % 2 == 0;
}));

echo "<br/>\n";

?>
