FileManager = (function(){
    var fileList = {};
    var head = document.getElementsByTagName('head')[0];
    var importSingle = function(uri,callback){
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = uri;
        script.onload = callback;
        head.appendChild(script);
        fileList[uri] = true;
    };
    var importFiles = function(){
        var args = [];
        for(var x = 0; x<arguments.length; x++){
            args.push(arguments[x]);
        }
        var fn = args.pop();
        if(args.length==1&&args[0] instanceof Array){
            args = args[0];
        }
        args = args.filter(function(arg){
            return !fileList[arg];
        });
        if(args.length>0){
            var recurse = function(){
                importSingle(args.shift(),(args.length<=0)?fn:recurse);
            };
            recurse();
        }else{
            fn();
        }
    };
    return {
        importFiles:importFiles
    };
})();
