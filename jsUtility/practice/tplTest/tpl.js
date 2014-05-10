(function(){
    var logger;
    var logLine = function(){
        logger.appendChild(document.createElement('hr'));
    }
    var log = function(statement){
        if(statement){
            logger.appendChild(document.createTextNode(statement));
        }
        logger.appendChild(document.createElement('br'));
    };
    var printObject = function(print,obj,key){
        if(typeof obj == 'object'){
            if(obj instanceof Array){
                obj.forEach(function(item,index){
                    printObject(print,item,key+"."+index);
                });
            }else{
                for(var x in obj){
                    printObject(print,obj[x],key+"."+x);
                }
            }
        }else{
            print(key + " = " + obj);
        }
    };
    var readTpl = function(tpl,data){
        var result = tpl;
        var patterns = {
            "[{][^{}[\\]]+[}]":function(match,tplStr){
                var out = tplStr;
                var ref = data;
                if(match != "{.}"){
                    var keys = match.replace("{","").replace("}","").split(".");
                    keys.forEach(function(key){
                        if(key.length>0&&ref[key]){
                            ref = ref[key];
                        }
                    });
                }
                if(ref){
                    out = out.split(match).join(ref);
                }
                return out;
            },
            "[{][[][^{}]+[\\]][}]":function(match,tplStr){
                var out = tplStr;
                var expression = match.replace("{[","").replace("]}","");
                var value = (function(exp,dat){
                    try{
                        with(dat){
                            return eval(exp);
                        }
                    }catch(e){
                        console.error(e);
                        return match;
                    }
                })(expression,data);
                if(value!=match){
                    out = out.split(match).join(value);
                }
                return out;
            }
        };
        var analyze = function(tplStr,pattern,callback){
            var regex = new RegExp(pattern,"g");
            var array = tpl.match(regex);
            if(array){
                array.forEach(function(match){
                    tplStr = callback(match,tplStr);
                });
            }
            return tplStr;
        }
        for(var x in patterns){
            result = analyze(result,x,patterns[x]);
        }
        return result;
    };
    var withTest = function(data){
        [
            "a + b"
        ].forEach(function(expression){
            with(data){
                log(expression);
                log(eval(expression));
            }
            log();
        });
    };
    var depthTest = function(){
        [
            "({index} - {item})",
            "{[{b::({index} - {item})}]}",
            "With {[{b::({index} - {item})}]} And  {[{c::({[array[index]]} == {item})}]} total = {total}",
            "Go {[{a::With {[{b::({index} - {item})}]} And  {[{c::({[array[index]]} == {item})}]} total = {total} }]} Gone"
        ].forEach(function(expression){
            var tag = {
                start:"{[{",
                end:"}]}",
                mark:"::"
            };
            var out = [];
            var mark = 0;
            
            
        });
    }
    test = function(){
        logger = document.getElementsByTagName('body')[0];
        var data = {
            a:3,
            b:6,
            hello:"So long",
            world:"sucker",
            name:"moron",
            message:{
                hi:"hello",
                there:"world"
            },
            list:[
                "uno",
                "dos",
                "tres"
            ],
            store:[{
                first:"Steve",
                last:"Rogers"
            },{
                first:"Tony",
                last:"Stark"
            },{
                first:"David",
                last:"Banner"
            },{
                first:"Sam",
                last:"Wilson"
            }],
            table:[[1,2,3],[4,5,6],[7,8,9]]
        };
        printObject(log,data,"data");
        withTest(data);
        [
            "{a}",
            "{{a}}",
            "a",
            "[a]",
            "{[a]}",
            "Hello World!",
            "{hello} {world}",
            "{message.hi} {message.there}",
            "Hello, my name is {name}!",
            "The sum of {a} and {b} is {[a + b]}",
            "Countdown: {list.2}, {list.1}, {list.0}",
            "Countdown: {[list[2]]}, {[list[1]]}, {[list[0]]}",
            "Countdown: {[list.reverse()]}",
            "Countdown: {[list.reverse().join(', ')]}"
        ].forEach(function(testCase){
            log("Test Case: "+testCase);
            var result = readTpl(testCase,data);
            log("result = " + result);
            log();
        });
    }
})()
