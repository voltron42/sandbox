Dog = Class.extend({
    init:function(myName,myAge){
        this.name = myName;
        this.age = myAge;
    },
    setName:function(name){
        this.name = name;
    },
    setAge:function(age){
        this.age = age;
    },
    getName:function(){
        return this.name;
    },
    getAge:function(){
        return this.age;
    },
    getAgeInHumanYears:function(){
        return 7*this.age;
    },
    bark:function(){
        return "Bow wow";
    },
    toString:function(){
        return "Name: {name}, Age: {age}, Age in human years: {getAgeInHumanYears}. {bark}!".format({
            name:this.name,
            age:this.age,
            getAgeInHumanYears:this.getAgeInHumanYears(),
            bark:this.bark()
        });
    }
});
BigDog = Dog.extend({
    bark:function(){
        return "Woof Woof";
    },
    toString:function(){
        return this._super() + " This is a big dog.";
    }
});
MediumDog = Dog.extend({
    bark:function(){
        return "Arf arf";
    },
    toString:function(){
        return this._super() + " This is a medium dog.";
    }
});
SmallDog = Dog.extend({
    bark:function(){
        return "Yip yip";
    },
    toString:function(){
        return this._super() + " This is a small dog.";
    }
});
DogTest = function(){
    return {
        main:function(){
            var log = function(logger){
                return function(statement){
                    statement = statement || "";
                    logger.innerHTML += statement + '<br/>';
                };
            }(document.getElementById('out'));
		    dogs = [];
		    dogs.push(new SmallDog("Pluto",14));
		    dogs.push(new Dog("Goofy",5));
		    dogs.push(new MediumDog("Spot",3));
		    dogs.push(new SmallDog("Beethoven",8));
		    dogs.push(new BigDog("Spike",12));
		    dogs.push(new BigDog("Odin",1));
		    dogs.push(new MediumDog("Milo",18));
		    log();
		    log("Dan's dogs:");
		    log();
		    for(var dogCounter = 0; dogCounter < dogs.length; dogCounter++){
			    log(dogs[dogCounter] + "  This dog goes " + dogs[dogCounter].bark() + ".");
			    log();
		    }
        }
    };
}();
