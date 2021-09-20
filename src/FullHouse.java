/**
 * This class models a Hand of Full House in a BigTwo card game.
 * <p>
 * Subclass of Hand.
 * 
 * @author Ajayveer Singh
 *
 */
public class FullHouse extends Hand {
	
	/**
	 * Constructor to create an object of type FullHouse.
	 * 
	 * @param player CardGamePlayer who is trying to play the FullHouse.
	 * @param cards CardList that the player wants to form a FullHouse with.
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * Method to check whether the FullHouse object is valid and represents a possible FullHouse.
	 * 
	 * @return true (boolean) if it is a valid FullHouse.<p>false (boolean) if it isn't a valid FullHouse.
	 */
	public boolean isValid() {
		if (this.size() != 5) return false;
		
		
		int rank1 = this.getCard(0).getRank();
		int rank2 = -1;
		
		// To store triplet and pair
		CardList templist1 = new CardList();
		CardList templist2 = new CardList();
		
		// Get the second type of rank
		for (int i = 0; i < 5; i++) {
			if (this.getCard(i).getRank() != rank1) {
				rank2 = this.getCard(i).getRank();
				break;
			}
		}
		
		// Fill templist1 and templist2
		for (int i = 0; i < 5; i++) {
			if (this.getCard(i).getRank() == rank1) {
				templist1.addCard(this.getCard(i));
			} else if (this.getCard(i).getRank() == rank2) {
				templist2.addCard(this.getCard(i));
			}
		}
		
		if (templist1.size() == 3 && templist2.size() == 2 || templist1.size() == 2 && templist2.size() == 3) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method that returns the type of an object of this class.
	 * 
	 * @return String "FullHouse".
	 */
	public String getType() {
		return "FullHouse";
	}
}
