/**
 * Add your name as a modifier and the date!
 */
package ca.sheridancollege.project;

import java.util.ArrayList;

/**
 * A concrete Game implementing the classic card game War for two players.
 *
 * Rules implemented:
 *  - A standard 52-card deck is shuffled and split evenly between two players.
 *  - Each round both players play their top card; higher rank wins the pot.
 *  - On a tie, a war happens: each player stakes up to 3 cards face down
 *    plus 1 face up, and the higher face-up card wins everything on the
 *    table. Ties during a war trigger another war, recursively.
 *  - If a player runs out of cards partway through staking a war, they
 *    stake whatever they have left, which may cost them the game.
 *  - The game ends when one player holds every card, or a round limit is
 *    hit (some shuffles of War can go on for a very long time).
 *
 * @author Ryan, 2026
 */
public class WarGame extends Game
{
    // War can theoretically go on close to forever with certain shuffles,
    // so this is a safety valve to guarantee the game terminates.
    private static final int MAX_ROUNDS = 100_000;

    private int roundNumber = 0;
    private final boolean verbose;

    public WarGame(boolean verbose)
    {
        super("War");
        this.verbose = verbose;

        ArrayList<Player> players = new ArrayList<>();
        players.add(new WarPlayer("Player 1"));
        players.add(new WarPlayer("Player 2"));
        setPlayers(players);

        dealCards();
    }

    /**
     * Builds a standard 52-card deck, shuffles it, and deals it out evenly
     * (alternating) to the two players.
     */
    private void dealCards()
    {
        ArrayList<Card> deck = new ArrayList<>(52);
        for (PlayingCard.Suit suit : PlayingCard.Suit.values())
        {
            for (int rank = 2; rank <= 14; rank++)
            {
                deck.add(new PlayingCard(rank, suit));
            }
        }

        GroupOfCards fullDeck = new GroupOfCards(52);
        fullDeck.addCards(deck);
        fullDeck.shuffle();

        WarPlayer p1 = (WarPlayer) getPlayers().get(0);
        WarPlayer p2 = (WarPlayer) getPlayers().get(1);

        boolean dealToFirst = true;
        while (!fullDeck.isEmpty())
        {
            Card card = fullDeck.removeCard();
            if (dealToFirst)
            {
                p1.getHand().addCard(card);
            }
            else
            {
                p2.getHand().addCard(card);
            }
            dealToFirst = !dealToFirst;
        }
    }

    @Override
    public void play()
    {
        WarPlayer p1 = (WarPlayer) getPlayers().get(0);
        WarPlayer p2 = (WarPlayer) getPlayers().get(1);

        while (p1.hasCards() && p2.hasCards() && roundNumber < MAX_ROUNDS)
        {
            roundNumber++;
            playRound(p1, p2);
        }

        declareWinner();
    }

    /**
     * Plays a single round: both players turn up their top card, and
     * whoever has the higher rank takes the pot (or a war breaks out).
     */
    private void playRound(WarPlayer p1, WarPlayer p2)
    {
        ArrayList<Card> pot = new ArrayList<>();

        p1.play();
        p2.play();
        PlayingCard c1 = p1.getCardInPlay();
        PlayingCard c2 = p2.getCardInPlay();
        pot.add(c1);
        pot.add(c2);

        if (verbose)
        {
            System.out.println("Round " + roundNumber + ": " + p1.getPlayerID() + " plays " + c1
                    + ", " + p2.getPlayerID() + " plays " + c2);
        }

        resolveComparison(p1, p2, c1, c2, pot);

        // Printed after the round (and any wars inside it) is fully
        // resolved, so the counts shown are the up-to-date totals.
        System.out.println("Round " + roundNumber + ": " + p1.getPlayerID() + ": " + p1.numCards()
                + " cards - " + p2.getPlayerID() + ": " + p2.numCards() + " cards");
    }

    /**
     * Compares the two face-up cards and either awards the pot or starts a war.
     */
    private void resolveComparison(WarPlayer p1, WarPlayer p2, PlayingCard c1, PlayingCard c2, ArrayList<Card> pot)
    {
        if (c1.getRank() > c2.getRank())
        {
            awardPot(p1, pot);
        }
        else if (c2.getRank() > c1.getRank())
        {
            awardPot(p2, pot);
        }
        else
        {
            war(p1, p2, pot);
        }
    }

    private void awardPot(WarPlayer winner, ArrayList<Card> pot)
    {
        if (verbose)
        {
            System.out.println("  -> " + winner.getPlayerID() + " takes " + pot.size() + " card(s).");
        }
        winner.winCards(pot);
    }

    /**
     * Handles a war: each player stakes up to 3 cards face down, then both
     * play one more card face up to decide who takes the whole pot. Another
     * tie recurses into another war.
     */
    private void war(WarPlayer p1, WarPlayer p2, ArrayList<Card> pot)
    {
        System.out.println("  -> WAR! (" + p1.getPlayerID() + " and " + p2.getPlayerID()
                + " each stake cards)");

        stake(p1, pot, 3);
        stake(p2, pot, 3);

        // If staking emptied someone's hand, the game will end naturally on
        // the next loop check, but let's hand the pot to whoever still has
        // cards left so it isn't just abandoned.
        if (!p1.hasCards() || !p2.hasCards())
        {
            if (p1.hasCards())
            {
                p1.winCards(pot);
            }
            else if (p2.hasCards())
            {
                p2.winCards(pot);
            }
            return;
        }

        p1.play();
        p2.play();
        PlayingCard c1 = p1.getCardInPlay();
        PlayingCard c2 = p2.getCardInPlay();
        pot.add(c1);
        pot.add(c2);

        if (verbose)
        {
            System.out.println("  War cards: " + p1.getPlayerID() + " plays " + c1
                    + ", " + p2.getPlayerID() + " plays " + c2);
        }

        resolveComparison(p1, p2, c1, c2, pot);
    }

    /**
     * Has a player stake up to n cards face down into the pot (fewer if
     * they don't have that many cards left).
     */
    private void stake(WarPlayer player, ArrayList<Card> pot, int n)
    {
        for (int i = 0; i < n && player.hasCards(); i++)
        {
            player.play();
            pot.add(player.getCardInPlay());
        }
    }

    @Override
    public void declareWinner()
    {
        WarPlayer p1 = (WarPlayer) getPlayers().get(0);
        WarPlayer p2 = (WarPlayer) getPlayers().get(1);

        System.out.println();
        if (!p1.hasCards())
        {
            System.out.println(p2.getPlayerID() + " wins after " + roundNumber + " rounds! ("
                    + p2.numCards() + " cards)");
        }
        else if (!p2.hasCards())
        {
            System.out.println(p1.getPlayerID() + " wins after " + roundNumber + " rounds! ("
                    + p1.numCards() + " cards)");
        }
        else
        {
            System.out.println("Round limit (" + MAX_ROUNDS + ") reached - calling it a draw.");
            System.out.println(p1.getPlayerID() + ": " + p1.numCards() + " cards, "
                    + p2.getPlayerID() + ": " + p2.numCards() + " cards");
        }
    }

    /**
     * Runs the game. Pass "-v" as an argument to see a full play-by-play log.
     */
    public static void main(String[] args)
    {
        boolean verbose = args.length > 0 && args[0].equalsIgnoreCase("-v");
        WarGame game = new WarGame(verbose);
        game.play();
    }

}//end class
