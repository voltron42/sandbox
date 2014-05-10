package com.practice.cardengine.domain;

import java.util.Set;

import com.base.util.RandomUtils;
import com.practice.cardengine.domain.Card.Deck;

public class Shoe {
    private Set<Deck> decks;

    public Shoe(final Set<Deck> decks){
        this.decks = decks;
    }

    public Card drawOne(){
        Deck deck = RandomUtils.getElementFromSet(this.decks);
        return deck.drawOne();
    }
}
