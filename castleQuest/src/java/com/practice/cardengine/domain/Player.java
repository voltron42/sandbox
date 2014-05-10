package com.practice.cardengine.domain;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public static class Hand {
        private final List<Card> cards;

        public Hand(){
            this.cards = new ArrayList<Card>();
        }

        public void add(final Card c){
            this.cards.add(c);
        }

        public Card drop(final int index){
            return this.cards.remove(index);
        }
    }

    private final Hand hand;

    public Player(){
        this.hand = new Hand();
    }

    Hand getHand(){
        return this.hand;
    }
}