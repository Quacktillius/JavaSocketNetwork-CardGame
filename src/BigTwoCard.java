/**
 * Models a card used in a Big Two card game.
 * <p>
 * Subclass of Card.
 * 
 * @author Ajayveer Singh
 *
 */
public class BigTwoCard extends Card{
	
	/**
	 * Constructor to create a BigTwo Card object.
	 * 
	 * @param suit int specifying the index that represents the suit of the BigTwo card.
	 * @param rank int specifying the index that represents the rank of the BigTwo card.
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	/**
	 * Method to compare the calling Card with the Card being passed.
	 * 
	 * @param card Card to be compared to the calling card.
	 * 
	 * @return 1 (int) if a Single of calling Card will be ranked higher than the passed Card.<p>
	 * 			0 (int) if they're the same.<p>
	 * 			-1 (int) if Single of passed Card will be ranked higher than the calling Card.
	 */
	public int compareTo(Card card) {
		// Correct the ranks according to Big Two rules and carry out same comparison as original method.
		int thisRank = (this.rank - 2 < 0) ? 13 + this.rank - 2 : this.rank - 2;
		int cardRank = (card.rank - 2 < 0) ? 13 + card.rank - 2 : card.rank - 2;
		
		if (thisRank > cardRank) {
			return 1;
		} else if (thisRank < cardRank) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}
