(function() {
    var XmlDoc = require("xmldoc").XmlDocument;
    var node = function(type, attrs, content) {
        var out = new XmlDoc("<" + type + "/>");
        Object.keys(attrs).forEach(function(key) {
            out.attr[key] = attrs[key];
        })
        if (children instanceof Array) {
            children.forEach(function(child) {
                out.children.push(child);
            })
        } else if (typeof children == 'string') {
            out.val = children;
        }
        return out;
    }
    var charList = [{
        start: 33,
        end: 126
    },"192",{
        start: "1FA",
        end: "1FF"
    },{
        start: "218",
        end: "21B"
    },{
        start: "2C6",
        end: ""
    },{
        start: "",
        end: ""
    }]
    var spans = [];
    var bodyContent = spans.map(function(span) {
        return node("span",{
            style:'font-family: "Lucida Console", Monaco, monospace; font-size: 14'
        },span);
    })
    var html = node("html", {}, [
        node("head",{},[
            node("title",{},"Measurement")
        ]),
        node("body",{},bodyContent)
    ])
    fs.writeFileSync("./index.html",html.toString());
})()