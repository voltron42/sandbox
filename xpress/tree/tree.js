test = function(){
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
    var mapTree = (function(expression,mark,obj){
        var check = false;
        while(mark<expression.length){
            mark = expression.indexOf("(",mark);
            obj["("] = mark;
            if(mark<0){
                break;
            }else{
                var point = {
                    "(":expression.indexOf("(",mark+1),
                    ")":expression.indexOf(")",mark+1)
                };
                if(point[")"]>mark){
                    if(point["("]<0||point[")"]<point["("]){
                    }else if(point["("]>0&&point[")"]>point["("]){
                    }else{
                        throw "incompatible parens";
                    }
                }else{
                    throw "incompatible parens";
                }
            }
        }
        return obj;
    });
    [
        "a * (b + c)",
        "(a + b) * c",
        "a * (b + c) * d",
        "(a + b) * (c - d)",
        "((a + b) * (c - d)) / e",
        "a + 2 * (b + 2 * (c + 2 * (d + 2)))"
    ].forEach(function(testCase){
        log('testCase');
        log(testCase);
        printObject(log,mapTree(testCase,0,{}),"mapTree");
        log();
    });
}
