/**
 * This class models a Hand of Flush in a BigTwo card game.
 * <p>
 * Subclass of Hand.
 * 
 * @author Ajayveer Singh
 *
 */
public class Flush extends Hand{
	
	/**
	 * Constructor to create an object of type Flush.
	 * 
	 * @param player CardGamePlayer who is trying to play the Flush.
	 * @param cards CardList that the player wants to form a Flush with.
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * Method to check whether the Flush object is valid and represents a possible Flush.
	 * 
	 * @return true (boolean) if it is a valid Flush.<p>false (boolean) if it isn't a valid Flush.
	 */
	public boolean isValid() {
		if (this.size() != 5) return false;
		
		int suit = this.getCard(0).getSuit();
		for (int i = 0; i < 5; i++) {
			if (this.getCard(i).getSuit() != suit) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method that returns the type of an object of this class.
	 * 
	 * @return String "Flush".
	 */
	public String getType() {
		return "Flush";
	}
}
