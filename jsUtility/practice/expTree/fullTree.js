(function(){
    var logger = document.getElementsByTagName('body')[0];
    var logLine = function(){
        logger.appendChild(document.createElement('hr'));
    }
    var log = function(statement){
        if(statement){
            logger.appendChild(document.createTextNode(statement));
        }
        logger.appendChild(document.createElement('br'));
    };
    var Tree = (function(){
        var Node;
        var evalItem = function(context,evalFn){
            return function(item){
                if(typeof item == 'string'){
                    return evalFn({
                        expression:item,
                        context:context
                    });
                }else if(item instanceof Node){
                    return item.eval(context,evalFn);
                }else{
                    return "";
                };
            };
        };
        var evalList = function(list,context,evalFn){
            return list.map(evalItem(context,evalFn)).join("")
        };
        Node = (function(){
            var open = "{[{";
            var close = "}]}";
            var split = "::";
            var constructor = function(base,children){
                this.eval = function(context,evalFn){
                    var evalBase = evalitem(context,evalFn)(base);
                    if(typeof evalBase == 'object'){
                        if(evalBase instanceof Array){
                            return evalBase.map(function(baseItem,index,base){
                                return evalList(children,{
                                    context:baseItem,
                                    index:index,
                                    collection:base,
                                    parent:context
                                },evalFn);
                            }).join("");
                        }else{
                            var outList = [];
                            for(var x in evalBase){
                                outList.push(evalList(children,{
                                    context:evalBase[x],
                                    index:x,
                                    collection:evalBase,
                                    parent:context
                                },evalFn));
                            }
                            return outList.join("");
                        }
                    }else{
                        if(evalBase){
                            return evalList(children,context,evalFn);
                        }else{
                            return "";
                        }
                    }
                };
            };
            constructor.buildFrom = function(exp){
                var list = [];
                var str;
                while(exp.length>0){
                    str = exp.join("");
                    var start = str.indexOf(open);
                    var end = str.indexOf(close);
                    if(start>=0&&(start<end||end<0)){
                        if(start>0){
                            list.push(exp.splice(0,start).join(""));
                            exp.splice(0,3);
                            str = exp.join("");
                        }
                        var step = str.indexOf(split);
                        var base;
                        if(step>0){
                            base = exp.splice(0,step).join("");
                            exp.splice(0,2);
                            str = exp.join("");
                        }else{
                            base = "";
                        }
                        var children = constructor.buildFrom(exp);
                        list.push(new constructor(base,children));
                    }else if(end>=0&&(start<0||start>end)){
                        if(end>0){
                            list.push(exp.splice(0,end).join(""));
                        }
                        exp.splice(0,3);
                        str = exp.join("");
                        break;
                    }else if(end<0&&start<0){
                        list.push(exp.join(""));
                        exp.splice(0,exp.length);
                    }else{
                        console.log("not sure what to do here");
                    }
                }
                return list;
            };
            constructor.name = "Node";
            return constructor;
        })();
        var buildRootsFrom = function(expression){
            return Node.buildFrom(expression);
        };
        return function(expression){
            var roots = buildRootsFrom(expression);
            this.evaluate = function(context,evalFn){
                return evalList(roots,context,evalFn);
            };
        }
    })();
    Tree.eval = function(expression,context,evalFn){
        return (new Tree(expression)).evaluate(context,evalFn);
    }
    window.test = function(){
        var evalFn = function(config){
        };
        var context = {};
        [
            
        ].forEach(function(testCase){
            log('testCase');
            log(testCase);
            log(Tree.eval(testCase,context,evalFn));
            log();
        });
    }
})();
