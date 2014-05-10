var data = {
    state:{
        "STRENGTH":50,
        "WEALTH":50,
        "MONSTER_TALLY":0,
        "SCORE":0
    },
    game:{
        init:"7",
        steps:{
            "1":{
                story:[
                    "You are in a dank, dark dungeon.",
                    "The only light comes into the room from a small hole in the west wall."
                ],
                choices:{
                    "Leave the dungeon":"25",
                    "Take your chances":['5','11']
                }
            },
            "2":{
                story:[
                    "You are in the L-shaped upper hallway of the castle.",
                    "A moth flutters across the room near the ceiling.",
                    "To the north is a door and there is a stairwell in the hall as well."
                ],
                chances:[
                    {
                        go:'13',
                    },
                    {
                        choices:{
                            "Peek through the door to the north":"33",
                            "Look down the stairwell":"39",
                            "Go through the door":"30",
                            "Go down the stairs":"21"
                        }
                    }
                ]
            },
            "3":{
                story:[
                    "Looking down the stairwell, you can see a gloomy,",
                    "unpleasant-looking room, the guardroom of the ancient dungeon."
                ],
                go:"21"
            },
            "4":{
                story:'Inside the sack you find jewels worth $500.',
                action:"WEALTH+=500",
                go:'9'
            },
            "5":{
                story:"The ghost of the guard has awoken!",
                action:"STRENGTH/=2",
                go:'1'
            },
            "6":{
                story:[
                    "You are in the Great Hall of the castle.",
                    "There are two doors in the L-shaped room.",
                    "You notice the wood panels around the room are warped and faded.",
                    "As you look around, a mouse scampers across the floor.",
                    "You whirl around at a sudden noise."
                ],
                chances:[
                    {
                        go:'43',
                    },
                    {
                        choices:{
                            "Look out the windows to get your bearings.":"28",
                            "Exit by the north doors and go west.":"29",
                            "Move to the east":"21"
                        }
                    }
                ]
            },
            "7":{
                story:[
                    "You are at the entrance of a forbidding-looking stone castle.",
                    "You are facing east.",
                    "The huge wooden entrance door stands lightly open."
                ],
                go:"40"
            },
            "8":{
                story:[
                    "A werewolf awakes in this room.",
                    "He attacks you."
                ],
                chances:[
                    {
                        story:"The werewolf has beaten you.",
                        go:'37'
                    },
                    {
                        story:"You have killed the werewolf.",
                        action:'MONSTER_TALLY++',
                        go:'35'
                    }
                ]
            },
            "9":{
                story:[
                    "You are in the storeroom, amidst spices, vegetables, and vast sacks of flour and other provisions.",
                    "The air is thick with spice and curry fumes."
                ],
                choices:{
                    "Look under some of the sacks":"31",
                    "Look inside the top sack":"4",
                    "Leave by the south door":"42",
                    "Leave by the north door":"35",
                }
            },
            "10":{
                story:"Looking up the stairwell you can just make out an elaborately decorated hallway.",
                go:"21"
            },
            "11":{
                story:"There are rats in this dungeon.",
                action:"WEALTH-=10",
                go:'1'
            },
            "12":{
                story:[
                    "You are descending very, very slowly.",
                    "Your strength is sapped by a magic spell left in the elevator."
                ],
                action:"STRENGTH/=2",
                go:"42"
            },
            "13":{
                story:[
                    "A malevolent Maldemer attacks you.",
                    "You can smell the sulfur on his breath."
                ],
                action:"STRENGTH-=10",
                chances:{
                    '3':{
                        go:'42'
                    },
                    '1':{
                        story:"You have killed the Maldemer.",
                        action:"MONSTER_TALLY++",
                        go:'42'
                    }
                }
            },
            "14":{
                story:"You've done it! That was the exit from the castle.",
                action:"STRENGTH*=2",
                go:"27"
            },
            "15":{
                story:"You are in the castle's ancient, hydraulic elevator.",
                choices:{
                    "Descend Slowly":'12',
                    "Get down as quickly as possible":"24"
                }
            },
            "16":{
                story:[
                    "Horrors. There is a devastatingly Ice-Dragon here.",
                    "It leaps toward you. Blood drips from his claws."
                ],
                chances:[
                    {
                        action:[
                            'STRENGTH-=10',
                            'MONSTER_TALLY++'
                        ],
                    },
                    {
                        action:'STRENGTH-=20'
                    }
                ],
                go:"30"
            },
            "17":{
                story:[
                    "This is the monarch's private meeting room.",
                    "The echo of ancient plotting and wrangling hangs heavy in the musty air."
                ],
                chances:[
                    {
                        story:"You find an emerald worth $100, and exit through the south door.",
                        action:"WEALTH+=100"
                    },
                    {
                        story:"You are attacked by a ghastly Gruesomeness which was hiding behind the drapes.",
                        chances:[
                            {
                                story:"You have defeated the Gruesomeness!"
                            },
                            {
                                story:"The Gruesomeness has defeated you.",
                                action:"WEALTH-=100"
                            }
                        ]
                    }
                ],
                go:'21'
            },
            "18":{
                story:[
                    "This tiny room on the upper level is the dressing chamber.",
                    "There is a window to the north.",
                    "There is a door which leaves the room to the north."
                ],
                choices:{
                    "See out the window":"22",
                    "Go out through the door":"32"
                }
            },
            "19":{
                story:[
                    "The noise is frightening.",
                    "What on earth (or beyond it) is inside that room?"
                ],
                go:'23'
            },
            "20":{
                story:[
                    "Aha . . . wealth!",
                    "You find great treasure here worth $900 in gems and gold."
                ],
                action:"WEALTH+=900",
                go:"30"
                
            },
            "21":{
                story:[
                    "You are in the inner hallway, which contains a door to the north, one to the west, and a circular stairwell.",
                    "The room is small and unfriendly."
                ],
                choices:{
                    "Look down the stairwell":"3",
                    "Look up the stairs":"10",
                    "Leave by the north door":"17",
                    "Leave by the west door":"6",
                    "Go up the stairs":"2",
                    "Go down the stairs":"25"
                }
            },
            "22":{
                story:[
                    "Looking out the window you see, below you, the secret herb garden.",
                    "Looking hard to the left, you recognize the land you crossed to get to the castle entrance."
                ],
                go:"18"
            },
            "23":{
                story:[
                    "You are in the room which was used as the castle treasury years ago.",
                    "A spider scampers down the wall.",
                    "There are no windows, just exits to the north and the east."
                ],
                choices:{
                    "Listen at the north door":"19",
                    "Leave by the north door":"32",
                    "Leave by the east door":"36"
                }
            },
            "24":{
                story:[
                    "You feel exhilarated, as a positive spell is triggered by your swift downward flight.",
                    "Your strength is doubled."
                ],
                go:"42"
            },
            "25":{
                story:[
                    "You are in the prison guardroom, in the basement of the castle.",
                    "The stairwell ends in this room.",
                    "There is one other exit, a small whole in the east wall.",
                    "The air is damp and unpleasant . . . a chill wind rushes into the room from gaps in the stone at the top of the walls."
                ],
                choices:{
                    "Go east":"1",
                    "Go up the stairs":"21"
                }
            },
            "26":{
                story:[
                    "Looking out the south window you see the ornamental lake.",
                    "There is a view across open fields through the east window.",
                    "You look longingly at the outdoors"
                ],
                go:'42'
            },
            "27":{
                story:"You have come to the end of the adventure.",
                action:"SCORE = STRENGTH * 5 + 2 * WEALTH + 30 * MONSTER_TALLY"
            },
            "28":{
                story:[
                    "By straining you're eyes through the mist which has swirled up while you've been exploring, you can see below you, looking southwards, an ornamental lake.",
                    "By craning your neck round to the right through the west window you can just see the entrance door to the castle."
                ],
                go:'6'
            },
            "29":{
                story:[
                    "You are in the castle's Audience Chamber.",
                    "The faded tapestries on the wall only hint at the splendor which this room once had.",
                    "There is a window to the west.",
                    "By craning your neck through it to the right you can see the castle entrance.",
                ],
                chances:[
                    {
                        story:"You find diamonds worth $169.",
                        action:"WEALTH+=169"
                    },
                    {
                        story:"A fanatical Fleshgorger suddenly stumbles into the room!",
                        chances:[
                            {
                                story:"You have defeated the Fleshgorger!",
                                action:[
                                    "MONSTER_TALLY++",
                                    "STRENGTH*=2"
                                ]
                            },
                            {
                                story:"The Fleshgorger has defeated you!",
                                action:"STRENGTH/=2"
                            }
                        ]
                    },
                    false,
                    false
                ],
                choices:{
                    "Leave by the north":"40",
                    "Leave by the south or east doors":"6",
                }
            },
            "30":{
                story:[
                    "You find yourself in the master bedroom on the upper level of the castle.",
                    "Looking down from the window to the west you can see the entrance to the castle, while the secret herb garden is visible below the north window.",
                    "There are doors to the east and to the south."
                ],
                choices:{
                    "Leave by the south door":'2',
                    "Leave by the east door":'32',
                    "Take your chances":['20','16']
                }
            },
            "31":{
                story:"A ferocious werewolf leaps at you, his eyes glinting violently.",
                chances:[
                    {
                        story:[
                            "You defeat the werewolf.",
                            "Your strength is diminished by ten, but you manage to escape.",
                        ],
                        action:[
                            "STRENGTH-=10",
                            "MONSTER_TALLY++"
                        ]
                    },
                    {
                        story:[
                            "The werewolf starts tearing you apart, cutting your strength to half of what it was before.",
                            "You drag yourself away."
                        ],
                        action:"STRENGTH/=2"
                    }
                ],
                go:'9'
            },
            "32":{
                story:[
                    "Oooh . . . you are in the chambermaid's bedroom.",
                    "Faint perfume hangs in the air.",
                    "There is an exit to the west and a door to the south.",
                    "However, your attention is caught by the steady thumping coming from behind the bed.",
                    "Fearfully, you look behind it, and a devastating Ice-dragon leaps at you!",
                    "You smell the sulfur on his breath."
                ],
                chances:{
                    '3':{
                        story:"The Ice-dragon has defeated you!",
                        action:"STRENGTH-=15"
                    },
                    '1':{
                        story:"You have defeated the Ice-dragon!",
                        action:"STRENGTH+=100"
                    }
                },
                choices:{
                    "Leave by the north door.":"18",
                    "Leave by the west door.":"30",
                    "Leave by the south door.":"23",
                }
            },
            "33":{
                story:[
                    "Peering into the room you see it was once the master bedroom.",
                    "It appears quiet.",
                    "As you turn back you notice a small bottle on the ground.",
                    "Quickly, you uncork it and swallow the contents."
                ],
                chances:{
                    '1':{
                        story:"The bottle contains water. Refreshing."
                    },
                    '3':{
                        story:"The bottle contains a wonderful elixir which has tripled your strength.",
                        action:"STRENGTH*=3"
                    },
                },
                go:'2'
            },
            "34":{
                story:"Now you're under attack from a nasty, chastly Gruesomeness.",
                chances:[
                    {
                        story:"You've defeated the Gruesomeness!",
                        action:"STRENGTH+=50"
                    },
                    {
                        story:"The Gruesomeness has defeated you!",
                        action:"STRENGTH/=2"
                    },
                    false,
                    false
                ],
                go:'2'
            },
            "35":{
                story:[
                    "This is the castle's kitchen.",
                    "Through windows in the north wall you can see the secret herp garden.",
                    "It has been many years since meals were prepared here for the monarch and the court.",
                    "A rat scurries across the floor."
                ],
                chances:[
                    {
                        story:'You leave by the south door.',
                        go:'9'
                    },
                    {
                        story:'You stumble, making a noise!',
                        go:'8'
                    }
                ]
            },
            "36":{
                story:[
                    "You are in a small room which is outside the castle itself.",
                    "You can see the lake through the southern windows."
                ],
                chances:[
                    {
                        go:'41',
                    },
                    {
                        choices:{
                            'Leave by the North Door':'15',
                            'Leave by the West Door':'23'
                        }
                    }
                ]
            },
            "37":{
                story:'You are dead!!!',
                go:'27'
            },
            "38":{
                go:'14'
            },
            "39":{
                story:[
                    'Looking down the stairwell you can see a small room below, and on the floor below that, you can just make out the gloomy guardroom.',
                    'A ghost of a former guardsman suddenly appears on the stairwell.',
                    'He demands $100.'
                ],
                action:{
                    'WEALTH>=100':{
                        story:'You pay him.',
                        action:'WEALTH-=100',
                        go:'2'
                    },
                    'WEALTH<100':{
                        story:[
                            'You do not have enough to pay him.',
                            'The guard attacks you in anger.'
                        ],
                        chances:[
                            {
                                go:'37'
                            },
                            {
                                story:'Your strength has been halved.',
                                action:'STRENGTH/=2',
                                go:'2'
                            }
                        ]
                    }
                },
            },
            "40":{
                story:[
                    'You are in the hallway entrance to the castle.',
                    'it is dark and gloomy, and the air of decay and desolation is very depressing.',
                    'You suddenly feel very frightened.'
                ],
                choices:{
                    'Run away from the castle':'7',
                    'Proceed through the south door':'29'
                }
            },
            "41":{
                story:[
                    'A spider bites you on the leg.',
                    'Your strength is diminished by 20.'
                ],
                action:'STRENGTH-=20',
                to:'36'
            },
            "42":{
                story:[
                    'You are in the rear vestibule.',
                    'There are windows to the south.'
                ],
                chances:[
                    {
                        go:'13',
                    },
                    {
                        choices:{
                            'Look out the windows':'26',
                            'Leave by the north door':'9',
                            'Leave by the east door':'38'
                        }
                    }
                ]
            },
            "43":{
                story:[
                    'The horrendous Hodgepodge attacks you!!!',
                    'He fights wildly, madly.'
                ],
                chances:{
                    '1':{
                        story:[
                            'The horrendous Hodgepodge has beaten you.',
                            'Your strength has been halved.'
                        ],
                        action:'STRENGTH/=2'
                    },
                    '3':{
                        story:[
                            'You have been beated by the horrendous Hodgepodge.',
                            'Your strength has been doubled.'
                        ],
                        action:'STRENGTH*=2'
                    }
                },
                go:'6'
            }
        }
    }
};