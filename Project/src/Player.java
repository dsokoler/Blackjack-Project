import java.util.ArrayList;

public class Player {
	int numChips;
	Card one;
	Card two;
	public static ArrayList<Card> hand;
	
	public int getNumChips() { return numChips; }
	
	public void setNumChips(int set) { numChips = set; }
	
	public Card getCardOne() { return one; }
	
	public void setCardOne(Card set) { one = set; }
	
	public Card getCardTwo() { return two; }
	
	public void setCardTwo(Card set) { two = set; }
	
	public ArrayList<Card> getHand() { return hand; }
	
	public void setHand(ArrayList<Card> set) { hand = set; }
	
	public void insertHand(Card in) { hand.add(in); }
}
