(function(){
    var head = document.getElementsByTagName('head')[0];
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = 'lazy.js';
    script.onload = function(){
        console.log("after load");
    };
    head.appendChild(script);
    console.log("after append");
})();

function go(){
    console.log("gone");
};