import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Engine {
	//Globals
	static Scanner in;
	static ArrayList<Card> deck;
	static ArrayList<CPU> comps;
	static Player hooman;
	static int drawIndex;
	static int chipSetting;
	static int difficulty;
	static int numCPU;
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
	
	//Begins a round of play with all hoomans and comps
	public static void play() {
		//Print board
		printBoard();
		
		//Print player's hand
		printHand();
		
		//Prompt player for an action
		playerAction();
		
	}
	
	//Will print all the CPU's hands as should be viewed by the player
	//Meaning the first two cards are blank backs and all other cards are face up
	public static void printBoard() {
		//Loop for each CPU
		for (int i = 0; i < comps.size(); i++) {
			CPU curr = comps.get(i);
			System.out.println("Hand for CPU " + i + ": ");
			//Loop for each card in hand
			for (int j = 0; j < comps.size(); j++) {
				System.out.print(" _________ ");
			}
			System.out.println("");
			//Loop for each card in hand
			for (int j = 0; j < comps.size(); j++) {
				System.out.println("");
			}
		}
	}
	
	//Prints all the player's cards face up
	public static void printHand() {
		
	}
	
	//Prompts the player for an action, completes that action,
	//CPUs complete their turn, and everything is set up for the next round
	public static void playerAction() {
		
	}
	
	//Deals the cards to the player and all CPUs
	public static void deal() {
		for (int i = 0; i < comps.size(); i++) {
			comps.get(i).one = deck.get(drawIndex++);
			comps.get(i).two = deck.get(drawIndex++);
		}
		hooman.one = deck.get(drawIndex++);
		hooman.two = deck.get(drawIndex++);
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
		System.out.println("Current number of CPUs (Default: 3): " + numCPU);
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
					if (newNumCPU <= 0 || newNumCPU >= 8) System.out.println("I'm sorry, that wasnt a proper number of CPUs (0 < number < 8)");
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
		
		//Print menu
		System.out.println("Welcome to BlackJack!");
		System.out.println("Please enter a number cooresponding to an option.");
		System.out.println("1. Rules");
		System.out.println("2. Start Game");
		System.out.println("3. Settings");
		System.out.println("4. Exit");
		
		while (!check) {
			
			//Get input
			try {
				choice = in.nextInt();
				check = true;
			} catch (Exception e) {
				System.out.println("Improper Input");
				mm();
				return;
			}
		}
		
		//Switch based on input
		switch (choice) {
			case 1:
				rules();
				mm();
				break;
				
			case 2:
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
		}
		
		return;
	}
	
	public static void main(String args[]) {
		//Initialize globals to defaults
		in = new Scanner(System.in);
		numCPU = 3;
		chipSetting = 1000;
		difficulty = 0;
		comps = new ArrayList<CPU>();
		hooman = new Player();
		
		//Print start menu
		mm();
		
		//Code to start game rounds goes here
		play();
	}
}

class Card {
	int suit;
	int value;
	
	public int getSuit() { return suit; }
	
	public void setSuit(int set) { suit = set; }
	
	public int getValue() { return value; }
	
	public void setValue(int set) { value = set; }
}
