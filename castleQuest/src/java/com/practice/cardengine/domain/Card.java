package com.practice.cardengine.domain;

import java.util.HashSet;
import java.util.Set;

import com.base.util.RandomUtils;

public class Card {
	private final Suit suit;
	private final Rank rank;

	private Card(final Suit suit, final Rank rank){
		this.suit = suit;
		this.rank = rank;
	}

	public Suit getSuit() {
		return this.suit;
	}

	public Rank getRank() {
		return this.rank;
	}

	public int hashCode(){
	    int prime = 53;
	    int result = 37;
        result = result * prime + this.suit.hashCode();
        result = result * prime + this.rank.hashCode();
        return result;
	}

	public boolean equals(final Object obj){
	    if(!(obj instanceof Card)){
	        return false;
	    }
	    Card card = (Card) obj;
        if(!this.rank.equals(card.rank)){
            return false;
        }
        if(!this.suit.equals(card.suit)){
            return false;
        }
        return true;
	}

	public Deck newDeck(){
	    return new Deck();
	}

	public static class Deck {
		private final Set<Card> cards;

		private Deck(){
		    this.cards = new HashSet<Card>();
		    for(Suit suit : Suit.values()){
		        for(Rank rank : Rank.values()){
		            this.cards.add(new Card(suit,rank));
		        }
		    }
		}

		public Card drawOne(){
		    return RandomUtils.getElementFromSet(this.cards);
		}
	}

	public static enum Suit {
		HEARTS (SuitColor.RED),
		SPADES (SuitColor.BLACK),
		DIAMONDS (SuitColor.RED),
		CLUBS  (SuitColor.BLACK),
		;

		private SuitColor color;
		Suit(final SuitColor color){
		    this.color = color;
		}

		public SuitColor getColor(){
		    return this.color;
		}
	}
	public static enum SuitColor {
	    RED {
            public SuitColor not() {
                return BLACK;
            }
        },
	    BLACK {
            public SuitColor not() {
                return RED;
            }
        },
	    ;

	    public abstract SuitColor not();
	}
	public static enum Rank {
		ACE (1),
		DEUCE (2),
		THREE (3),
		FOUR (4),
		FIVE (5),
		SIX (6),
		SEVEN (7),
		EIGHT (8),
		NINE (9),
		TEN (10),
		JACK (11),
		QUEEN (12),
		KING (13),
		;
		private final int value;
		Rank(final int value){
			this.value = value;
		}

		public int getValue(){
			return this.value;
		}
	}
}
