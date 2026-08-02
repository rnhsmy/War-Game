/**
 * Add your name as a modifier and the date!
 */
package ca.sheridancollege.project;

/**
 * A concrete Card representing a standard playing card (rank + suit) from
 * a 52-card deck, used to play War.
 * @author Ryan Massey
 */
public class PlayingCard extends Card
{
    /**
     * The four suits of a standard deck.
     */
    public enum Suit
    {
        CLUBS, DIAMONDS, HEARTS, SPADES
    }

    // rank runs 2-14, where 11=Jack, 12=Queen, 13=King, 14=Ace
    private final int rank;
    private final Suit suit;

    public PlayingCard(int rank, Suit suit)
    {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * @return the numeric rank of this card, 2-14 (11=J, 12=Q, 13=K, 14=A).
     *         Higher numbers beat lower numbers when comparing cards in War.
     */
    public int getRank()
    {
        return rank;
    }

    /**
     * @return the suit of this card. Suit doesn't affect who wins in War,
     *         but it's part of what makes a card a card.
     */
    public Suit getSuit()
    {
        return suit;
    }

    private String rankName()
    {
        switch (rank)
        {
            case 11: return "J";
            case 12: return "Q";
            case 13: return "K";
            case 14: return "A";
            default: return String.valueOf(rank);
        }
    }

    private String suitSymbol()
    {
        switch (suit)
        {
            case CLUBS: return "\u2663";
            case DIAMONDS: return "\u2666";
            case HEARTS: return "\u2665";
            default: return "\u2660";
        }
    }

    @Override
    public String toString()
    {
        return rankName() + suitSymbol();
    }

}//end class
