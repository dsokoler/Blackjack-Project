import java.util.ArrayList;

public class Player {
	//player's current chip count
	int numChips;
	//indicates if player has busted on their hand or not
	boolean hasBusted;
	ArrayList<Card> playerHand;
	//If player receives two of the same card, have option to split and create another hand
	ArrayList<Card> splitHand;
   
    String lastAction = "";
		
	public int getNumChips() { 
		return numChips;
	}
	
	public void setNumChips(int set) { 
		numChips = set; 
	}
	
	public boolean getHasBusted() { 
		return hasBusted; 
	}
	
	public void setHasBusted(boolean set) { 
		hasBusted = set; 
	}
	
	public ArrayList<Card> getCards() { 
		return playerHand; 
	}
	
	//Aces assumed 11 unless bust, then are reverted to 1's as needed
	//Number cards are valued at face value
	//Face cards are all worth 10 with exception of the ace
	public int handValue() {
		int value = 0;
		int numAces = 0;
		for (Card card : playerHand) {
			System.out.println("Card Value: " + card.value);
			if (card.value == 1) {
				numAces++;
				value += 11;
			}
			if (card.value > 10) value += 10;
			else value += card.value;
		}
		while (numAces > 0) {
			if (value > 21 && numAces > 0) {
				value = (value - 11) + 1;
				numAces--;
			}
			else numAces--;
		}
		System.out.println("HAND VALUE RETURNS: " + value);
		return value;
	}
	
	public void setCards(ArrayList<Card> set){ 
		playerHand = set; 
	}
	
	public void insertCard(Card c){ 
		playerHand.add(c);
	}
   
   public String getLastAction(){
      return lastAction;
   }
   
   public void setLastAction(String set){
      lastAction = set;
   }
}
