/**
 * This class models a Hand of Pair in a BigTwo card game.
 * <p>
 * Subclass of Hand.
 * 
 * @author Ajayveer Singh
 *
 */
public class Pair extends Hand{
	
	/**
	 * Constructor to create an object of type Pair.
	 * 
	 * @param player CardGamePlayer who is trying to play the Pair.
	 * @param cards CardList that the player wants to form a Pair with.
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * Method to check whether the Pair object is valid and represents a possible Pair.
	 * 
	 * @return true (boolean) if it is a valid Pair.<p>false (boolean) if it isn't a valid Pair.
	 */
	public boolean isValid() {
		if (this.size() != 2) {
			return false;
		}
		if (this.getCard(0).getRank() != this.getCard(1).getRank()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Method that returns the type of an object of this class.
	 * 
	 * @return String "Pair".
	 */
	public String getType() {
		// Can use: return this.getClass().getSimpleClassName();
		return "Pair";
	}
}
