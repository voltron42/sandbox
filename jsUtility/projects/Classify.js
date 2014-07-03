Classify = function(){
	var fnTest = /xyz/.test(function(){xyz;}) ? /\b_super\b/ : /.*/;
	var objectBuilder = function(baseClass){
		var out = function(args){
			return baseClass.apply(this,args);
		};
		out.prototype = baseClass.prototype;
		return out;
	};
	return function (root,namespace){
		var ref = root||window;
		namespace = namespace || "";
		var names = namespace.split("/.");
		names.forEach(function(name){
			if(name.length>0){
				ref[name] = ref[name] || {};
				ref = ref[name];
			}
		});
		var baseFunction = function(params){
			var className = params.className;
			var superClass = params.superClass;
			var constructor = params.constructor;
			var argumentConverter = params.argumentConverter;
			var statics = params.statics;
			if(!superClass){
				ref[className] = constructor;
				ref[className].name = className;
			}else{
				argumentConverter = argumentConverter || function(args){return args;};
				var _superConstructor = objectBuilder(superClass);
				ref[className] = function(){
					var _this, _super;
					_super = new _superConstructor(argumentConverter(arguments));
					var _thisConstructor = objectBuilder(constructor);
					_thisConstructor.prototype = _super;
					_this = new _thisConstructor(arguments);
					for(var name in _this){
						if(typeof _this[name] == "function" && typeof _super[name] == "function" && !fnTest.test(_this[name])){
							_super[name] = _this[name];
						}else if(!(name in _super) || typeof _super[name] != "function"){
							this[name] = _this[name];
						}
					}
					for(var name in _super){
						if(typeof _this[name] == "function" && typeof _super[name] == "function" && fnTest.test(_this[name])){
							this[name] = (function(name, fn){
								return function() {
									var tmp = this._super;
									// Add a new ._super() method that is the same method
									// but on the super-class
									this._super = _super[name];
									// The method only need to be bound temporarily, so we
									// remove it when we're done executing
									var ret = fn.apply(this, arguments);		
									this._super = tmp;
									return ret;
								};
							})(name, _this[name])
						}else if(!(name in _this) || typeof _this[name] == "function"){
							this[name] = _super[name];
						}
					}
				};
				ref[className].name = className;
				ref[className].prototype = new _superConstructor();//instanceof recognizes superclass
			}
			if(statics){
				for(var x in statics){
					if(x in ref[className]){
						throw "Cannot add static member " + x + " to class " + className + ".";
					}else{
						ref[className][x] = statics[x];
					}
				}
			}
		}
		return baseFunction;
	};
}()
