import java.util.ArrayList;

public class Player {
	//player's current chip count
	int numChips;
	//indicates if player has busted on their hand or not
	boolean hasBusted;
	ArrayList<Card> playerHand;
	//If player receives two of the same card, have option to split and create another hand
	ArrayList<Card> splitHand;
		
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
	
	//TODO: Double check logic on this function, what about Aces being 1 or 11?
	//Are the face card values correct? I.E. jack = 11, etc.
	public int handValue() {
		int value = 0;
		for (Card card : playerHand) {
			value += card.value;
		}
		return value;
	}
	
	public void setCards(ArrayList<Card> set){ 
		playerHand = set; 
	}
	
	public void insertCard(Card c){ 
		playerHand.add(c);
	}
}
