var rules = {
    blackjack:{
        predealBet:true,
        deal:{
            dealer:["down","up"],
            player:["up","up"]
        },
        play:[{
            forEach:"player",
            play:[{
                doThis:"hit",
                until:"stay"
            },{
                score:["player","dealer"]
            }]
        }],
        scoreRules:{
            cards:[{
                rank:"ace",
                value:[1,11]
            },{
                rank:["jack","king","queen"],
                value:10
            }],
            hand:[
                "sum<=21",
            ],
            contest:"player/dealer"
        }
    },
    "texas hold'em":{
        predealBet:true,
        play:[{
            
        }]
    }
}