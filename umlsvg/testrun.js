(function() {
	var require("fs");
	var umlsvg = require("./builder/umlsvg.js");
	var xml = fs.readFileSync("./gameplandomain.xml","utf-8");
	
	console.log(umlsvg(xml));
})()