/**
 * This class models a Hand of Single in a BigTwo card game.
 * <p>
 * Subclass of Hand.
 * 
 * @author Ajayveer Singh
 *
 */
public class Single extends Hand{
	
	/**
	 * Constructor to create an object of type Single.
	 * 
	 * @param player CardGamePlayer who is trying to play the Single.
	 * @param cards CardList that the player wants to form a Single with.
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * Method to check whether the Single object is valid and represents a possible Single.
	 * 
	 * @return true (boolean) if it is a valid Single.<p>false (boolean) if it isn't a valid Single.
	 */
	public boolean isValid() {
		if (this.size() == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method that returns the type of an object of this class.
	 * 
	 * @return String "Single"
	 */
	public String getType() {
		// Can use: return this.getClass().getSimpleName();
		return "Single";
	}
}
