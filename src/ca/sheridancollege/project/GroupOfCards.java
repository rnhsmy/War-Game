/**
 * Students can modify and extend to implement their game.
 * Add your name as a modifier and the date!
 *
 * Modified by Ryan, 2026: initialized the cards list in the constructor
 * (it was previously left null, which would cause a NullPointerException
 * the first time shuffle() or showCards() was used), and added the basic
 * add/remove helper methods a card game needs to move cards between groups.
 */
package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A concrete class that represents any grouping of cards for a Game.
 * HINT, you might want to subclass this more than once.
 * The group of cards has a maximum size attribute which is flexible for reuse.
 * Ryan, 2026
 */
public class GroupOfCards 
{
   
    //The group of cards, stored in an ArrayList
    private ArrayList <Card> cards;
    private int size;//the size of the grouping
    
    public GroupOfCards(int givenSize)
    {
        size = givenSize;
        cards = new ArrayList<>();
    }
    
    /**
     * A method that will get the group of cards as an ArrayList
     * @return the group of cards.
     */
    public ArrayList<Card> showCards()
    {
        return cards;
    }
    
    public void shuffle()
    {
        Collections.shuffle(cards);
    }

    /**
     * @return the size of the group of cards
     */
    public int getSize() {
        return size;
    }

    /**
     * @param givenSize the max size for the group of cards
     */
    public void setSize(int givenSize) {
        size = givenSize;
    }
    
    // --- The methods below were added by Ryan, 2026, so this class can
    //     actually be used to deal, draw, and win cards during play. ---

    /**
     * Adds a single card to the bottom of this group.
     * @param card the card to add
     */
    public void addCard(Card card)
    {
        cards.add(card);
    }

    /**
     * Adds a whole collection of cards to the bottom of this group, in order.
     * Handy for a player collecting a won pot, for example.
     * @param cardsToAdd the cards to add
     */
    public void addCards(ArrayList<Card> cardsToAdd)
    {
        cards.addAll(cardsToAdd);
    }

    /**
     * Removes and returns the top card (index 0) of this group.
     * @return the top card, or null if this group is empty
     */
    public Card removeCard()
    {
        if (cards.isEmpty())
        {
            return null;
        }
        return cards.remove(0);
    }

    /**
     * @return true if this group currently has no cards in it
     */
    public boolean isEmpty()
    {
        return cards.isEmpty();
    }

    /**
     * @return how many cards are currently in this group (distinct from
     *         getSize(), which is just the configured maximum/target size)
     */
    public int numCards()
    {
        return cards.size();
    }
    
}//end class
