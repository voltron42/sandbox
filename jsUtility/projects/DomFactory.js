DomFactory = (function(){
    var build = function(obj){
        if(obj.tag){
            var out = document.createElement(obj.tag);
            if(obj.attrs){
                for(var x in obj.attrs){
                    if(typeof obj.attrs[x] == 'string'){
                        out.setAttribute(x,obj.attrs[x]);
                    }else{
                        out[x] = obj.attrs[x];
                    }
                }
            }
            if(obj.children){
                append(out,obj.children);
            }
        }
    };
    var append = function(dom,arr){
        arr.forEach(function(elem){
            var node;
            if(typeof elem == 'object'){
                node = build(elem);
            }else{
                node = document.createTextNode(elem);
            }
            dom.appendChild(node);
        });
    };
    return {
        buildFromObject:build,
        appendAllToExisting:append
    };
})();
