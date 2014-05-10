DogTest = function(){
    var classes = (function(){
        var out = {};
        var classify = Classify(out);
        classify('Dog',function(myName,myAge){
            this.setName = function(name){
                myName = name;
            };
            this.setAge = function(age){
                myAge = age;
            };
            this.getName = function(){
                return myName;
            };
            this.getAge = function(){
                return myAge;
            };
            this.getAgeInHumanYears = function(){
                return 7*myAge;
            };
            this.bark = function(){
                return "Bow wow";
            };
            this.toString = function(){
                return "Name: {name}, Age: {age}, Age in human years: {getAgeInHumanYears}. {bark}!".format({
                    name:myName,
                    age:myAge,
                    getAgeInHumanYears:this.getAgeInHumanYears(),
                    bark:this.bark()
                });
            };
        });
        classify('BigDog',out.Dog,function(myName,myAge){
            this.bark = function(){
                return "Woof Woof";
            };
            this.toString = function(){
                return this._super() + " This is a big dog.";
            };
        });
        classify('MediumDog',out.Dog,function(myName,myAge){
            this.bark = function(){
                return "Arf arf";
            };
            this.toString = function(){
                return this._super() + " This is a medium dog.";
            };
        });
        classify('SmallDog',out.Dog,function(myName,myAge){
            this.bark = function(){
                return "Yip yip";
            };
            this.toString = function(){
                return this._super() + " This is a small dog.";
            };
        });
        classify('Chihuahua',out.SmallDog,function(myName,myAge){
            this.bark = function(){
                return "Yo quiero Taco Bell! "+this._super();
            }
        });
        return out;
    })();
    return {
        main:function(){
            var log = function(logger){
                return function(statement){
                    statement = statement || "";
                    logger.innerHTML += statement + '<br/>';
                };
            }(document.getElementById('out'));
            dogs = [];
            dogs.push(new classes.SmallDog("Pluto",14));
            dogs.push(new classes.Dog("Goofy",5));
            dogs.push(new classes.Dog("Kevin",8));
            dogs.push(new classes.MediumDog("Spot",3));
            dogs.push(new classes.SmallDog("Beethoven",8));
            dogs.push(new classes.BigDog("Spike",12));
            dogs.push(new classes.BigDog("Odin",1));
            dogs.push(new classes.MediumDog("Milo",18));
            dogs.push(new classes.Chihuahua("Stanley",4));
            log();
            log("Dan's dogs:");
            log();
            var classNames = ['Dog','BigDog','SmallDog','MediumDog','Chihuahua'];
            dogs.forEach(function(dog){
                log(dog + "  This dog goes " + dog.bark() + ".");
                classNames.forEach(function(cls){
                    if(dog instanceof classes[cls]){
                        log(dog.getName() + " is a " + cls);
                    }
                });
                log();
            });
        }
    };
}();
