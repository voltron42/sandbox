module.exports = (function() {
	var Primitive = function(type) {
	}
	Primitive.buildFromXml = function(xml) {
		return new Primitive(xml.attr.type);
	}
	var DataType = function(name) {
	}
	DataType.buildFromXml = function(xml) {
		return new DataType(xml.attr.name);
	}
	var types = {
		primitive:Primitive,
		datatype:DataType
	}
	var Variable = function(name, type) {
	}
	Variable.buildFromXml = function(xml) {
		var type = xml.children[0];
		return new Variable(xml.attr.name, types[type.name].buildFromXml(type));
	}
	var mapVariableList = function(child) {
		return Variable.buildFromXml(child.children[0]);
	}
	var MethodDeclaration = function(name, returnType, parameters) {
	}
	MethodDeclaration.buildFromXml = function(xml) {
		var params = xml.children.filter(function(child,index) {
			return index > 0;
		}).map(mapVariableList)
		var type = xml.children[0];
		return new MethodDeclaration(xml.attr.name, types[type.name].buildFromXml(type), params);
	}
	var Interface = function(name, extendsList, constants, methods) {
	}
	Interface.buildFromXml = function(xml) {
		
	}
	var Class = function(name, superClass, isAbstract, implementsList, fieldList, methodList, constructorList) {
	}
	Class.buildFromXml = function(xml) {
	}
	var Enum = function(name,values) {
	}
	Enum.buildFromXml = function(xml) {
		var name = xml.attr.name;
		var values = xml.children.filter(function(child) {
			return child.name == "enum-value";
		}).map(function(child) {
			return child.attr.value;
		})
		return new Enum(name,values);
	}
	var Node = function(className, cardinality) {
	}
	Node.buildFromXml = function(xml) {
		return new Node(xml.attr.className, xml.attr.cardinality);
	}
	var Relationship = function(name, from, to) {
	}
	Relationship.buildFromXml = function(xml) {
		var relationshipInstance = {};
		var parameters = xml.children.map(function(child) {
			return Node.buildFromXml(child.children[0]);
		})
		parameters.unshift(xml.attr.name);
		Relationship.apply(relationshipInstance,parameters);
		return relationshipInstance;
	}
	var constructs = {
		interface:Interface,
		class:Class,
		enum:Enum,
		relationship:Relationship
	}
	var Domain = function(interfaces, classes, enums, relationships) {
		this.diagram = function() {
		}
	}
	Domain.buildFromXml = function(xml) {
		var constructMap = {};
		xml.children.forEach(function(child) {
			var type = child.name;
			constructMap[type] = constructMap[type] || [];
			constructMap[type].push(child);
		});
		Object.keys(constructMap).forEach(function(key) {
			var objectMap = {};
			constructMap[key].forEach(function(construct) {
				var object = constructs[key].buildFromXml();
				var name = object.getName();
				if (objectMap[name]) {
					throw new Error("Cannot have two " + key + " nodes with the same name attribute.");
				}
				objectMap[name] = object;
			})
			constructMap[key] = objectMap;
		})
	}
	var Sequence = function() {
		this.diagram = function() {
		}
	}
	Sequence.buildFromXml = function(xml) {
		return new Sequence();
	}
	var umlBuilder = {
		domain:Domain,
		sequence:Sequence
	}
	return {
		buildFromXml:function(xml) {
			var child = xml.children[0];
			return umlBuilder[child.name].buildFromXml(xml);
		}
	};
})()