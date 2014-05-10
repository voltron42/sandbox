//TODO -- factor out private static functions into closure wrapper for constructor
//TODO -- factor out cross class private statics into domain function
FileManager.importFiles([
    "../util/FormatString.js",
    "../util/Classify.js",
    "../util/DomFactory.js",
    "cqDataSet.js"
],function(){
    window.Domain = (function(){
        var domain = {},
        html = document.getElementsByTagName('html')[0],
        classFactory = Classify(domain),
        item_toString = function(initStr,list,obj,loopTpl){
            var out = initStr;
            list.forEach(function(listItem){
                out += FormatString(loopTpl,{
                    name:listItem,
                    value:obj[listItem]
                });
            });
            return out;
        },
        launcher;
        classFactory({
            className:'Item',
            constructor:function(name,stats){
                var statNames = [];
                for(var x in stats){
                    statNames.push(x);
                }
                this.getName = function(){
                    return name;
                };
                this.getStatNames = function(){
                    return statNames;
                };
                this.getStat = function(statName){
                    return stats[statName];
                };
                this.toString = function(){
                    return item_toString(FormatString("Name: {name}\n",{
                        name:name
                    }),statNames,stats,"\tStat: {name}, Value: {value}\n");
                };
            }
        });
        classFactory({
            className:'Equipment',
            superClass:domain.Item,
            constructor:function(name,stats,type){
                this.getType = function(){
                    return type;
                };
                this.toString = function(){
                    return item_toString(FormatString("Name: {name}, Type: {type}\n",{
                        name:name
                    }),this.getStatNames(),stats,"\tStat: {name}, Value: {value}\n");
                };
            }
        });
        classFactory({
            className:'Skill',
            superClass:domain.Item,
            constructor:(function(){
                var adjust_loop = function(input,output,list,fn){
                    list.forEach(function(listItem){
                        output[listItem] = (input[listItem]||0) + fn(listItem);
                    });
                };
                return function(name,stats,costs){
                    var costNames = [];
                    for(var x in costs){
                        costNames.push(x);
                    };
                    this.getCostNames = function(){
                        return type;
                    };
                    this.getCost = function(costName){
                        return costs[costName];
                    };
                    this.adjust = function(target){
                        var out = {};
                        adjust_loop(target,out,this.getStatNames(),this.getStat);
                        adjust_loop(target,out,this.getCostNames(),this.getCost);
                        return out;
                    };
                    this.toString = function(){
                        return item_toString(this._super(),costNames,costs,"\tCost: {name}, Value: {value}\n");
                    };
                };
            })()
        });
        classFactory({
            className:'Level',
            constructor:(function(){
                var init_countTotal = function(counters){
                    var out = 0;
                    for(var x in counters){
                        out += counters[x];
                    }
                    return out;
                };
                return function(boss,stepCount,ratios){
                    var countTotal = init_countTotal(ratios);
                    var step = 0;
                    this.getStep = function(){
                        return step;
                    },
                    this.getStepCount = function(){
                        return stepCount;
                    },
                    this.isBoss = function(){
                        return step>stepCount;
                    },
                    this.getSpace = function(seed){
                        step++;
                        if(step>stepCount){
                            return boss;
                        }
                        var temp = seed;
                        while(temp>1){
                            temp *= Math.random();
                        }
                        temp *= countTotal;
                        for(var x in ratios){
                            if(temp < ratios[x]){
                                return x;
                            }else{
                                temp -= ratios[x];
                            }
                        }
                    }
                };
            })()
        });
        classFactory({
            className:'Logger',
            constructor:(function(){
                var init = function(body,config){
                    var elem = DomFactory.buildFromObject({
                        tag:'textarea',
                        attrs:{
                            id:'console',
                            rows:config.rows,
                            cols:config.cols,
                            readonly:'readonly'
                        }
                    });
                    body.appendChild(elem);
                    return elem;
                };
                return function(body,config){
                    var output = init(config);
                    this.log = function(statement){
                        output.innerHTML += statement + "\n";
                    };
                };
            })()
        });
        classFactory({
            className:'MenuSet',
            constructor:(function(){
                var makeOpt = function(menu,optText,optValue){
                    var opt = document.createElement('option');
                    opt.text = optText;
                    opt.value = JSON.stringify(optValue);
                    menu.add(opt,null);
                };
                var eventFunction = function(ns,fn,param){
                    return FormatString("{namespace}.{function}({param})",{
                        namespace:ns,
                        'function':fn,
                        param:param
                    });
                };
                var setOpts = function(menuSet){
                    return function(menuName,opts){
                        var menu = menuSet;
                        var options = menu.options;
                        while(options.length>0){
                            menu.remove(0);
                        }
                        if(opts instanceof Array){
                            opts.forEach(function(option){
                                makeOpt(menu,option,option);
                            });
                        }else if((typeof opts) == 'object'){
                            for(var x in opts){
                                makeOpt(menu,x,opts[x]);
                            }
                        }
                    };
                };
                var init = function(dom,data){
                    var result = {
                        menuNames:[],
                        menuSet:{},
                        styleSet:{}
                    };
                    for(var x in data.menues){
                        dom.appendChild(DomFactory.buildFromObject({
                            tag:'div',
                            children:[{
                                tag:'label',
                                attrs:{'for':x},
                                children:[x]
                            },{
                                tag:'select',
                                attrs:{
                                    id:x,
                                    onclick:eventFunction(data.ns,data.eventFn,x)
                                }
                            }]
                        }));
                        if(data.menues[x] instanceof Array){
                            dom.lastChild.appendChild(DomFactory.buildFromObject({
                                tag:'button',
                                attrs:{onclick:eventFunction(data.ns,data.cancelFn,x)},
                                children:["Cancel"]
                            }));
                        }
                        result.menuNames.push[x];
                        result.menuSet[x] = dom.lastChild.childNodes[1];
                        result.styleSet[x] = dom.lastChild.style;
                        result.styleSet[x].display = 'none';
                        setOpts(result.menuSet)(x,data.menues[x]);
                    }
                };
                return function(dom,data){
                    var result = init(dom,data);
                    var menuNames = result.menuNames;
                    var menuSet = result.menuSet;
                    var styleSet = result.styleSet;
                    var currentMenu = "";
                    var prevMenu = "";
                    this.rollback = function(){
                        if(prevMenu){
                            this.setCurrentMenu(prevMenu);
                            prevMenu = null;
                        }
                    };
                    this.getMenuNames = function(){
                        return menuNames;
                    };
                    this.getMenu = function(name){
                        return menuSet[name];
                    };
                    this.setCurrentMenu = function(curr){
                        if(currentMenu){
                            styleSet[currentMenu].display = 'none';
                        }
                        prevMenu = currentMenu;
                        currentMenu = curr;
                        if(currentMenu){
                            styleSet[currentMenu].display = 'block';
                        }
                    };
                    this.getCurrentMenuName = function(){
                        return currentMenul
                    };
                    this.getCurrentMenu = function(){
                        return menuSet[this.getCurrentMenuName()];
                    };
                    this.setOptionsOnCurrent = function(opts){
                        return this.setOptions(currentMenu,opts);
                    };
                    this.setOptions = setOpts(menuSet);
                    this.setOptionValuesToRandom = function(menu){
                        var opts = this.getMenu(menu).options;
                        for(var x = 0; x<opts.length; x++){
                            opts[x].value = Math.random();
                        }
                    };
                };
            })()
        });
        classFactory({
            className:'GameCharacter',
            constructor:(function(){
                var uniqueTypes = {
                    persist:true,
                    attack:true,
                    defend:true
                };
                var validateCollections = function(collec,fn){
                    collec = collec || {};
                    var out = {};
                    if(collec instanceof Array){
                        collec.forEach(function(elem){
                            out[elem[fn]()] = elem;
                        });
                    }else{
                        out = collec;
                    }
                    return out;
                };
                var init = function(name,stats,statsByType,items,skills,equipped,availEquipTypes,equipStock,statTypes,statNames){
                    for(var x in uniqueTypes){
                        if(!(x in statsByType)){
                            throw x+" stat type must relate to one and only one stat.";
                        }
                    }
                    for(var x in statsByType){
                        if(x in uniqueTypes){
                            if(((statsByType[x] instanceof Array)&&(statsByType[x].length!=1))||(typeof statsByType != 'string')){
                                throw x+" stat type must relate to one and only one stat.";
                            }
                        }
                        if(typeof statsByType[x] == 'string'){
                            if(statTypes[statsByType[x]]){
                                throw statsByType[x] + " must be linked to only one stat type";
                            }
                            statTypes[statsByType[x]] = x;
                            statNames.push(statsByType[x]);
                        }else if(statsByType[x] instanceof Array){
                            statsByType[x].forEach(function(stat){
                                if(statTypes[stat]){
                                    throw stat + " must be linked to only one stat type";
                                }
                                statTypes[stat] = x;
                                statNames.push(stat);
                            });
                        }
                    }
                    items = items || [];
                    skills = validateCollections(skills,'getName');
                    equipped = validateCollections(equipped,'getType');
                    equipStock = validateCollections(equipStock,'getName');
                    equipTypes = (function(types){
                        types = types || [];
                        var out = [];
                        if(types instanceof Array){
                            out = types;
                        }else{
                            for(var x in types){
                                out.push(x);
                            }
                        }
                        return out;
                    })();
                };
                return function(name,stats,statsByType,items,skills,equipped,availEquipTypes,equipStock){
                    var guarding = false;
                    var dead = false;
                    var statTypes = {};
                    var statNames = [];
                    init(name,stats,statsByType,items,skills,equipped,availEquipTypes,equipStock,statTypes,statNames);
                    this.getName = function(){
                        return name;
                    };
                    this.isDead = function(){
                        return dead;
                    };
                    this.guard = function(skill){
                        if(skill&&(skill instanceof out.Skill)){
                            guarding = skill;
                        }else{
                            guarding = true;
                        }
                    };
                    this.getStatNames = function(){
                        return statNames;
                    };
                    this.getStat = function(stat){
                        return stats[stat];
                    },
                    this.addItem = function(item){
                        items.push(item);
                    };
                    this.useItem = function(index){
                        var item = items.splice(index,1)[0];
                        if(item){
                            var itemStatNames = item.getStatNames();
                            itemStatNames.forEach(function(itemStatName){
                                if(stats[itemStatName]){
                                    stats[itemStatName] += item.getStat(itemStatName);
                                }
                            });
                        }
                    };
                    this.getSkillNames = function(){
                        return function(skls){
                            var out = [];
                            for(var x in skls){
                                out.push[x];
                            }
                            return out;
                        }(skills);
                    };
                    this.learnSkill = function(skill){
                        var skillName = skill.getName();
                        if(!skills[skillName]){
                            skills[skillName] = skill;
                        }
                    };
                    this.applySkill = function(skillName){
                        //TODO -- stub
                    };
                    this.getStockNames = function(){
                        return (function(stock){
                            var out = [];
                            for(var x in stock){
                                out.push(x);
                            }
                            return out;
                        })(equipStock);
                    };
                    this.getCurrentEquipTypes = function(){
                        return (function(equip){
                            var out = [];
                            for(var x in equip){
                                out.push(x);
                            }
                            return out;
                        })(equipped);
                    };
                    this.addEquipment = function(equip){
                        var newName = equip.getName();
                        if(!equipStock[newName]){
                            equipStock[newName] = equip;
                        }
                    };
                    this.equipStock = function(stockName){
                        if(equipStock[stockName]){
                            var newEquip = equipStock[stockName];
                            var type = newEquip.getType();
                            var temp = equipped[type];
                            equipped[type] = newEquip;
                            delete equipStock[stockName];
                            if(temp){
                                var tempName = temp.getName();
                                if(!equipStock[tempName]){
                                    equipStock[tempName] = temp;
                                }
                            }
                        }
                    };
                    this.attack = function(enemy,skillName){
                        //TODO -- stub
                    };
                    this.defend = function(hitPoints){
                        //TODO -- stub
                    };
                };
            })()
        });
        classFactory({
            className:'GameState',
            constructor:function(){
                //TODO -- stub
            }
        });
        classFactory({
            className:'GameContext',
            constructor:function(items,equipment,skills,enemies,player,gameState){
                //TODO -- stub
            }
        });
        classFactory({
            className:'GameView',
            constructor:(function(){
                var setTitle = function(title){
                    var head = document.getElementsByTagName('head')[0];
                    var titles = head.getElementsByTagName('title');
                    for(var x = 0; x<titles.length; x++){
                        head.removeChild(titles[x]);
                    }
                    var newTitle = DomFactory.buildFromObject({
                        tag:'title',
                        children:[title]
                    });
                    head.appendChild(newTitle);
                };
                var initMenu = function(body,menuData){
                    var div = DomFactory.buildFromObject({
                        tag:'div',
                        attrs:{id:'menu'}
                    });
                    body.appendChild(div);
                    return new domain.MenuSet(div,menuData);
                };
                
                return function(viewData){
                    var body = document.createElement('body');
                    html.append(body);
                    setTitle(viewData.title);
                    var logger = new domain.Logger(body,viewData.console);
                    var menuSet = initMenu(viewData.menuSet);
                    this.getLogger = function(){
                        return logger;
                    };
                    this.getMenuSet = function(){
                        return menuSet;
                    };
                };
            })()
        });
        classFactory({
            className:'GameController',
            constructor:function(view,model){
                this.launch = function(){
                    //TODO -- stub
                };
                this.fireEvent = function(){
                    //TODO -- stub
                };
                this.cancelEvent = function(){
                    //TODO -- stub
                };
            }
        });
        classFactory({
            className:'GameReader',
            constructor:function(){
                this.parseJSON = function(jsonObj){
                    var items,equipment,skills,enemies,player,gameState;
                    //TODO -- stub
                    return new obj.GameContext(items,equipment,skills,enemies,player,gameState);
                };
            }
        });
        classFactory({
            className:'GameLauncher',
            constructor:(function(){
                var functions = {
                    launch:'launch',
                    eventFn:'fireEvent',
                    cancelFn:'cancelEvent'
                };
                return function(json){
                    var reader = new org.GameReader(),
                    context = reader.parseJSON(json),
                    view = new org.GameView({
                        title:json.view.title,
                        console:json.view.console,
                        menuSet:{
                            menues:json.view.menues,
                            ns:json.controller.ns,
                            eventFn:json.controller.eventFn,
                            cancelFn:json.controller.cancelFn
                        }
                    }),
                    controller = new org.GameController(view,context);
                    for(var f in functions){
                        window[json.controller.ns][json.controller[f]] = function(){
                            return controller[functions[f]].apply(arguments);
                        };
                    }
                };
            })()
        });
        html.onload = function(){
            launcher = new org.GameLauncher(data);
        }
    })();
});
