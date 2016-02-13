
public class Card {
	
	/* 
	 * Card value guide:
	 * 1 = Ace
	 * 2-10 = Corresponding number card
	 * 11 = Jack
	 * 12 = Queen
	 * 13 = King
	 */
	
	int suit;
	int value;
	
	public int getSuit() { 
		return suit; 
	}
	
	public void setSuit(int set) { 
		suit = set; 
	}
	
	public int getValue() { 
		return value; 
	}
	
	public void setValue(int set) { 
		value = set; 
	}
}