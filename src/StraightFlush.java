/**
 * This class models a Hand of Straight Flush in a BigTwo card game.
 * <p>
 * Subclass of Hand.
 * 
 * @author Ajayveer Singh
 *
 */
public class StraightFlush extends Hand {
	
	/**
	 * Constructor to create an object of type Straight Flush.
	 * 
	 * @param player CardGamePlayer who is trying to play the Straight Flush.
	 * @param cards CardList that the player wants to form a Straight Flush with.
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * Method to check whether the Straight Flush object is valid and represents a possible Straight Flush.
	 * 
	 * @return true (boolean) if it is a valid Straight Flush.<p>false (boolean) if it isn't a valid Straight Flush.
	 */
	public boolean isValid() {
		if (this.size() != 5) return false;
		
		// Suit of the first card to compare to every other card's suit.
		int suit = this.getCard(0).getSuit();
		for (int i = 0; i < 5 - 1; i++) {
			int currentRank = this.getCard(i).getRank();
			currentRank = (currentRank - 2 < 0) ? 13 + currentRank - 2 : currentRank - 2;
			
			int nextRank = this.getCard(i + 1).getRank();
			nextRank = (nextRank - 2 < 0) ? 13 + nextRank - 2 : nextRank - 2;
			
			// Meaning of condition: They are not of consecutive ranks.
			if (nextRank - currentRank != 1) return false;
			// Meaning of condition: They are not of the same suit as the first card. Over the course of the loop this equates to: They are all not of the same suit.
			if (this.getCard(i).getSuit() != suit) return false;
		}
		return true;
	}
	
	/**
	 * Method that returns the type of an object of this class.
	 * 
	 * @return String "StraightFlush".
	 */
	public String getType() {
		return "StraightFlush";
	}
}
