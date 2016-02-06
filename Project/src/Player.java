public class Player {
	int numChips;
	Card one;
	Card two;
	Card cards[] = new Card[11];	//11 is the maximum number of cards possible in one hand
	
	public int getNumChips() { return numChips; }
	
	public void setNumChips(int set) { numChips = set; }
	
	public Card getCardOne() { return one; }
	
	public void setCardOne(Card set) { one = set; }
	
	public Card getCardTwo() { return two; }
	
	public void setCardTwo(Card set) { two = set; }
}
