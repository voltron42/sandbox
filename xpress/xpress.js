var x = (function(){
    var primitives = {
        "string":true,
        "number":true,
        "boolean":true
    };
    var isPrimitive = function(obj){
        return typeof obj in primitives;
    };
    var valFN = function(library){
        var validate = function(expression, indicies, baseType){
            indicies = indicies || []
            if(isPrimitive(expression)) {
                console.log(expression + ":" + baseType + " [" + indicies + "]");
                if (baseType) {
                    if (typeof expression != baseType) {
                        throw "primitive value of type " + typeof expression + " does not match expected type " + baseType + ": " + indicies;
                    }
                }
                return true;
            } else {
                if (!(expression instanceof Array)) {
                    throw "Expressions can only contain primitive values or other expressions: " + indicies;
                }
                var exp = expression.concat();
                var name = exp.shift();
                if (typeof name != "string") {
                    throw "Expressions must be declared with a string literal in the first value: " + indicies;
                }
                var meta = library.filter(function(fn){
                    return fn.name == name;
                })[0];
                var params = meta.paramTypes.concat();
                var tail;
                if (meta.isVarArg) {
                    tail = params.pop();
                }
                if (exp.length < params.length) {
                    throw "Expression " + name + " expecting parameter(s) of the following type(s) " + params.slice(exp.length).join(", ") + ": " + indicies;
                }
                params.forEach(function(param, index){
                    console.log(param);
                    console.log(exp[index]);
                    validate(exp[index], indicies.concat(index), param);
                });
                if (tail) {
                    var count = params.length
                    exp.slice(count).forEach(function(exp, index){
                        validate(exp, indicies.concat(count + index), tail)
                    });
                }
                return true;
            }
        };
        return validate;
    };
    return function(library){
        var fn = valFN(library);
        return {
            validate:fn
        };
    };
})()