import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Engine {
	//Globals
	static Scanner in;
	static ArrayList<Card> deck;
	static ArrayList<Player> computers;
	static ArrayList<Player> winners; // Used to store which players have won. ArrayList in case of a tie
	static Player human;
	static String playerDisplayName;
	static Player dealer;
	static int drawIndex;
	static int startingChipCount;
	static int difficulty;
	static int numCPU;
	static int chipPot = 0;               // Sum of all chips bet for the current game

	static final int CPU_EASY = 0;
	static final int CPU_MEDIUM = 1;
	static final int CPU_HARD = 2;
	static final int CPU_INSANE = 3;

	static final int MIN_CPU_COUNT = 1;
	static final int MAX_CPU_COUNT = 5;

	static final int DEFAULT_CHIP_SETTING = 500;
	static final int DEFAULT_CPU_COUNT_SETTING = 3;
	static final int DEFAULT_CPU_DIFFICULTY_SETTING = CPU_EASY;

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


	//Begins a round of play with human and computers
	public static void playGame() {

      //Print board
		printBoard();
      
      //Print player's hand
		printHand();

		while (handIsOver() == false){
			printHand();

			playerAction();			
			computerAction();			
			dealerAction();
      
			printBoard();
		}
      
		determineWinners();
		splitWinnings();
      
		//hand has been won
      	askForNewRound();
	}

   private static void splitWinnings(){

      int payout = 0;
      if(winners.size() > 0)
         payout = (int)(chipPot / winners.size());
      for(Player player : winners){
         player.setNumChips(player.getNumChips() + payout);
         System.out.print("Player " + player.getDisplayName() + " has won " + payout + " chips!");

      }
   }

	private static void askForNewRound() {
		System.out.println("The hand is over, would you like to :\n"
				+ "1. Play another game\n"
				+ "2. Return to main menu\n"
				+ "3. Quit"
				);
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();

		if (input.equals("1")){
			resetGame();
         initializeGame();
			playGame();
		}
		else if (input.equals("2")){
			mainMenu();
		}
		else if (input.equals("3")){
			quit();
		}
		else{
			System.out.println("Invalid Input");
			askForNewRound();
		}
	}	




	//Will print all the CPU's hands as should be viewed by the player
	//Meaning the first two cards are blank backs and all other cards are face up
	public static void printBoard() {
		//Loop for each CPU
		for (int i = 0; i < computers.size(); i++) {
			Player currentPlayer = computers.get(i);
			int size;

			if (currentPlayer.getHand().isEmpty()) size = 0;
			else size = currentPlayer.getHand().size();
			System.out.println("Hand for CPU " + i + ": ");

			//Loop for each line to be printed
			for (int line = 0; line < 10; line++){

				//Loop for each card in hand
				for (int cardIndex = 0; cardIndex < size; cardIndex++) {
					Card currentCard = currentPlayer.getHand().get(cardIndex);
					String currentCardValue = currentCard.getStringValue();

					switch(line) {
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
						if (cardIndex < 1) System.out.print("|{{{{{{{{{| ");
						else {
							if (!(currentCardValue.equals("10"))) System.out.print("|" + currentCardValue + "        | ");
							else System.out.print("|" + currentCardValue + "       | ");
						}
						break;

						//Second to last line
					case 8:
						//If one of the first two cards, don't print the value
						if (cardIndex < 1) System.out.print("|{{{{{{{{{| ");
						else {
							if (!(currentCardValue.equals("10"))) System.out.print("|        " + currentCardValue + "| ");
							else System.out.print("|       " + currentCardValue + "| ");
						}
						break;

						//Last line
					case 9:
						System.out.print("\\_________/ ");
						break;

						//All lines in between
					default:
						//If one of the first two cards, don't print the suit
						if (cardIndex < 1) System.out.print(empty[line-3]);
						else {
							switch(currentCard.suit) {
							case 1:
								System.out.print(club[line-3]);
								break;

							case 2:
								System.out.print(diamond[line-3]);
								break;

							case 3:
								System.out.print(heart[line-3]);
								break;

							case 4:
								System.out.print(spade[line-3]);
								break;

							default:
								System.out.print(empty[line-3]);
								break;
							}// END inner switch
						}// END else
					}// END switch(line)
				}// END for(line)
				//Line is finished, move to next line
				System.out.println("");
			}// END for(computers)
			//Aesthetic
			System.out.println("");
		}
	}

	//Prints all the player's cards face up
	public static void printHand() {
		int size;

		System.out.println("Your hand: ");
		if (human.getHand().isEmpty()) size = 0;
		else size = human.getHand().size();

		//Loop for each line to be printed
		for (int line = 0; line < 10; line++) {
			//Loop for each card in hand
			for (int cardIndex = 0; cardIndex < size; cardIndex++) {

				Card currentCard = human.getHand().get(cardIndex);
				String currentCardValue = currentCard.getStringValue();

				switch(line) {
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
					if (!(currentCardValue.equals("10"))) System.out.print("|" + currentCardValue + "        | ");
					else System.out.print("|" + currentCardValue + "       | ");
					break;

					//Second to last line
				case 8:
					if (!(currentCardValue.equals("10"))) System.out.print("|        " + currentCardValue + "| ");
					else System.out.print("|       " + currentCardValue + "| ");
					break;

					//Last line
				case 9:
					System.out.print("\\_________/ ");
					break;

					//All lines in between
				default:
					switch(currentCard.suit) {
					case 1:
						System.out.print(club[line-3]);
						break;

					case 2:
						System.out.print(diamond[line-3]);
						break;

					case 3:
						System.out.print(heart[line-3]);
						break;

					case 4:
						System.out.print(spade[line-3]);
						break;

					default:
						System.out.print(empty[line-3]);
						break;
					}
				}
			}
			//Line is finished, move to next line
			System.out.println("");
		}
		//Aesthetic
		System.out.println("");
		System.out.println("Number of remaining chips: " + human.getNumChips());
		System.out.println("");
		return;
	}

	//Plays the actions for all the computers in comps
	//TODO: Dan's territory
	public static void computerAction() {
		int humanHandValue = human.getHand().get(1).value - 1;
		for (Player cpu : computers) {
			int total = cpu.handValue();

			int cardOne = cpu.getHand().get(0).getValue();
			int cardTwo = cpu.getHand().get(1).getValue();
			//System.out.println("CPU " + cpu.getID() + " HAND VALUE: " + total);

			//System.out.println("CPU CARDS: " + cardOne + " " + cardTwo);

			// Check if the player has busted
			// if yes, skip the player

			if(cpu.getHasBusted()){
            cpu.setLastAction("stay");
            continue;
         }


			boolean handSizeOfTwo = (cpu.getHand().size() == 2);
			if (handSizeOfTwo && cpu.getHand().get(0).value == 1) {
				if (cardOne <= 8) {
					int action = LookupTables.softTotals[cardTwo - 2][humanHandValue - 1];
				}
				//Use softTotals table
			}

			else if (handSizeOfTwo && cpu.getHand().get(1).value == 1) {
				System.out.println("Ace as Card 2: [" + cardOne + "][" + humanHandValue + "]");

				if (cardOne <= 8) {
					int action = LookupTables.softTotals[cardOne - 2][humanHandValue - 1];
				}
			}
			else if (handSizeOfTwo && (cpu.getHand().get(0).value == cpu.getHand().get(1).value)) {
				//SPLIT
				System.out.println("SPLITTING");
			}
			else {
				/*
				Use hardTotals table
				0 is stay
				1 is hit
				2 is double down (if not allowed, then hit)
				3 is double down (if not allowed, then stand)
				4 is Split
				5 is Surrender (if not allowed, then hit)
				 */
				int action = LookupTables.hardTotals[total-6][humanHandValue];
				Card card;
				switch(action) {
				case 0:		//Stay
					System.out.println("STAY");
               cpu.setLastAction("stay");
					break;
				case 1:		//Hit
					System.out.println("HIT");
					card = deck.get(drawIndex++);
					cpu.hit(card);
					cpu.setLastAction("hit");

					break;
				case 2:		//Double down (if not allowed, then hit)
					System.out.println();
					if (doubleDown) {
                  cpu.setLastAction("stay");
					}
					else {

						card = deck.get(drawIndex++);
						cpu.hit(card);
						cpu.setLastAction("hit");

					}
					break;
				case 3:		//Double down (if not allowed, then stand)
					System.out.println("DDS");
					if (doubleDown) {
                  cpu.setLastAction("stay");
					}
					else {
                  cpu.setLastAction("stay");
					}
					break;
				case 4:		//Split
					System.out.println("SPLIT");
               cpu.setLastAction("stay");
					break;
				case 5:		//Surrender (if not allowed, then hit)
					System.out.println("SURRENDER");
					if (surrender) {
                  cpu.setLastAction("stay");
					}
					else {

						card = deck.get(drawIndex++);
						cpu.hit(card);
                  cpu.setLastAction("hit");

					}
					break;
             default:
               cpu.setLastAction("stay");
				}
			}
         System.out.println("CPU " + cpu.getDisplayName() + " chose to " + cpu.getLastAction());
		}
		System.out.println("Computers have made their move");
		return;
	}
	
	public static void dealerAction(){
		if(dealer.getIsStaying() == true || dealer.getHasBusted() == true){
			return;
		}
		else if(dealer.handValue() < 17){
			Card card = deck.get(drawIndex++);
			dealer.hit(card);
		}
		else{
			dealer.stay();
		}
	}

	public static void determineWinners(){
		
		// Check if all players have busted
		boolean allBust = true;
		if(human.getHasBusted() == false){   
			allBust = false;
		}
		for(int i = 0; i <  computers.size(); i++){
			if(computers.get(i).getHasBusted() == false){
				allBust = false;
			}
		}
		if (dealer.getHasBusted() == false){
			allBust = false;
		}
		
		//Dealer wins if everyone busts
		if(allBust){
			winners.add(dealer);
			return;
		}
      
		//first, determine the highest hand value that is 21 or under
      int highestNonBust = 0;
      
      if (human.getHasBusted() == false && human.handValue() > highestNonBust){
    	  highestNonBust = human.handValue();
      }
      
      if (dealer.getHasBusted() == false && dealer.handValue() > highestNonBust){
    	  highestNonBust = human.handValue();
      }
      
      for (int i = 0; i < numCPU; i++){
    	  Player computer = computers.get(i);
    	  
          if (computer.getHasBusted() == false && computer.handValue() > highestNonBust){
        	  highestNonBust = computer.handValue();
          }
      }
      
      //Now find all hand(s) that have that value and add them to winners pool
      if (human.handValue() == highestNonBust){
    	  winners.add(human);
      }
      
      if (dealer.handValue() == highestNonBust){
    	  winners.add(dealer);
      }
      
      for (int i = 0; i < numCPU; i++){
    	  Player computer = computers.get(i);
    	  
          if (computer.handValue() == highestNonBust){
        	  winners.add(computer);
          }
      }
      

      //5 cards in hand rule : might add later
      /*
		// Check if any single player has 5 cards no bust --> Automatic win
		if(human.getHand().size() >= 5 && human.handValue() <= 21){
			winners.add(human);
			return true;
		}
		for(int i = 0; i < computers.size(); i++){
			if(computers.get(i).getHand().size() >= 5 && computers.get(i).handValue() <= 21){
				winners.add(computers.get(i));
				return true;
			}
		}
		*/

	}
	
	//TODO
	public static boolean handIsOver() {

		//start with assumption of true, systematically go through each player to prove true/false
		boolean isHandOver = true;
		
		if (human.getHasBusted() == false && human.getIsStaying() == false){
			isHandOver = false;
		}
		
		if (dealer.getHasBusted() == false && dealer.getIsStaying() == false){
			isHandOver = false;
		}
		
		for (int i = 0; i < computers.size(); i++){
			Player computer = computers.get(i);
			if (computer.getHasBusted() == false && computer.getIsStaying() == false){
				isHandOver = false;
			}
		}
		
		return isHandOver;	
	}

	//Prompts the player for an action, completes that action,
	//CPUs complete their turn, and everything is set up for the next round
	public static void playerAction() {
		boolean raise = false;
		boolean check = false;
		boolean bust = human.getHasBusted();
		int input = 0;
		/*if(bust){
			System.out.println("Sorry, you have busted.");
			System.out.println("Please Enter the number corresponding with the action you want to take:");
			System.out.println("1. Continue");
			System.out.println("2. Quit Game");
		}else{
			System.out.println("What would you like to do?");
			System.out.println("Please Enter the number corresponding with the action you want to take:");
			System.out.println("1. Hit");
			System.out.println("2. Stay");
			System.out.println("3. PLACEHOLDER");
			System.out.println("4. Quit Game");
		}*/
		while(!check) {
         check = true;
      
         System.out.println("What would you like to do?");
			System.out.println("Please Enter the number cooresponding with the action you want to take:");
			System.out.println("1. Hit");
			System.out.println("2. Stay");
			System.out.println("3. PLACEHOLDER");
			System.out.println("4. Quit Game");
      
			try {
				input = in.nextInt();
			} catch (Exception e) {
				System.out.println("Please enter a valid option.");
			}
		
			switch(input) {
			case 1:
				if(bust){
					System.out.println("You cannot hit after you have already busted.");
               check = false;
				}else{
					System.out.println("You are dealt another card.");
					Card card = deck.get(drawIndex++);
					human.hit(card);
				}
				break;

			case 2:
					System.out.println("You passed this round.");

				break;

			case 3:
				System.out.println("PLACEHOLDER DOES NOTHING, IT'S SUPER EFFECTIVE");
				break;

			case 4:
				quit();
				break;
         
         default:
            check = false;
			}
		}

	}

	//Deals the cards to the player and all CPUs
	public static void deal() {
		for (int i = 0; i < computers.size(); i++) {
			//draw two cards
			computers.get(i).insertCard(deck.get(drawIndex++));
			computers.get(i).insertCard(deck.get(drawIndex++));
		}
		//draw two cards
		human.insertCard(deck.get(drawIndex++));
		human.insertCard(deck.get(drawIndex++));
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
		Card card;
		//Loop for each suit
		for (int i = 1; i < 4; i++) {
			//Loop for each card
			for (int j = 1; j < 13; j++) {
				card = new Card();
				card.setSuit(i);
				card.setValue(j);
				deck.add(card);
			}
		}
		return;
	}

	//Set up each CPU
	public static void initializeCPU() {
		for (int i = 0; i < numCPU; i++) {
			Player computer = new Player(startingChipCount, "CPU " + i);
			computers.add(computer);
		}				
		return;
	}

	public static void initializeBets() {
		// Do for all players
		// for(Player player : players) ???
		System.out.println("Current chip amount: " + human.getNumChips());
		System.out.println("Please enter the number of chips you would like to bet this round.");
		int bet = 0;
		boolean valid = false;
		while(!valid){
			String betStr = in.nextLine();
			betStr = betStr.replace("\n", "");
			try{      
				bet = Integer.parseInt(betStr);
				if(bet > human.getNumChips()){
					System.out.println("Error: You cannot bet more chips than you currently have.");
				}else if(bet < 1){
					System.out.println("Error: You must place a bet between 1 and " + human.getNumChips() + " chips.");
				}else{
					valid = true;
					human.setNumChips(human.getNumChips() - bet);
               chipPot += bet;
				}
			}catch(Exception e){
				System.out.println("Error: Please enter a valid integer.");
			}
		}
		//} // END for(Player...)
	}

	//Setup the deck for the first time and shuffle the cards for a new game
	public static void initializeGame() {

		System.out.println("Setting up CPUs...");
		initializeCPU();
		System.out.println("Setting up deck...");
		initializeDeck();
		System.out.println("Shuffling cards...");
		shuffle();
		System.out.println("Dealing hands...");
		deal();
		System.out.println("Setting up initial bets...");
		initializeBets();
		System.out.println("");
		return;
	}
   
   public static void resetGame() {
      human = null;
      for(Player cpu : computers)
         cpu = null;
      chipPot = 0;
      winners = null;
   }

	//Prints the rules
	//TODO PRINT GAME RULES
	public static void printRules() {
		System.out.println("TODO: ADD RULES HERE");
		System.out.println("");
		return;
	}

	//Allows the player to change the game settings
	public static void settings() {
		System.out.println("Current starting chip amount (Default: " + DEFAULT_CHIP_SETTING + "): " + startingChipCount);
		System.out.println("Difficulty Guideline: " + CPU_EASY + " - Easy, "
				+ CPU_MEDIUM + " - Medium, "
				+ CPU_HARD + " - Hard, "
				+ CPU_INSANE + " - Insane");
		System.out.println("Current CPU Difficulty level (Default: " + DEFAULT_CPU_DIFFICULTY_SETTING + "): " + difficulty);
		System.out.println("Current number of CPUs (Default: " + DEFAULT_CPU_COUNT_SETTING + ", "
				+ "Min: " + MIN_CPU_COUNT + ", Max: " + MAX_CPU_COUNT + "): " + numCPU);
		while (true) {
			System.out.println("To change the starting chip amount type: 'chips (amount)' where (amount) is the number of chips you want to start with.");
			System.out.println("To change the difficulty level type: 'difficulty (level)' where (level) is the number cooresponding to the level of difficulty.");
			System.out.println("To change the number of CPUs typs: 'cpu (number)' where number is how many computers you wish to play against.");
			System.out.println("To return to the menu type: 'return'");
			System.out.println("");
			String input = in.nextLine();
			//System.out.println("input: '" + input + "'");
			if (input.contains("difficulty")) {
				String level = null;
				if (input.length() > 10) {
					level = input.substring(11);
					try {
						int newLevel = Integer.parseInt(level);
						if (newLevel < 0 || newLevel > 3) System.out.println("I'm sorry, that wasnt a proper difficulty setting (-1 < difficulty < 4)");
						else {
							difficulty = newLevel;
							System.out.println("New difficulty setting is: " + difficulty);
						}
					} catch (Exception e) {
						System.out.println("I'm sorry, that wasnt a proper difficulty setting (-1 < difficulty < 4)");
					}
				}
				else {
					System.out.println("I'm sorry that wasnt a proper input, there must be a space then an integer following the word 'difficulty'.");
				}
			}
			else if (input.contains("chips")) {
				String amount = null;
				if (input.length() > 5) {
					amount = input.substring(6);
					try {
						int newAmount = Integer.parseInt(amount);
						if (newAmount <= 0 || newAmount >= 10000000) System.out.println("I'm sorry, that wasnt a proper chip amount (0 < amount < 10,000,000)");
						else {
							startingChipCount = newAmount;
							System.out.println("New chip setting is: " + startingChipCount);
						}
					} catch (Exception e) {
						System.out.println("I'm sorry, that wasnt a proper chip amount (0 < amount < 10,000,000)");
					}
				}
				else System.out.println("I'm sorry that wasnt a proper input, there must be a space then an integer following the word 'chips'.");
			}
			else if (input.contains("cpu")) {
				String numCPU = null;
				if (input.length() > 3) {
					numCPU = input.substring(4);
					try {
						int newNumCPU = Integer.parseInt(numCPU);
						if (newNumCPU <= 0 || newNumCPU >= 6) System.out.println("I'm sorry, that wasnt a proper number of CPUs (0 < number < 6)");
						else {
							Engine.numCPU = newNumCPU;
							System.out.println("New CPU setting is: " + Engine.numCPU);
						}
					} catch (Exception e) {
						System.out.println("I'm sorry, that wasnt a proper number of CPUs (0 < number < 8)");
					}
				}
				else System.out.println("I'm sorry that wasnt a proper input, there must be a space then an integer following the word 'cpu'.");
			}
			else if (input.equals("return")) {
				mainMenu();
				return;
			}
			else System.out.println("I'm sorry, thats not a proper command.");
			System.out.println("");
		}
	}

	//Prints the main menu and allows input to traverse options
	public static void mainMenu() {

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
				mainMenu();
				return;
			}

			//Switch based on input
			switch (choice) {
			case 1:
				printRules();
				mainMenu();
				break;

			case 2:		
				initializeGame();
				playGame();
				break;

			case 3:
				settings();
				break;

			case 4:
				quit();
				break;

			default:
				System.out.println("Invalid input: Input must be in range [1-4].");
				check = false;
			}// END Switch

		}// END while(!check)

		return;
	}

	public static void quit() {
		in.close();
		System.out.println("Bye-Bye!");
		System.exit(0);	
	}

	public static void main(String args[]) {
		//Initialize globals to defaults
		in = new Scanner(System.in);
		numCPU = DEFAULT_CPU_COUNT_SETTING;
		startingChipCount = DEFAULT_CHIP_SETTING;
		difficulty = DEFAULT_CPU_DIFFICULTY_SETTING;
		computers = new ArrayList<Player>();
	     winners = new ArrayList<Player>();
		
		//allows update w/ account serialization
		playerDisplayName = "Player";
		
		human = new Player(startingChipCount, playerDisplayName);
		dealer = new Player(startingChipCount, "Dealer");


		//Print start menu
		mainMenu();

	}
}


