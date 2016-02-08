import java.util.ArrayList;

public class CPU {
	int numChips;
	int count;		//Used for CPU that card counts
	Card one;
	Card two;
	ArrayList<Card> cards;	//11 is the maximum number of cards possible in one hand
	
	public double[][] systems = {
		/*
		 * T: Cards with value 10 (10, J, Q, K)
		 * BC: Betting Correlation: effectiveness of the system (the higher the better)
		 * PE: Playing Efficiency: effectiveness of a system's determination of correct playing variations (the higher the better)
		 * IC: Insurance Correlation: 
		 * Bal: Balanced or Unbalanced: 
		//				  2  3  4    5  6   7  8    9   T  Ace  BC   PE   IC Bal?
		/*Hi-Lo*/		{ 1, 1, 1,   1, 1,  0, 0,   0, -1, -1, .97, .51, .76, 1},
		/*K-O*/			{ 1, 1, 1,   1, 1,  1, 0,   0, -1, -1, .98, .55, .78, 0},
		/*Hi-Opt 1*/	{ 0, 1, 1,   1, 1,  0, 0,   0, -1,  0, .88, .61, .85, 1},
		/*Hi-Opt 2*/	{ 1, 1, 2,   2, 1,  1, 0,   0, -1,  0, .91, .67, .91, 1},
		/*Halves*/		{.5, 1, 1, 1.5, 1, .5, 0, -.5, -1, -1, .99, .56, .72, 1},
		/*Omega II*/	{ 1, 1, 2,   2, 2,  1, 0,  -1, -2,  0, .92, .67, .86, 1},
		/*Zen*/			{ 1, 1, 2,   2, 2,  1, 0,   0, -1, -1, .96, .63, .85, 1}

	};
	
	public int[][] cpuStrats = {
		//	 		  2  3  4    5  6   7  8    9   T  Ace
		/*Easy*/	{ 1, 1, 1,   0, 0,  0, 0,   0,  0, -1},
		/*Medium*/	{ 1, 1, 1,   0, 0,  0, 0,   0, -1, -1},
		/*Hard*/	{ 1, 1, 1,   1, 1,  0, 0,   0, -1, -1},
	};
	
	//This is what the average player will do (y axis is player hand, x axis is dealer's visible card)
	//0 is stay
	//1 is hit
	//2 is double down (if not allowed, then hit)
	//3 is double down (if not allowed, then stand)
	//4 is Split
	//5 is Surrender (if not allowed, then hit)
	// **Double down means to increase the initial bet by up to 100% in exchange for committing to stand after receiving exactly one more card
	public int[][] hardTotals = {
		//	 	 2  3  4  5  6  7  8  9 10  J  Q  K  A
		/*21*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		/*20*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		/*19*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		/*18*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		/*17*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		/*16*/	{0, 0, 0, 0, 0, 1, 1, 5, 5, 5, 5, 5, 5},
		/*15*/	{0, 0, 0, 0, 0, 1, 1, 1, 5, 5, 5, 5, 1},
		/*14*/	{0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
		/*13*/	{0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
		/*12*/	{1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
		/*11*/	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
		/*10*/	{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1},
		/*9*/	{1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1},
		/*8*/	{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		/*7*/	{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		/*6*/	{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		/*5*/	{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};
	
	public int[][] softTotals = {
		//	 	 2  3  4  5  6  7  8  9 10  J  Q  K  A
		/*A,8*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		/*A,9*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		/*A,7*/	{0, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1},
		/*A,6*/	{1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1},
		/*A,5*/	{1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1},
		/*A,4*/	{1, 1, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1},
		/*A,3*/	{1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1},
		/*A,2*/	{1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1}
	};
	
	public int[][] pairs = {
		//	 	 2  3  4  5  6  7  8  9 10  J  Q  K  A
		/*A*/	{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
		/*K*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		/*Q*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		/*J*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		/*10*/	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
		/*9*/	{4, 4, 4, 4, 4, 0, 4, 4, 0, 0, 0, 0, 0},
		/*8*/	{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
		/*7*/	{4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1},
		/*6*/	{4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1},
		/*5*/	{2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1},
		/*4*/	{1, 1, 1, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1},
		/*3*/	{4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1},
		/*2*/	{4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1},
	};
	public int getNumChips() { return numChips; }
	
	public void setNumChips(int set) { numChips = set; }
	
	public Card getCardOne() { return one; }
	
	public void setCardOne(Card set) { one = set; }
	
	public Card getCardTwo() { return two; }
	
	public void setCardTwo(Card set) { two = set; }
	
	//Plays an easy difficulty turn for the CPU
	public static void easy(CPU input) {
		
	}
	
	//Plays a medium difficulty turn for the CPU
	public static void medium(CPU input) {
		
	}
	
	//Plays a hard difficulty turn for the CPU
	public static void hard(CPU input) {
		
	}
	
	//Plays an insane difficulty turn for the CPU
	public static void insane(CPU input) {
		
	}
	
	//Calculate the value of the current hand of a CPU given the current state of the board
	public static int calculateHandValue(CPU input) {
		return 0;
	}
}