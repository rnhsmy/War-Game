/**
 * Add your name as a modifier and the date!
 */
package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 * A concrete Player for the game of War. Each WarPlayer owns a hand
 * (a GroupOfCards) that starts with half the deck, and keeps track of
 * whichever card it most recently played face up.
 * @author Ryan Massey
 */
public class WarPlayer extends Player
{
    private GroupOfCards hand;
    private PlayingCard cardInPlay;

    public WarPlayer(String name)
    {
        super(name);
        hand = new GroupOfCards(52); // 52 is just an upper bound, not a hard cap
    }

    /**
     * @return this player's hand of cards
     */
    public GroupOfCards getHand()
    {
        return hand;
    }

    /**
     * @return the card this player most recently turned face up, or null
     *         if they haven't played yet / had no cards left to play
     */
    public PlayingCard getCardInPlay()
    {
        return cardInPlay;
    }

    /**
     * @return true if this player still has at least one card left to play
     */
    public boolean hasCards()
    {
        return !hand.isEmpty();
    }

    /**
     * @return how many cards are currently in this player's hand
     */
    public int numCards()
    {
        return hand.numCards();
    }

    /**
     * Plays this player's turn: draws the top card off their hand and turns
     * it face up as the cardInPlay. Does nothing (leaves cardInPlay as-is)
     * if the player has no cards left, which can happen mid-war.
     */
    @Override
    public void play()
    {
        if (hasCards())
        {
            cardInPlay = (PlayingCard) hand.removeCard();
        }
    }

    /**
     * Awards a won pot of cards to this player, adding them to the bottom
     * of their hand.
     * @param wonCards the cards this player won and should collect
     */
    public void winCards(ArrayList<Card> wonCards)
    {
        hand.addCards(wonCards);
    }

}//end class
