CQ = function(){
    var domain = {
        Item:function(name,stat,value,type){
            return {
                getName:function(){
                    return name;
                },
                getStat:function(){
                    return stat;
                },
                getValue:function(){
                    return value;
                },
                getType:function(){
                    return type;
                }
            };
        },
        Skill:function(name,valueStat,value,costStat,cost,type){
            return {
                getName:function(){
                    return name;
                },
                getValueStat:function(){
                    return valueStat;
                },
                getValue:function(){
                    return value;
                },
                getCostStat:function(){
                    return costStat;
                },
                getCost:function(){
                    return cost;
                },
                getType:function(){
                    return type;
                },
                adjust:function(stats){
                    stats[valueStat] += value;
                    stats[costStat] -= cost;
                },
                isActive:function(){
                    return (valueStat in {'Hit':true,'Block':true});
                }
            };
        },
        Sprite:function(name,defaults){
            var stats = {};
            ["Life","Hit","Block","Magic"].forEach(function(label){
                if(defaults[label]){
                    stats[label] = defaults[label];
                }else{
                    stats[label] = 0;
                }
            });
            var guarding = false;
            var dead = false;
            var weapon = defaults.Weapon||null;
            var armor = defaults.Armor||null;
            var shield = defaults.Shield||null;
            var items = [];
            return {
                getName:function(){
                    return name;
                },
                getStat:function(stat){
                    return stats[stat];
                },
                attack:function(enemy,spell){
                    var adjusted = {};
                    for(var x in stats){
                        adjusted[x] = stats[x];
                    }
                    if(spell){
                        if(spell.isActive()){
                            adjusted[spell.getValueStat()] += spell.getValue();
                            stats[spell.getCostStat()] -= spell.getCost();
                        }else{
                            spell.adjust(stats);
                        }
                    }else{
                        if(weapon){
                            adjusted.Hit += weapon.getValue();
                        }
                    }
                    return enemy.defend(adjusted.Hit);
                },
                defend:function(hitCount){
                    var adjusted = {};
                    if(armor){
                        hitCount -= armor.getValue();
                    }
                    if(guarding){
                        if((typeof guarding) == 'object' && guarding.getValueStat() == 'Block'){
                            stats[guarding.getCostStat()] -= guarding.getCost();
                            hitCount -= guarding.getValue();
                        }else{
                            if(shield){
                                hitCount -= shield.getValue();
                            }
                        }
                        guarding = false;
                    }
                    if(hitCount > 0){
                        stats.Life -= hitCount;
                        if(stats.Life<=0){
                            dead = true;
                        }
                        return hitCount;
                    }else{
                        return 0;
                    }
                },
                isDead:function(){
                    return dead;
                },
                getWeapon:function(){
                    return weapon;
                },
                getArmor:function(){
                    return armor;
                },
                getShield:function(){
                    return shield;
                },
                setWeapon:function(w){
                    if(!weapon||(weapon&&(weapon.getValue()<w.getValue()))){
                        weapon = w;
                        return true;
                    }
                    return false;
                },
                setArmor:function(r){
                    if(!armor||(armor&&(armor.getValue()<r.getValue()))){
                        armor = r;
                        return true;
                    }
                    return false;
                },
                setShield:function(s){
                    if(!shield||(shield&&(shield.getValue()<s.getValue()))){
                        shield = s;
                        return true;
                    }
                    return false;
                },
                guard:function(spell){
                    if(spell){
                        guarding = spell;
                    }else{
                        guarding = true;
                    }
                },
                isGuarding:function(){
                    if(guarding){
                        return true;
                    }else{
                        return false;
                    }
                },
                addItem:function(item){
                    items.push(item);
                },
                listItems:function(){
                    return items.map(function(item){
                        return item.getName();
                    });
                },
                useItem:function(index){
                    var item = items[index];
                    if(item){
                        stats[item.getStat()] += item.getValue();
                        items.splice(index,1);
                        return true;
                    }else{
                        return false;
                    }
                },
                cast:function(spell){
                    if(!spell.isActive()){
                        spell.adjust(stats);
                        return true;
                    }else{
                        return false;
                    }
                }
            };
        },
        Level:function(boss,stepCount,ratios){
            var countTotal = 0;
            for(var x in ratios){
                countTotal += ratios[x];
            }
            var step = 0;
            return {
                getStep:function(){
                    return step;
                },
                getStepCount:function(){
                    return stepCount;
                },
                getSpace:function(seed){
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
                },
                isBoss:function(){
                    return step>stepCount;
                }
            };
        }
    };
    var menues = function(){
        var buildMenues = function(dom,obj){
            var tempMenues = {};
            var tempStyles = {};
            var out = {};
            for(var x in obj){
                var div = document.createElement("div");
                dom.appendChild(div);
                var temp = buildMenu(x,obj[x]);
                div.appendChild(buildLabel(x));
                div.appendChild(temp);
                if(obj[x] instanceof Array){
                    var cancel = document.createElement("button");
                    cancel.innerText = "Cancel";
                    cancel.setAttribute("onclick","CQ.cancelMenu('"+x+"')");
                    div.appendChild(cancel);
                }
                div.style.display = 'none';
                tempMenues[x] = temp;
                tempStyles[x] = div.style;
            }
            out = function(menuList,styleList){
                var current;
                var prev;
                var names = [];
                var setMenu = function(curr){
                    if(current){
                        styleList[current].display = 'none';
                    }
                    prev = current;
                    current = curr;
                    if(current){
                        styleList[current].display = 'block';
                    }
                };
                return {
                    rollback:function(){
                        if(prev){
                            setMenu(prev);
                            prev = null;
                        }
                    },
                    getMenuNames:function(){
                        if(names.length<=0){
                            for(var x in menuList){
                                names.push(x);
                            }
                        }
                        return names;
                    },
                    getMenu:function(name){
                        return menuList[name];
                    },
                    getCurrentMenu:function(){
                        return menuList[current];
                    },
                    getCurrentMenuName:function(){
                        return current;
                    },
                    setCurrentMenu:setMenu
                };
            }(tempMenues,tempStyles);
            return out;
        }
        var buildLabel = function(id){
            var out = document.createElement("label");
            out.setAttribute("for",id);
            out.innerText = id;
            return out;
        };
        var buildMenu = function(id,options){
            var out = document.createElement("select");
            out.setAttribute("id",id);
            out.setAttribute("onclick","CQ.call(this)");
            setOptions(out,options);
            return out;
        };
        var setOptions = function(menu,options){
            while(menu.options.length>0){
                menu.remove(0);
            }
            if(options instanceof Array){
                options.forEach(function(option){
                    var opt = document.createElement("option");
                    opt.text = option;
                    opt.value = JSON.stringify(option);
                    menu.add(opt,null);
                });
            }else if((typeof options) == 'object'){
                for(var x in options){
                    var opt = document.createElement("option");
                    opt.text = x;
                    opt.value = JSON.stringify(options[x]);
                    menu.add(opt,null);
                }
            }
        }
        var setOptionValuesToRandom = function(menu){
            var opts = menu.options;
            for(var x = 0; x<opts.length; x++){
                opts[x].value = Math.random();
            }
        }
        return {
            buildMenues:function(dom,obj){
                return buildMenues(dom,obj);
            },
            setOptions:function(menu,options){
                setOptions(menu,options);
            },
            setOptsToRandom:function(menu){
                setOptionValuesToRandom(menu);
            }
        };
    }();
    var buildBody = function(dataIn,output,menu){
        var log = function(statement){
            output.value += statement + "\n";
            output.scrollTop = output.scrollHeight;
        };
        var singles = [];
        for(var x in dataIn.Weapon){
            singles.push(domain.Item(x,"Hit",dataIn.Weapon[x],"Weapon"));
        }
        for(var x in dataIn.Armor){
            singles.push(domain.Item(x,"Block",dataIn.Armor[x],"Armor"));
        }
        for(var x in dataIn.Shield){
            singles.push(domain.Item(x,"Block",dataIn.Shield[x],"Shield"));
        }
        var context = {
            items:[],
            enemies:[],
            spells:{},
            levels:[],
            locRefs:{},
            flags:{
                turnOrBattle:false,
                offenseOrDefense:false,
                useOrView:false,
                fightOrFlight:false
            },
            vars:{
                currentSpell:null,
                currentEnemy:null,
                currentLevel:0,
                isBoss:false
            }
        };
        for(var x in dataIn.items){
            var itemName;
            var itemStat;
            for(var y in dataIn.items[x]){
                itemName = y;
                itemStat = dataIn.items[x][y];
            }
            context.locRefs[x] = {
                loc:"items",
                index:context.items.length
            };
            context.items.push(function(name,value,stat){
            return function(){
                return domain.Item(name,value,stat);
            }}(x,itemName,itemStat));
        }
        singles.forEach(function(single){
            context.locRefs[single.getName()] = {
                loc:"items",
                index:context.items.length
            };
            context.items.push(function(item){
                return function(){
                    return item;
                };
            }(single));
        });
        for(var x in dataIn.Enemies){
            context.locRefs[x] = {
                loc:"enemies",
                index:context.enemies.length
            };
            var temp = dataIn.Enemies[x];
            ["Weapon","Armor","Shield"].forEach(function(label){
                if(label in temp){
                    temp[label] = context.items[context.locRefs[temp[label]].index]();
                }
            });
            context.enemies.push(function(name,obj){
                return function(){
                    return domain.Sprite(name,obj);
                };
            }(x,temp));
        }
        dataIn.levels.forEach(function(level){
            context.levels.push(domain.Level(level.Boss,level.length,level.ratios));
        });
        dataIn.gameMenues.spells = [];
        for(var x in dataIn.spells){
            var v;
            var c;
            for(var y in dataIn.spells[x]){
                if(y=='Magic'){
                    var c = y;
                }else{
                    var v = y;
                }
            }
            context.spells[x] = domain.Skill(x,v,dataIn.spells[x][v],c,-dataIn.spells[x][c]);
            dataIn.gameMenues.spells.push(x);
        }
        dataIn.gameMenues.items = [];
        context.menues = menues.buildMenues(menu,dataIn.gameMenues);
        menues.setOptsToRandom(context.menues.getMenu('move'));
        context.menues.setCurrentMenu('turn');
        context.player = domain.Sprite("Player",dataIn.playerDefaults);
        context.defaultFlags = {};
        for(var x in context.flags){
            context.defaultFlags[x] = context.flags[x];
        }
        var enemyTurn = function(){
            if(context.vars.currentEnemy.isDead()){
                log("{enemy} has died.".format({
                    enemy:context.vars.currentEnemy.getName()
                }));
                context.vars.currentEnemy = null;
                context.flags.fightOrFlight = false;
                return true;
            }else{
                var score;
                var spellToCast;
                var options = [];
                var costStat = 'Magic';
                var priorities = ['Hit','Block','Life'];
                do {
                    priorities.forEach(function(stat){
                        for(var name in context.spells){
                            var spell = context.spells[name];
                            if(spell.getValueStat()==stat&&spell.getCostStat()==costStat&&-spell.getCost()<=context.vars.currentEnemy.getStat(costStat)){
                                options.push(spell);
                            }
                        }
                        var ref = 0;
                        if(options.length>1){
                            var max = 0;
                            options.forEach(function(spell,index){
                                if(spell.getValue()>max){
                                    max = spell.getValue();
                                    ref = index;
                                }
                            });
                        }
                        spellToCast == options[ref];
                        return spellToCast;
                    });
                    if(spellToCast&&!spellToCast.isActive()){
                        if(context.vars.currentEnemy.cast(spellToCast)){
                            log("{enemy} cast {spell}.".format({
                                enemy:context.vars.currentEnemy.getName(),
                                spell:spellToCast.getName()
                            }));
                        }
                    }
                } while (spellToCast&&!spellToCast.isActive());
                if(spellToCast){
                    if(spellToCast.getValueStat()=='Hit'){
                        score = context.vars.currentEnemy.attack(context.player,spellToCast);
                    }else if(spellToCast.getValueStat()=='Block'){
                        context.vars.currentEnemy.guard(spellToCast);
                    }else{
                        score = context.vars.currentEnemy.attack(context.player,spellToCast);
                    }
                }else{
                    score = context.vars.currentEnemy.attack(context.player);
                }
                if(context.vars.currentEnemy.isGuarding()){
                    log("{enemy} is guarding.".format({
                        enemy:context.vars.currentEnemy.getName(),
                    }));
                }else{
                    log("{enemy} has scored {score} hit points on {player}".format({
                        enemy:context.vars.currentEnemy.getName(),
                        player:context.player.getName(),
                        score:score
                    }));
                }
                return false;
            }
        };
        var checkLevel = function(){
            if(context.levels[context.vars.currentLevel].isBoss()){
                context.vars.currentLevel++;
                log("{player} has completed level {level}".format({
                    player:context.player.getName(),
                    level:context.vars.currentLevel
                }));
                if(context.vars.currentLevel>context.levels.length){
                    log("YOU WIN!");
                    log("GAME OVER");
                    return true;
                }
            }
            return false;
        };
        var nonturnProcessing = function(type,label){
            console.log("nonturnProcessing");
            console.log(context.flags);
            if(context.flags.useOrView){
                if(type == 'spells'){
                    if(context.flags.turnOrBattle){
                        console.log("");
                        console.log("spell to cast");
                        var spell = context.spells[label];
                        if(context.player.cast(spell)){
                            log("You have cast {name}.".format({
                                name:spell.getName()
                            }))
                        }else{
                            log("You cannot cast {name} here.".format({
                                name:spell.getName()
                            }))
                        }
                        console.log("");
                        context.flags.turnOrBattle = false;
                    }else{
                        var spell = context.spells[label];
                        if(spell.getValueStat() == "Hit"){
                            log("You have cast {name}.".format({
                                name:spell.getName()
                            }))
                            var score = context.player.attack(context.vars.currentEnemy,spell);
                            log("{player} has scored {score} hit points on {enemy}".format({
                                enemy:context.vars.currentEnemy.getName(),
                                player:context.player.getName(),
                                score:score
                            }));
                            if(enemyTurn()){
                                var nextMenu = 'turn';
                                if(checkLevel()){
                                    nextMenu = null;
                                }
                                context.menues.setCurrentMenu(nextMenu);
                                return ;
                            }
                        }else if(spell.getValueStat() == "Block"){
                            log("You have cast {name}.".format({
                                name:spell.getName()
                            }))
                            context.player.guard(spell);
                            log("{player} is guarding.".format({
                                player:context.player.getName(),
                            }));
                            if(enemyTurn()){
                                var nextMenu = 'turn';
                                if(checkLevel()){
                                    nextMenu = null;
                                }
                                context.menues.setCurrentMenu(nextMenu);
                                return ;
                            }
                        }else{
                            log("You cannot cast {name} here.".format({
                                name:spell.getName()
                            }))
                        }
                        if(context.player.isDead()){
                            log("{player} is dead.".format({
                                player:context.player.getName()
                            }));
                            log("GAME OVER");
                            context.menues.setCurrentMenu();
                            return ;
                        }
                    }
                }else if(type == 'items'){
                    var index = label;
                    var itemName = context.player.listItems()[index];
                    if(context.player.useItem(index)){
                        log("You have used {item}.".format({
                            item:itemName
                        }));
                    }else{
                        log("You cannot use this {item} here.".format({
                            item:itemName
                        }));
                    }
                }
            }else{
                if(type == 'spells'){
                    var spell = context.spells[label];
                    log("Spell: {name}, Value: {value} {valueStat}, Cost: {cost} {costStat}".format({
                        name:spell.getName(),
                        valueStat:spell.getValueStat(),
                        value:spell.getValue(),
                        costStat:spell.getCostStat(),
                        cost:spell.getCost()
                    }));
                }else if(type == 'items'){
                    var index = label;
                    var itemName = context.player.listItems()[index];
                    var item = context.items[itemName]();
                    log("Spell: {name} ({type}), Value: {value} {stat}".format({
                        name:spell.getName(),
                        stat:spell.getStat(),
                        value:spell.getValue(),
                        type:spell.getType()
                    }));
                }
            }
            context.menues.rollback();
        }
        var viewPlayer = function(){
            log("");
            log("You are {player}.".format({
                player:context.player.getName()
            }));
            ["Life","Hit","Block","Magic"].forEach(function(name){
                log("\t{stat}: {value}".format({
                    stat:name,
                    value:context.player.getStat(name)
                }));
            });
            ["Weapon","Armor","Shield"].forEach(function(type){
                var equip = context.player['get'+type]();
                if(equip){
                    log("{equip}: {name} ({stat}: {value})".format({
                        equip:type,
                        name:equip.getName(),
                        stat:equip.getStat(),
                        value:equip.getValue()
                    }));
                }
            });
            log("Items: {list}".format({
                list:context.player.listItems().join(',')
            }));
        };
        var turnProcessing = function(spec,flags){
            for(var x in flags){
                if(x in context.flags){
                    context.flags[x] = flags[x];
                }
            }//start
            
            console.log(spec);
            console.log(flags);
            console.log("menu: "+flags.menu);
            
            if(flags.showAll){
                viewPlayer();
            }
            
            if(context.flags.fightOrFlight){
                if(context.flags.offenseOrDefense){
                    var score = context.player.attack(context.vars.currentEnemy);
                    log("{player} has scored {score} hit points on {enemy}".format({
                        enemy:context.vars.currentEnemy.getName(),
                        player:context.player.getName(),
                        score:score
                    }));
                }else{
                    context.player.guard();
                    log("{player} is guarding.".format({
                        player:context.player.getName(),
                    }));
                }
                if(enemyTurn()){
                    flags.menu = 'turn';
                    if(checkLevel()){
                        flags.menu = 'null';
                    }
                }
                if(context.player.isDead()){
                    log("{player} is dead.".format({
                        player:context.player.getName()
                    }));
                    log("GAME OVER");
                    flags.menu = null;
                }
            }
            
            if(flags.menu){//last command
                context.menues.setCurrentMenu(flags.menu);
                if(flags.menu == 'move'){
                    menues.setOptsToRandom(context.menues.getCurrentMenu());
                }else if(flags.menu == 'items'){
                    menues.setOptions(context.menues.getMenu("items"),context.player.listItems());
                }
            }
            context.flags.fightOrFlight = false;
        };
        log("You are playing CASTLE-QUEST!");
        viewPlayer();
        return {
            log:log,
            context:context,
            controller:{
                move:function(action,index,meta){
                    console.log("move: "+action+" - "+meta);
                    log("You've moved {move}.".format({
                        move:action
                    }));
                    var space = context.levels[context.vars.currentLevel].getSpace(meta);
                    console.log("space: "+space);
                    var ref = context.locRefs[space];
                    console.log("ref: "+JSON.stringify(ref));
                    var type = ref.loc;
                    console.log("type: "+type);
                    var x = ref.index;
                    console.log("x: "+x);
                    var hold = {};
                    hold[type] = context[type][x]();
                    console.log("hold: "+JSON.stringify(hold));
                    if(type == 'enemies'){
                        context.vars.currentEnemy = hold[type];
                        context.menues.setCurrentMenu('battle');
                        log("You have encountered a {enemy}. What will you do?".format({
                            enemy:hold[type].getName()
                        }));
                    }else if(type == 'items'){
                        context.menues.setCurrentMenu('turn');
                        var itemType = hold[type].getType();
                        if(itemType){
                            var itemName = hold[type].getName()+((itemType=="Armor"||itemType=="Shield")?" "+itemType:"");
                            if(context.player['set'+itemType](hold[type])){
                                log("You found a {name}! Your {stat} points will be increased by {count}.".format({
                                    name:itemName,
                                    stat:hold[type].getStat(),
                                    count:hold[type].getValue()
                                }));
                            }else{
                                var hasItem = context.player['get'+itemType]();
                                log("You found a {name}. You already have a {mine}, which is more powerful.".format({
                                    name:itemName,
                                    mine:hasItem.getName()
                                }));
                            }
                        }else{
                            context.player.addItem(hold[type]);
                            log("You found a {name}! It will be added to your inventory. When you use it, your {stat} points will be increased by {count}.".format({
                                name:hold[type].getName(),
                                stat:hold[type].getStat(),
                                count:hold[type].getValue()
                            }));
                        }
                    }
                },
                battle:function(action,index,meta){
                    console.log("battle: "+action);
                    turnProcessing(action,meta);
                },
                turn:function(action,index,meta){
                    console.log("turn: "+action+" - "+meta);
                    turnProcessing(action,meta);
                },
                spells:function(spell,index){
                    console.log("spells: "+spell);
                    nonturnProcessing("spells",spell);
                },
                items:function(item,index){
                    console.log("items: "+item);
                    console.log("index: "+index);
                    nonturnProcessing("items",index);
                }
            },
            cancelMenu:function(){
                context.menues.rollback();
            }
        };
    };
    return {
        launch:function(){
            CQ.runtime = buildBody(data,document.getElementById("console"), document.getElementById("menu"));
        },
        call:function(selector){
            var f = function(menu,index,item){
                eval("var value = "+item.value);
                console.log("menu = "+menu);
                console.log("index = "+index);
                console.log("text = "+item.text);
                console.log("value = "+value);
                CQ.runtime.controller[menu](item.text,index,value);
            }(selector.id,selector.selectedIndex,selector.options[selector.selectedIndex]);
        },
        cancelMenu:function(menu){
            console.log("menu to cancel: " + menu);
            CQ.runtime.cancelMenu();
        }
    };
}();
