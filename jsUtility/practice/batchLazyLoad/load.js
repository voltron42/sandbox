LazyLoader = (function(){
    var head = document.getElementsByTagName('head')[0];
    var importFile = function(src,fn){
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = src;
        script.onload = fn;
        head.appendChild(script);
        console.log(src + " after append");
    };
    var importFiles = function(srcs,fn){
        var toggle = 0;
        var semaphore = false;
        var cb = function(){
            --toggle;
            if(toggle<=0){
                semaphore = true;
                if(typeof fn == 'function'){
                    fn.apply(arguments);
                }
            }
        };
        srcs.forEach(function(src){
            toggle++;
            importFile(src,cb);
        });
    };
    return {
        importFiles:importFiles
    }
})();
LazyLoader.importFiles([
    'lazy.js','lazy1.js','lazy2.js','lazy3.js','lazy4.js'
],function(){
    console.log("this callback should be after all files");
});
function go(){
    console.log("gone");
};