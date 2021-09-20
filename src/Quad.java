/**
 * This class models a Hand of Quad in a BigTwo card game.
 * <p>
 * Subclass of Hand.
 * 
 * @author Ajayveer Singh
 *
 */
public class Quad extends Hand {
	
	/**
	 * Constructor to create an object of type Quad.
	 * 
	 * @param player CardGamePlayer who is trying to play the Quad.
	 * @param cards CardList that the player wants to form a Quad with.
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * Method to check whether the Quad object is valid and represents a possible Quad.
	 * 
	 * @return true (boolean) if it is a valid Quad.<p>false (boolean) if it isn't a valid Quad.
	 */
	public boolean isValid() {
		if (this.size() != 5) return false;
		
		int rank1 = this.getCard(0).getRank();
		int rank2 = -1;
		
		// To store quadruple and single
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
		
		if (templist1.size() == 4 && templist2.size() == 1 || templist1.size() == 1 && templist2.size() == 4) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method that returns the type of an object of this class.
	 * 
	 * @return String "Quad".
	 */
	public String getType() {
		return "Quad";
	}
}
