memory = (function(){
    var control = (function(){
        var tableDiv, inputStyle, table, toggle, listing, buttonDefault, listLen;
        var calcRC = function(count){
            listLen = count;
            count *= 2;
            var count2 = count + 1;
            var out = {
                a:{
                    r:0,
                    c:0
                },
                b:{
                    r:0,
                    c:0
                }
            };
            var found = [];
            for(var sqrt = Math.floor(Math.sqrt(count2)); sqrt>=2; sqrt--){
                var quotient = count / sqrt;
                var quotient2 = count2 / sqrt;
                if(quotient == Math.floor(quotient)){
                    out.a.r = sqrt;
                    out.a.c = quotient;
                    found.push('a');
                }
                if(quotient2 == Math.floor(quotient2)){
                    out.b.r = sqrt;
                    out.b.c = quotient2;
                    found.push('b');
                }
                if(found.length>0){
                    break;
                }
            }
            var rows;
            var cols;
            var setdim = function(lbl){
                rows = Math.max(out[lbl].r,out[lbl].c);
                cols = Math.min(out[lbl].r,out[lbl].c);
            }
            if(found.length>=2){
                if(Math.abs(out.a.r-out.a.c) < Math.abs(out.b.r-out.b.c)){
                    setdim('a');
                }else{
                    setdim('b');
                }
            }else{
                setdim(found[0]);
            }
            var ret = {
                r:rows,
                c:cols,
                hasCenter:((rows*cols)%2==0)
            };
            return ret;
        };
        var setupTable = function(list,rows,cols,hasCenter){
            var table = document.createElement('table'),
            halfR = Math.floor(rows/2), 
            halfC = Math.floor(cols/2), 
            row, cell, button, r, c, rand;
            list = list.concat(list);
            for(r = 0; r<rows; r++){
                row = table.insertRow(-1);
                for(c = 0; c<cols; c++){
                    cell = row.insertCell(-1);
                    if(hasCenter||r!=halfR||c!=halfC){
                        rand = Math.floor(Math.random()*list.length);
                        button = document.createElement('button');
                        button.id = r+'_'+c;
                        button.setAttribute('onclick',"memory.look('"+button.id+"')");
                        button.innerHTML = buttonDefault;
                        cell.appendChild(button);
                        listing[button.id] = list.splice(rand,1)[0];
                    }
                }
            }
            return table;
        };
        return {
            buildTable:function(obj){
                buttonDefault = obj.cover.value;
                tableDiv = obj.table;
                tableDiv.innerHTML = "";
                inputStyle = obj.input.style;
                inputStyle.display = 'none'
                listing = {};
                var delim = obj.delim.value || ", ";
                var list = obj.list.value.split(delim);
                var max = 0;
                var setMax = function(item){
                    max = Math.max(max,item.length);
                };
                list.forEach(setMax);
                setMax(buttonDefault);
                var reshape = function(item){
                    var out = item;
                    while(out.length<max){
                        out = '_'+out+'_';
                    }
                    return out;
                };
                console.log(list);
                list = list.map(reshape);
                console.log(list);
                buttonDefault = reshape(buttonDefault);
                var rc = calcRC(list.length);
                tableDiv.appendChild(setupTable(list,rc.r,rc.c,rc.hasCenter));
                console.log("cover value: " + obj.cover.value);
                console.log(listing);
            },
            togglePick:function(button){
                if(toggle){
                    console.log("toggle id: " + toggle.id);
                    console.log("button id: " + button.id);
                    button.innerHTML = listing[button.id];
                    if(listing[toggle.id] === listing[button.id]){
                        button.disabled = true;
                        toggle.disabled = true;
                        listLen--;
                        if(listLen<=0){
                            alert("You Win!");
                            inputStyle.display = 'block';
                            tableDiv.innerHTML = "";
                        }else{
                            alert("Match");
                        }
                    }else{
                        alert("No Match!");
                        button.innerHTML = buttonDefault;
                        toggle.innerHTML = buttonDefault;
                        toggle.disabled = false;
                    }
                    toggle = undefined;
                }else{
                    toggle = button;
                    button.innerHTML = listing[button.id];
                    button.disabled = true;
                }
            }
        };
    })();
    return {
        go:function(tableId,inputId,listId,delimId,coverId){
            var args = arguments;
            var obj = {};
            [
                'table','input','list','delim','cover'
            ].forEach(function(item,index){                
                var elem = document.getElementById(args[index]);
                obj[item] = elem;
            });
            console.log(obj);
            control.buildTable(obj);
        },
        look:function(id){
            var button = document.getElementById(id);
            console.log('id');
            console.log(id);
            console.log('button');
            console.log(button);
            control.togglePick(button);
        }
    };
})();