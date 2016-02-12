import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Engine {
	//Globals
	static Scanner in;
	static ArrayList<Card> deck;
	static ArrayList<Player> comps;
	static Player hooman;
	static int drawIndex;
	static int chipSetting;
	static int difficulty;
	static int numCPU;
	
	static boolean doubleDown = false;
	static boolean surrender = false;
	
	static String[] club = {"|    _    | ",
							"|   (_)   | ",
							"|  (_)_)  | ",
							"|   /_\\   | ",
						 	"|         | "};
	static String[] diamond = {"|    ,    | ",
							   "|   / \\   | ",
							   "|  <   >  | ",
							   "|   \\ /   | ",
							   "|    '    | "};
	static String[] heart = {"|   _ _   | ",
							 "|  / ^ \\  | ",
							 "|  \\   /  | ",
							 "|   \\ /   | ",
					  		 "|    '    | "};
	static String[] spade = {"|    ,    | ",
					  		 "|   / \\   | ",
					  		 "|  (_ _)  | ",
					  		 "|   /_\\   | ",
					  		 "|         | "};
	static String[] empty = {"|{{{{{{{{{| ",
					  		 "|}}}}}}}}}| ",
					  		 "|{{{{{{{{{| ",
					  		 "|}}}}}}}}}| ",
					  		 "|{{{{{{{{{| ",
					  		 "|}}}}}}}}}| ",
					  		 "|{{{{{{{{{| "};
	static Boolean shutdown;
	
	//Begins a round of play with hooman and comps
	public static void play() {
		//Print board
		printBoard();
		
		//Print player's hand
		printHand();
		
		//Prompt player for an action
		playerAction();
		
		return;
	}
	
	//Returns the card's value as a string
	public static String getValue(Card input) {
		String value = "";
		if (input.value == 1 || input.value > 10) {
			switch (input.value) {
				case 11:
					value = "J";
					break;
					
				case 12:
					value = "Q";
					break;
					
				case 13:
					value = "K";
					break;
					
				case 1:
					value = "A";
					break;
					
				default:
					value = " ";
					break;
			}
		}
		else value = Integer.toString(input.value);
		return value;
	}
	
	//Will print all the CPU's hands as should be viewed by the player
	//Meaning the first two cards are blank backs and all other cards are face up
	public static void printBoard() {
		//Loop for each CPU
		for (int i = 0; i < comps.size(); i++) {
			Player curr = comps.get(i);
			int size;
			if (curr.cards.isEmpty()) size = 0;
			else size = curr.cards.size();
			System.out.println("Hand for CPU " + i + ": ");
			//Loop for each line to be printed
			for (int k = 0; k < 10; k++) {
				//Loop for each card in hand
				for (int j = 0; j < size; j++) {
					switch(k) {
						//First line
						case 0:
							System.out.print(" _________  ");
							break;
							
						//Second line
						case 1:
							System.out.print("/         \\ ");
							break;
							
						//Third line
						case 2:
							//If one of the first two cards, don't print the value
							if (j < 1) System.out.print("|{{{{{{{{{| ");
							else {
								if (!(getValue(curr.cards.get(j)).equals("10"))) System.out.print("|" + getValue(curr.cards.get(j)) + "        | ");
								else System.out.print("|" + getValue(curr.cards.get(j)) + "       | ");
							}
							break;
						
						//Second to last line
						case 8:
							//If one of the first two cards, don't print the value
							if (j < 1) System.out.print("|{{{{{{{{{| ");
							else {
								if (!(getValue(curr.cards.get(j)).equals("10"))) System.out.print("|        " + getValue(curr.cards.get(j)) + "| ");
								else System.out.print("|       " + getValue(curr.cards.get(j)) + "| ");
							}
							break;
							
						//Last line
						case 9:
							System.out.print("\\_________/ ");
							break;
						
						//All lines in between
						default:
							//If one of the first two cards, don't print the suit
							if (j < 1) System.out.print(empty[k-3]);
							else {
								switch(curr.cards.get(j).suit) {
									case 1:
										System.out.print(club[k-3]);
										break;
									
									case 2:
										System.out.print(diamond[k-3]);
										break;
										
									case 3:
										System.out.print(heart[k-3]);
										break;
										
									case 4:
										System.out.print(spade[k-3]);
										break;
										
									default:
										System.out.print(empty[k-3]);
										break;
								}
							}
					}
				}
				//Line is finished, move to next line
				System.out.println("");
			}
			//Aesthetic
			System.out.println("");
		}
	}
	
	//Prints all the player's cards face up
	public static void printHand() {
		int size;
		System.out.println("Your hand: ");
		if (hooman.cards.isEmpty()) size = 0;
		else size = hooman.cards.size();
		//Loop for each line to be printed
		for (int k = 0; k < 10; k++) {
			//Loop for each card in hand
			for (int j = 0; j < size; j++) {
				switch(k) {
					//First line
					case 0:
						System.out.print(" _________  ");
						break;
						
					//Second line
					case 1:
						System.out.print("/         \\ ");
						break;
						
					//Third line
					case 2:
						if (!(getValue(hooman.cards.get(j)).equals("10"))) System.out.print("|" + getValue(hooman.cards.get(j)) + "        | ");
						else System.out.print("|" + getValue(hooman.cards.get(j)) + "       | ");
						break;
					
					//Second to last line
					case 8:
						if (!(getValue(hooman.cards.get(j)).equals("10"))) System.out.print("|        " + getValue(hooman.cards.get(j)) + "| ");
						else System.out.print("|       " + getValue(hooman.cards.get(j)) + "| ");
						break;
						
					//Last line
					case 9:
						System.out.print("\\_________/ ");
						break;
					
					//All lines in between
					default:
						switch(hooman.cards.get(j).suit) {
							case 1:
								System.out.print(club[k-3]);
								break;
							
							case 2:
								System.out.print(diamond[k-3]);
								break;
								
							case 3:
								System.out.print(heart[k-3]);
								break;
								
							case 4:
								System.out.print(spade[k-3]);
								break;
								
							default:
								System.out.print(empty[k-3]);
								break;
						}
				}
			}
			//Line is finished, move to next line
			System.out.println("");
		}
		//Aesthetic
		System.out.println("");
		return;
	}
	
	//Plays the actions for all the computers in comps
	//TODO: Dan's territory
	public static void playComputers() {
		int hoomanFaceUp = hooman.cards.get(1).value;
		for (Player cpu : comps) {
			int total = cpu.handValue();
			System.out.println("CPU HAND VALUE: " + total);
			boolean handSizeOfTwo = (cpu.cards.size() == 2);
			if (handSizeOfTwo && cpu.cards.get(0).value == 1) {	//HOW DO WE DEFINE ACES AND FACE CARDS???
				System.out.println("");
				int action = cpu.softTotals[cpu.cards.get(1).value][hoomanFaceUp];
				//Use softTotals table
			}
			else if (handSizeOfTwo && cpu.cards.get(1).value == 1) {
				System.out.println("Ace as Card 2");
				int action = cpu.softTotals[cpu.cards.get(0).value][hoomanFaceUp];
			}
			else if (handSizeOfTwo && (cpu.cards.get(0).value == cpu.cards.get(1).value)) {
				//SPLIT
				System.out.println("SPLITTING");
			}
			else {
				//Use hardTotals table
				//0 is stay
				//1 is hit
				//2 is double down (if not allowed, then hit)
				//3 is double down (if not allowed, then stand)
				//4 is Split
				//5 is Surrender (if not allowed, then hit)
				int action = cpu.hardTotals[total-6][hoomanFaceUp];
				switch(action) {
					case 0:		//Stay
						System.out.println("STAY");
						break;
					case 1:		//Hit
						System.out.println("HIT");
						hit(cpu);
						break;
					case 2:		//Double down (if not allowed, then hit)
						System.out.println();
						if (doubleDown) {
							
						}
						else {
							hit(cpu);
						}
						break;
					case 3:		//Double down (if not allowed, then stand)
						System.out.println("DDS");
						if (doubleDown) {
							
						}
						else {
							
						}
						break;
					case 4:		//Split
						System.out.println("SPLIT");
						break;
					case 5:		//Surrender (if not allowed, then hit)
						System.out.println("SURRENDER");
						if (surrender) {
							
						}
						else {
							hit(cpu);
						}
						break;
				}
			}
		}
		System.out.println("Computers have made their move");
		return;
	}
	
	//Deal one card to the player, if bust, flag bust
	public static void hit(Player player) {
		player.cards.add(deck.get(drawIndex++));
		player.setBust(calcBust(player));
		return;
	}
	
	//Checks to see if the player has busted, returns 1 if bust, 0 if not
	//TODO
	public static int calcBust(Player player) {
		return 0;
	}
	
	//Prompts the player for an action, completes that action,
	//CPUs complete their turn, and everything is set up for the next round
	public static void playerAction() {
		Boolean raise = false;
		Boolean check = false;
		int input = 0;
		System.out.println("What would you like to do?");
		System.out.println("Please Enter the number cooresponding with the action you want to take:");
		System.out.println("1. Hit");
		System.out.println("2. Pass");
		System.out.println("3. PLACEHOLDER");
		System.out.println("4. Quit Game");
		while(!check) {
			try {
				input = in.nextInt();
				check = true;
			} catch (Exception e) {
				System.out.println("Please enter a valid option.");
			}
		}
		//TODO: get rid of the "raise = true;" lines when done testing 
		while(!raise) {
			switch(input) {
				case 1:
					System.out.println("You are dealt another card.");
					hit(hooman);
					playComputers();
					raise = true;
					break;
					
				case 2:
					System.out.println("You passed this round.");
					playComputers();
					raise = true;
					break;
					
				case 3:
					System.out.println("PLACEHOLDER DOES NOTHING, IT'S SUPER EFFECTIVE");
					break;
					
				case 4:
					shutdown();
					break;
			}
		}
	}
	
	//Deals the cards to the player and all CPUs
	public static void deal() {
		for (int i = 0; i < comps.size(); i++) {
			comps.get(i).insertCards(deck.get(drawIndex++));
			comps.get(i).insertCards(deck.get(drawIndex++));
		}
		hooman.insertCards(deck.get(drawIndex++));
		hooman.insertCards(deck.get(drawIndex++));
		return;
	}
	
	//Shuffles the deck
	public static void shuffle() {
		long seed = System.nanoTime();
		Collections.shuffle(deck, new Random(seed));
		return;
	}
	
	/*
	 * Creates a new deck in numerical order
	 * Suit guide:
	 * 1 = clubs
	 * 2 = diamonds
	 * 3 = hearts
	 * 4 = spades
	 * 
	 * Card value guide:
	 * 1 = Ace
	 * 2-10 = Corresponding number card
	 * 11 = Jack
	 * 12 = Queen
	 * 13 = King
	 */
	public static void initializeDeck() {
		deck = new ArrayList<Card>();
		drawIndex = 0;
		Card temp;
		//Loop for each suit
		for (int i = 1; i < 4; i++) {
			//Loop for each card
			for (int j = 1; j < 13; j++) {
				temp = new Card();
				temp.setSuit(i);
				temp.setValue(j);
				deck.add(temp);
			}
		}
		return;
	}
	
	//Set up each CPU and add them to the global list after initializing values
	public static void initializePlayers() {
		hooman.cards = new ArrayList<Card>();
		hooman.setBust(0);
		hooman.setNumChips(chipSetting);
		
		for (int i = 0; i < numCPU; i++) {
			Player temp = new Player();
			temp.setNumChips(chipSetting);
			temp.setBust(0);
			temp.cards = new ArrayList<Card>();
			comps.add(temp);
		}
		return;
	}
	
	//Setup the deck for the first time and shuffle the cards for a new game
	public static void start() {
		System.out.println("Setting up deck...");
		initializeDeck();
		System.out.println("Shuffling cards...");
		shuffle();
		System.out.println("Dealing hands...");
		deal();
		System.out.println("");
		return;
	}
	
	//Prints the rules
	public static void rules() {
		System.out.println("It's Blackjack, silly!");
		System.out.println("");
		return;
	}
	
	//Allows the player to change the game settings
	public static void settings() {
		System.out.println("Current starting chip amount (Default: 1,000): " + chipSetting);
		System.out.println("Difficulty Guideline: 0 - Easy, 1 - Medium, 2 - Hard, 3 - Insane");
		System.out.println("Current CPU Difficulty level (Default: 0): " + difficulty);
		System.out.println("Current number of CPUs (Default: 3, Min: 1, Max: 5): " + numCPU);
		while (true) {
			System.out.println("To change the starting chip amount type: 'chips (amount)' where (amount) is the number of chips you want to start with.");
			System.out.println("To change the difficulty level type: 'difficulty (level)' where (level) is the number cooresponding to the level of difficulty.");
			System.out.println("To change the number of CPUs typs: 'cpu (number)' where number is how many computers you wish to play against.");
			System.out.println("To return to the menu type: 'return'");
			System.out.println("");
			String input = in.nextLine();
			System.out.println("input: '" + input + "'");
			if (input.contains("difficulty")) {
				String level = input.substring(11);
				try {
					int newLevel = Integer.parseInt(level);
					if (newLevel < 0 || newLevel > 3) System.out.println("I'm sorry, that wasnt a proper difficulty setting (-1 < difficulty < 4)");
					else difficulty = newLevel;
				} catch (Exception e) {
					System.out.println("I'm sorry, that wasnt a proper difficulty setting (-1 < difficulty < 4)");
				}
			}
			else if (input.contains("chips")) {
				String amount = input.substring(6);
				try {
					int newAmount = Integer.parseInt(amount);
					if (newAmount <= 0 || newAmount >= 10000000) System.out.println("I'm sorry, that wasnt a proper chip amount (0 < amount < 10,000,000)");
					else chipSetting = newAmount;
				} catch (Exception e) {
					System.out.println("I'm sorry, that wasnt a proper chip amount (0 < amount < 10,000,000)");
				}
			}
			else if (input.contains("cpu")) {
				String numCPU = input.substring(4);
				try {
					int newNumCPU = Integer.parseInt(numCPU);
					if (newNumCPU <= 0 || newNumCPU >= 6) System.out.println("I'm sorry, that wasnt a proper number of CPUs (0 < number < 6)");
					else Engine.numCPU = newNumCPU;
				} catch (Exception e) {
					System.out.println("I'm sorry, that wasnt a proper number of CPUs (0 < number < 8)");
				}
				
			}
			else if (input.equals("return")) return;
			else System.out.println("I'm sorry, thats not a proper command.");
			System.out.println("");
		}
	}
	
	//Prints the main menu and allows input to traverse options
	public static void mm() {
		
		//Initialize locals
		Boolean check = false;
		int choice = 0;
		
		while (!check) {
      
	         //Print menu
	   		System.out.println("Welcome to BlackJack!");
	   		System.out.println("Please enter a number cooresponding to an option.");
	   		System.out.println("1. Rules");
	   		System.out.println("2. Start Game");
	   		System.out.println("3. Settings");
	   		System.out.println("4. Exit");
				
			//Get input
			try {
	            String input = in.nextLine();
	            input = input.replace("\n", "");
	            choice = Integer.parseInt(input);
				//choice = in.nextInt();
				check = true;
			} catch (Exception e) {
				System.out.println("Invalid Input: Input must be a number");
				mm();
				return;
			}
			
	   		//Switch based on input
	   		switch (choice) {
	   			case 1:
	   				rules();
	   				mm();
	   				break;
	   				
	   			case 2:
	   				initializePlayers();
	   				start();
	   				break;
	   				
	   			case 3:
	   				settings();
	   				mm();
	   				break;
	   				
	   			case 4:
	   				System.out.println("Good-Bye!");
	   				in.close();
	   				System.exit(0);
	   				break;
	   				
	   			default:
	               System.out.println("Invalid input: Input must be in range [1-4].");
	               check = false;
	   		}// END Switch
      
      }// END while(!check)
		
		return;
	}
	
	public static void shutdown() {
		in.close();
		System.out.println("Bye-Bye!");
		System.exit(0);	
	}
	
	public static void main(String args[]) {
		//Initialize globals to defaults
		in = new Scanner(System.in);
		numCPU = 3;
		chipSetting = 1000;
		difficulty = 0;
		comps = new ArrayList<Player>();
		hooman = new Player();
		shutdown = false;
		
		//Print start menu
		mm();
		
		//Code to start game rounds goes here
		while (!shutdown) {
			play();
		}
	}
}

/* 
 * Card value guide:
 * 1 = Ace
 * 2-10 = Corresponding number card
 * 11 = Jack
 * 12 = Queen
 * 13 = King
 */
class Card {
	int suit;
	int value;
	
	public int getSuit() { return suit; }
	
	public void setSuit(int set) { suit = set; }
	
	public int getValue() { return value; }
	
	public void setValue(int set) { value = set; }
}
