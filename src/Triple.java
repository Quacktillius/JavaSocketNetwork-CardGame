/**
 * This class models a Hand of Triple in a BigTwo card game.
 * <p>
 * Subclass of Hand.
 * 
 * @author Ajayveer Singh
 *
 */
public class Triple extends Hand {
	
	/**
	 * Constructor to create an object of type Triple.
	 * 
	 * @param player CardGamePlayer who is trying to play the Triple.
	 * @param cards CardList that the player wants to form a Triple with.
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * Method to check whether the Triple object is valid and represents a possible Triple.
	 * 
	 * @return true (boolean) if it is a valid Triple.<p>false (boolean) if it isn't a valid Triple.
	 */
	public boolean isValid() {
		if (this.size() != 3) {
			return false;
		}
		
		// Rank of the first card
		int rank = this.getCard(0).getRank();
		for (int i = 0; i < 3; i++) {
			// Meaning of condition: It is not of the same rank as the first card. Over the course of the loop this equates to: All 3 are not of the same rank.
			if (this.getCard(i).getRank() != rank) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method that returns the type of an object of this class.
	 * 
	 * @return String "Triple".
	 */
	public String getType() {
		return "Triple";
	}
}
