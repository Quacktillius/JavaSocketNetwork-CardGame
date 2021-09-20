/**
 * This class models a Hand of Straight in a BigTwo card game.
 * <p>
 * Subclass of Hand.
 * 
 * @author Ajayveer Singh
 *
 */
public class Straight extends Hand{
	
	/**
	 * Constructor to create an object of type Straight.
	 * 
	 * @param player CardGamePlayer who is trying to play the Straight.
	 * @param cards CardList that the player wants to form a Straight with.
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * Method to check whether the Straight object is valid and represents a possible Straight.
	 * 
	 * @return true (boolean) if it is a valid Straight.<p>false (boolean) if it isn't a valid Straight.
	 */
	public boolean isValid() {
		
		if (this.size() != 5) return false;
		
		this.sort();
		
		// Compare each card to the next
		for (int i = 0; i < 5 - 1; i++) {
			int currentRank = this.getCard(i).getRank();
			currentRank = (currentRank - 2 < 0) ? 13 + currentRank - 2 : currentRank - 2;
			
			int nextRank = this.getCard(i + 1).getRank();
			nextRank = (nextRank - 2 < 0) ? 13 + nextRank - 2 : nextRank - 2;
			
			// Meaning of condition: They are not of consecutive ranks.
			if (nextRank - currentRank != 1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method that returns the type of an object of this class.
	 * 
	 * @return String "Straight".
	 */
	public String getType() {
		return "Straight";
	}
}
