module.exports = (function(){
	var fnTest = /xyz/.test(function(){xyz;}) ? /\b_super\b/ : /.*/;
	var objectBuilder = function(baseClass, superClassInstance){
		var out = function(args){
			return baseClass.apply(this,args);
		};
		out.prototype = superClassInstance || baseClass.prototype;
		return out;
	};
	var instantiate = function(cls) {
		return function(args) {
			var obj = {};
			cls.apply(obj,args);
			return obj;
		}
	}
	var appendStatics = function(cls, statics) {
		if(statics){
			var existingMembers = Object.keys(statics).filter(function(key) {
				return typeof cls[key] != 'undefined';
			}).map(function(key) {
				return key;
			});
			if (existingMembers.length > 0) {
				throw new Error("Cannot add static members " + existingMembers + " to class " + className + ".");
			}
			Object.keys(statics).forEach(function(key) {
				cls[key] = statics[key];
			});
		}
	}
	var defaultArgumentConverter = function() {
		return arguments;
	}
	var inherit = function(me,_this,_super) {
		return function(name) {
			if(typeof _this[name] == "function" && typeof _super[name] == "function" && !fnTest.test(_this[name])){
				_super[name] = _this[name];
			}else if(!(name in _super) || typeof _super[name] != "function"){
				me[name] = _this[name];
			}
		}
	}
	var override = function(me,_this,_super) {
		return function(name) {
			if(typeof _this[name] == "function" && typeof _super[name] == "function" && fnTest.test(_this[name])){
				me[name] = (function(name, fn){
					return function() {
						var tmp = me._super;
						// Add a new ._super() method that is the same method
						// but on the super-class
						me._super = _super[name];
						// The method only need to be bound temporarily, so we
						// remove it when we're done executing
						var ret = fn.apply(me, arguments);	
						me._super = tmp;
						return ret;
					};
				})(name, _this[name])
			}else if(!(name in _this) || typeof _this[name] == "function"){
				me[name] = _super[name];
			}
		}
	}
	var extend = function(instantiateSuper, constructor, argumentConverter) {
		return function() {
			var _this, _super, me = this;
			_super = instantiateSuper(argumentConverter.apply(null,arguments));
			var _thisConstructor = objectBuilder(constructor, _super);
			_this = new _thisConstructor(arguments);
			Object.keys(_this).forEach(inherit(me,_this,_super));
			Object.keys(_super).forEach(override(me,_this,_super));
		}
	}
	var classFactory = function(pkg) {
		return function(className,constructor,statics,superClass,argumentConverter) {
			if (typeof className != 'string') {
				throw new Error("Class name must be a string!");
			}
			if (typeof constructor != 'function') {
				throw new Error("Constructor must be a function!");
			}
			if (typeof statics == 'function') {
				argumentConverter = superClass;
				superClass = statics;
				statics = (function(){})();
			}
			if (!superClass) {
				pkg[className] = constructor;
				pkg[className].name = className;
			} else {
				argumentConverter = argumentConverter || defaultArgumentConverter;
				var instantiateSuper = instantiate(superClass);
				pkg[className] = extend(instantiateSuper, constructor, argumentConverter);
				pkg[className].name = className;
				pkg[className].prototype = instantiateSuper();//instanceof recognizes superclass
			}
			appendStatics(pkg[className], statics);
		}
	}
	return function (root,namespace){
		var pkg = root||global||window;
		namespace = namespace || "";
		var names = namespace.split("/.");
		names.forEach(function(name){
			if(name.length>0){
				pkg[name] = pkg[name] || {};
				pkg = pkg[name];
			}
		});
		return classFactory(pkg);
	};
})()