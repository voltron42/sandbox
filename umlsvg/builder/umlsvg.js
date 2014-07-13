module.exports = (function() {
	var XmlDoc = require("xmldoc").XmlDocument;
	var pk = require("pdfkit");
	var uml = require("umlObjects.js");
	
	return function(xmlText) {
		var xml = new XmlDoc(xmlText);
		var model = uml.buildFromXml(xml);
		return model.diagram();
	}
})()