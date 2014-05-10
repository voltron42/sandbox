var tests = [{
    name:"Warmup-1",
    description:"Simple warmup problems to get started (solutions available)",
    tests:[{
        name:"sleepIn",
        description:"The parameter weekday is true if it is a weekday, and the parameter vacation is true if we are on vacation. We sleep in if it is not a weekday or we're on vacation. Return true if we sleep in.",
        stub:"sleepIn(weekday, vacation) {\n}",
        solution:"return !weekday || vacation;",
        cases:[{
            params:[false, false],
            result:true
        },{
            params:[true, false],
            result:false
        },{
            params:[false, true],
            result:true
        },{
            params:[true, true],
            result:true
        }]
    },{
        name:"monkeyTrouble",
        description:"We have two monkeys, a and b, and the parameters aSmile and bSmile indicate if each is smiling. We are in trouble if they are both smiling or if neither of them is smiling. Return true if we are in trouble.",
        stub:"monkeyTrouble(aSmile, bSmile) {\n}",
        solution:"return (aSmile && bSmile) || !(aSmile || bSmile);",
        cases:[{
            params:[false, false],
            result:true
        },{
            params:[true, false],
            result:false
        },{
            params:[false, true],
            result:false
        },{
            params:[true, true],
            result:true
        }]
    },{
        name:"sumDouble",
        description:"Given two int values, return their sum. Unless the two values are the same, then return double their sum.",
        stub:"sumDouble(a, b) {\n}",
        solution:"return (a + b) * (a==b?2:1);",
        cases:[{
            params:[1, 2],
            result:3
        },{
            params:[3, 2],
            result:5
        },{
            params:[2, 2],
            result:8
        },{
            params:[-1, 0],
            result:-1
        },{
            params:[3, 3],
            result:12
        },{
            params:[0, 0],
            result:0
        },{
            params:[0, 1],
            result:1
        },{
            params:[3, 4],
            result:7
        }]
    }]
},{
    name:"Warmup-2",
    description:"",
    tests:[{
    }]
}];