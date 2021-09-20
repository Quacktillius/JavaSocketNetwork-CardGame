
/**
 * Abstract class that models a hand of cards.
 * 
 * @author Ajayveer Singh
 *
 */
public abstract class Hand extends CardList{
	
	private CardGamePlayer player;
	
	/**
	 * Constructor to create an object of this class.
	 * 
	 * @param player CardgamePlayer who is trying to form/play the hand.
	 * @param cards CardList that the player wants to form the hand with.
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for(int i = 0; i < cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
	}
	
	/**
	 * Getter method for instance variable 'CardGamePlayer player';
	 * 
	 * @return CardGamePlayer player who played/is trying to play the hand.
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	
	/**
	 * Method that returns the top card of the Hand.
	 * 
	 * @return Card that specifies the top card of this Hand object.
	 */
	public Card getTopCard() {
		if (this.getType() == "FullHouse" || this.getType() == "Quad") {
			CardList templist1 = new CardList();
			CardList templist2 = new CardList();
			
			// Full House and Quad have 2 ranks.
			int rank1 = this.getCard(0).getRank();
			int rank2 = -1;
			
			for (int i = 0; i < 5; i++) {
				if (this.getCard(i).getRank() != rank1) {
					rank2 = this.getCard(i).getRank();
				}
			}
			
			for (int i = 0; i < 5; i++) {
				if (this.getCard(i).getRank() == rank1) {
					templist1.addCard(this.getCard(i));
				} else if (this.getCard(i).getRank() == rank2) {
					templist2.addCard(this.getCard(i));
				}
			}
			
			// Default is FullHouse
			int sizeOfLargestListWithCommonRank = 3;
			if (this.getType() == "Quad") {
				sizeOfLargestListWithCommonRank = 4;
			}
			
			// Returns the 'strongest' card from the larger list (quadruple or triplet depending on FullHouse or Quad)
			if (templist1.size() == sizeOfLargestListWithCommonRank) {
				templist1.sort();
				return templist1.getCard(sizeOfLargestListWithCommonRank - 1);
			} else {
				templist2.sort();
				return templist2.getCard(sizeOfLargestListWithCommonRank - 1);
			}
		}
		
		// For hands other than Quad and Full House
		this.sort();
		return this.getCard(this.size() - 1);
	}
	
	/**
	 * Abstract method to check whether this Hand object is a valid hand.
	 * <p>
	 * Will be implemented in subclasses.
	 * 
	 * @return true (boolean) if it is a valid hand.<p>false (boolean) if it is not valid.<p>Note it does not have an implementation
	 * 			in this class and so its return value depends on the subclass which inherits from it. The previously mentioned
	 * 			behavior was simply the expected return value.
	 */
	public abstract boolean isValid();
	/**
	 * Abstract method that 'gets' or returns the type of Hand object.
	 * 
	 * @return String value specifying the type of the subclass of Hand. ie. Type of the Hand object.<p>Note it does
	 * 			not have an implementation in this class and so its return value depends on the subclass which inherits
	 * 			from it . The previously described behavior was simply the expected return value.
	 */
	public abstract String getType();
	
	/**
	 * Method that checks whether the calling Hand object beats the passed Hand object.
	 * 
	 * @param hand Hand object to which the calling Hand object is compared.
	 * @return true (boolean) if the calling Hand does beat the passed Hand.<p>
	 * 			false (boolean) if the calling Hand does not beat the passed Hand.
	 */
	public boolean beats(Hand hand) {
		if (!this.isValid()) return false;
		if (!hand.isValid()) return false;
		if (this.size() != hand.size()) return false;
		
		// Getting the top cards of the calling Hand and the passed Hand. Also get corrected ranks for top cards.
		Card thisTopCard = this.getTopCard();
		int thisTopCardRank = thisTopCard.getRank();
		thisTopCardRank = (thisTopCardRank - 2 < 0) ? 13 + thisTopCardRank - 2 : thisTopCardRank - 2;
		
		Card handTopCard = hand.getTopCard();
		int handTopCardRank = handTopCard.getRank();
		handTopCardRank = (handTopCardRank - 2 < 0) ? 13 + handTopCardRank - 2 : handTopCardRank - 2;
		
		if (hand.size() == 1) {
			return thisTopCard.compareTo(handTopCard) == 1;
		} else if (hand.size() == 2) {
			if (thisTopCardRank > handTopCardRank) {
				return true;
			} else if (thisTopCardRank < handTopCardRank) {
				return false;
			} else if (thisTopCard.getSuit() > handTopCard.getSuit()) {
				return true;
			} else {
				return false;
			}
		} else if (hand.size() == 3) {
			if (thisTopCardRank > handTopCardRank) {
				return true;
			} else {
				return false;
			}
		} else if (hand.size() == 5) {
			if (this.getType() == hand.getType()) {
				if (this.getType() == "Flush") {
					if (thisTopCard.getSuit() > handTopCard.getSuit()) {
						return true;
					} else if (thisTopCard.getSuit() < handTopCard.getSuit()) {
						return false;
					} else {
						// Will return true if thisTopCard is of a higher rank
						return thisTopCard.compareTo(handTopCard) == 1;
					}
				} else {
					return thisTopCard.compareTo(handTopCard) ==  1;
				}
			} else {
				if (this.getType() == "Straight Flush") {
					return true;
				} else if (this.getType() == "Quad") {
					if (hand.getType() != "Straight Flush") {
						return true;
					} else {
						return false;
					}
				} else if (this.getType() == "Full House") {
					if (hand.getType() == "Flush" || hand.getType() == "Straight") {
						return true;
					} else {
						return false;
					}
				} else if (this.getType() == "Flush") {
					if (hand.getType() == "Straight") {
						return true;
					} else {
						return false;
					}
				} else if (this.getType() == "Straight") {
					return false;
				}
			}
		}
		return false;
	}
}
