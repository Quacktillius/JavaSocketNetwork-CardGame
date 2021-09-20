/**
 * Models a deck of BigTwoCard cards used in a BigTwo card game.
 * <p>
 * Subclass of Deck.
 * 
 * @author Ajayveer Singh
 *
 */
public class BigTwoDeck extends Deck {
	
	/**
	 * Overridden method initializes deck with BigTwoCard cards instead of Card cards.
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				addCard(new BigTwoCard(i,j));
			}
		}
	}
}
