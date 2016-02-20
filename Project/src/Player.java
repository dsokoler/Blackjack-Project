import java.util.ArrayList;

public class Player {
	//player's current chip count

	private int numChips;
	//indicates if player has busted on their hand or not
	private boolean hasBusted;
	private boolean isStaying;
	
	private ArrayList<Card> playerHand;

	//If player receives two of the same card, have option to split and create another hand
	ArrayList<Card> splitHand;
   
   String lastAction = "";
   String ID = "";
   
   public Player(){}
   public Player(String id){
      ID = id;
   }
		
    public Player(int startingChipCount){
    	this.numChips = startingChipCount;
    	this.hasBusted = false;
    	this.isStaying = false;
    	playerHand = new ArrayList<Card>();
    }
    
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
	
	public void setIsStaying(boolean set){
		isStaying = set;
	}
	
	public boolean getIsStaying(){
		return isStaying;
	}
	
	public ArrayList<Card> getHand() { 
		return playerHand; 
	}
	
	public static void split(Player player){
		//TODO
	}
	
	public void stay(){
		setIsStaying(true);
	}
	
	
	
	public void hit(Card card) {
		
		playerHand.add(card);
		
		if (handValue() > 21){
			setHasBusted(true);
		}	
	}
	
	//Aces assumed 11 unless bust, then are reverted to 1's as needed
	//Number cards are valued at face value
	//Face cards are all worth 10 with exception of the ace
	public int handValue() {
		int value = 0;
		int numAces = 0;
		for (Card card : playerHand) {
			int cardValue = card.getValue();
			if (card.value == 1) {
				numAces++;
				value += 11;
			}
			else if (cardValue > 10) value += 10;
			else value += cardValue;
		}
		while (numAces > 0) {
			if (value > 21 && numAces > 0) {
				value = (value - 11) + 1;
				numAces--;
			}
			else numAces--;
		}
      if(value > 21) setHasBusted(true);
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
   
   public String getID(){
      return ID;
   }
   
   public void setID(String s){
      ID = s;
   }
}
