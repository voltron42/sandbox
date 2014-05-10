var engine = (function(){
    var buildView = function(view){
        var viewFns = {
            show:function(){
                view.style.display = 'block';
            },
            hide:function(){
                view.style.display = 'none';
            },
            setOptions:function(opts){
                var opt;
                if(typeof opts == 'object'){
                    for(var x in opts){
                        opt = document.createElement('option');
                        opt.text = x;
                        opt.value = opts[x];
                        view.menu.add(opt,null);
                    }
                }
            },
            clearOptions:function(){
                var menu = view.menu;
                var opts = menu.options;
                while(opts.length>0){
                    menu.remove(0);
                }
            }
        };
        return {
            setOptions:function(opts){
                viewFns.clearOptions();
                viewFns.hide();
                viewFns.setOptions(opts);
                if(view.menu.options.length>0){
                    viewFns.show();
                }
            },
            getSelection:function(){
                if(view.menu.selectedIndex>=0){
                    var selection = view.menu.options[view.menu.selectedIndex];
                    return {
                        value:selection.value,
                        text:selection.text
                    };
                }
            },
            clear:function(){
                viewFns.clearOptions();
                viewFns.hide();
            },
            log:function(statement){
                statement = statement || "";
                view.console.value += statement + '\n';
                view.console.scrollTop = view.console.scrollHeight;
            }
        };
    };
    var buildModel = function(model){
        return {
            state:Collections.deepCopy(model.state),
            game:Collections.deepCopy(model.game.steps),
            init:model.game.init
        };
    };
    var build = function(view,model){
        var ui = buildView(view),
        gamedata = buildModel(model);
        var next = gamedata.init,
        loop, control;
        loop = function(obj){
            next = undefined;
            ui.clear();
            if(obj){
                [
                    'story','action','chances','choices','go'
                ].forEach(function(item){
                    if(obj[item]){
                        return control[item](obj[item]);
                    }
                });
            }
        };
        control = {
            story:function(obj){
                Collections.forEach(obj,function(item){
                    ui.log(item);
                });
                ui.log();
            },
            action:function(obj){
                var curr = {};
                for(var x in data.state){
                    curr[x] = data.state[x];
                }
                Collections.forEach(obj,function(item){
                    try{
                        with(data.state){
                            eval(item);
                        }
                    }catch(e){
                        console.log('cannot eval: '+item);
                    }
                });
                for(var x in curr){
                    if(curr[x] != data.state[x]){
                        var result = data.state[x]-curr[x];
                        if(result!=0){
                            ui.log("Your "+x+" has "+(result>0?"in":"de")+"creased by "+Math.abs(result)+".");
                        }
                    }
                }
            },
            chances:function(obj){
                var count = 0;
                if(obj instanceof Array){
                    count = obj.length
                }else{
                    for(var x in obj){
                        if(obj[x] instanceof Array){
                            count+=(x*obj[x].length)
                        }else{
                            count+=(x/1);
                        }
                    }
                }
                var seed = Math.floor(Math.random()*count);
                if(obj instanceof Array){
                    var result = obj[seed];
                    loop(result);
                }else{
                    for(var x in obj){
                        if(obj[x] instanceof Array){
                            if(seed<obj[x].length*x){
                                var temp = Collections.shallowCopy(obj);
                                loop(obj[x][Math.floor(seed/x)]);
                                break;
                            }else{
                                seed-=obj[x].length*x;
                            }
                        }else{
                            if(seed<x){
                                loop(obj[x]);
                                break;
                            }else{
                                seed-=x;
                            }
                        }
                    }
                }
            },
            choices:function(obj){
                ui.setOptions(obj);
                return true;
            },
            go:function(obj){
                next = obj;
                return true;
            }
        };
        var menuOption;
        var go = function(){
            if(menuOption){
                ui.log('"'+menuOption+'"');
                ui.log();
                menuOption = undefined;
            }
            loop(gamedata.game[next]);
        };
        go();
        return {
            pick:function(){
                var selection = ui.getSelection();
                if(selection){
                    var value = selection.value;
                    if(value.indexOf(",")>-1){
                        value = value.split(',');
                        vlaue = value[Math.floor(Math.random()*value.length)]
                    }
                    next = value;
                    menuOption = selection.text;
                }
            },
            go:go
        };
    };
    var domain;
    return {
        init:function(){
            var view = {
                console:document.getElementById('console'),
                style:document.getElementById('menuDiv').style,
                menu:document.getElementById('menu')
            };
            domain = build(view,data);
            domain.go();
        },
        pick:function(){
            domain.pick();
        },
        go:function(){
            domain.go();
        }
    };
})();