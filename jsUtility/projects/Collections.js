var Collections = (function(){
    var looper = function(obj,callback){
        if(typeof obj == 'object'){
            if(obj instanceof Array){
                obj.forEach(function(item,index,array){
                    var result = callback(item,index,array)
                    if(result){
                        return true;
                    }
                });
            }else{
                for(var x in obj){
                    var result = callback(obj[x],x,obj);
                    if(result){
                        break;
                    }
                }
            }
        }else{
            callback(obj,0,[obj]);
        }
    };
    var map = function(obj,callback){
        if(typeof obj == 'object'){
            var out = new obj.constructor();
            looper(obj,function(item,index,collection){
                var result = callback(item,index,collection);
                out[index] = result;
            });
            return out;
        }else{
            return callback(obj,0,[obj]);
        }
    };
    var deepCopy = function(obj){
        if(typeof obj == 'object'){
            return map(obj,function(item){
                return deepCopy(item);
            });
        }else{
            return obj;
        }
    }
    return {
        forEach:looper,
        map:map,
        deepCopy:deepCopy,
        shallowCopy:function(obj){
            return map(obj,function(item){
                return item;
            });
        },
        filter:function(obj,callback){
            if(typeof obj == 'object'){
                var out = new obj.constructor();
                var set = (obj instanceof Array)?function(obj,item){
                    obj.push(item);
                }:function(obj,item,index){
                    obj[index] = item;
                };
                looper(obj,function(item,index,collection){
                    var result = callback(item,index,collection);
                    if(result){
                        set(out,item,index);
                    }
                });
                return out;
            }else{
                if(callback(obj,0,[obj])){
                    return obj;
                }
            }
        }
    };
})();