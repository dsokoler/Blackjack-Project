
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
   
   public Card(){}
   
   public Card(int v){
      value = v;
   }
	
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
	
	//Returns the card's value as a string
	public String getStringValue() {
		String stringValue = "";
		if (this.value == 1 || this.value > 10) {
			switch (this.value) {
				case 11:
					stringValue = "J";
					break;
					
				case 12:
					stringValue = "Q";
					break;
					
				case 13:
					stringValue = "K";
					break;
					
				case 1:
					stringValue = "A";
					break;
					
				default:
					stringValue = " ";
					break;
			}
		}
		else stringValue = Integer.toString(this.value);
		return stringValue;
	}
}