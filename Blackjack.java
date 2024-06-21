import java.util.Scanner;
import java.util.Random;

public class Blackjack {

    //Win Condition Parameters: "Bust", "HigherCount", "Blackjack"


    static String[] cards = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

    static ArrayList<String> playerCards = new ArrayList<String>();
    static ArrayList<String> dealerCards = new ArrayList<String>();

    static int playerBet;
    static int playerTotal = 100;
    static boolean status = true; //determines whether or not to keep the game running, isGameOver determines if the game should 
                                  //continue to ask the player to hit or stand
    static boolean isGameOver;



    public static void main(String[] args) {
        while(status) {
            initialize();

            Scanner sc = new Scanner(System.in);

        
            if(isGameOver != true) { //asks player once for options, only time Double Down is included
                System.out.println("\nHit, Stand, or Double Down?");
                String input = sc.nextLine();
                switch(input.toLowerCase()) {
                    case "hit":
                       hit();
                        break;
                    case "stand":
                         stand();
                        break;
                    case "double down":
                        doubleDown();
                        break;
                    default:
                        System.out.println("Enter a valid input");
                 }
            }

            while(isGameOver != true) { //as long as the game is not over, player is asked to hit or stand
                System.out.println("Hit or Stand?");
                String in = sc.nextLine();
        
                switch(in.toLowerCase()) {
                    case "hit":
                        hit();
                        break;
                    case "stand":
                        stand();
                        break;
                    default:
                        System.out.println("Enter a valid input");
            }
        }
            //Code runs after isGameOver returns false (blackjack, player/dealer win or bust)
            System.out.println("Play Again? (Y/N)");
            String choice = sc.nextLine();
            if(choice.toUpperCase().contains("N")) {
                sc.close();
                status = false; //ends the code
            }
            else if(choice.toUpperCase().contains("Y")) {
                System.out.print("\033[H\033[2J");  
                System.out.flush();  
            }
            playerCards.clear(); //clears player hand
            dealerCards.clear(); //clears dealer hand
        }

    }


    static Random rand = new Random(); //determines cards picked up from the deck
    static int hiddenIndex = rand.nextInt(13); //Index of dealer's hidden card that is drawn but kept flipped over

    static void initialize() {

        isGameOver = false;

        //BETTING
        System.out.println("Enter your bet, your total is " + playerTotal + ":");
        Scanner scanner = new Scanner(System.in);
        int bet = scanner.nextInt();
        if(bet <= playerTotal) {
            playerBet = bet;
            playerTotal -= playerBet;
        }
        else if(bet < 0) {
            System.out.println("cant bet less than 0");
        }
        else {
            System.out.println("cant bet more than total");
        }


        //PLAYER CARDS

        playerCards.add(cards[rand.nextInt(13)]); //Adds first card to the player's hand
        playerCards.add(cards[rand.nextInt(13)]); //adds second card to the player's hand

        System.out.println("\nYour Cards: " + playerCards);
        System.out.print("Your Count: " + count(playerCards));

        //DEALER CARDS

        dealerCards.add(cards[rand.nextInt(13)]); //Adds shown dealer card
        System.out.print("\n\nDealer's card: " + dealerCards + "\n"); //prints first card
        

        //If the hidden card is an ace and the first card equals 10    OR    if the hidden card is equal to 10 and the first card is an ace, then the dealer has blackjack
        if((hiddenIndex == 0) && (getCardValue(dealerCards.get(0)) == 10) || (getCardValue(cards[hiddenIndex]) == 10 && dealerCards.get(0) == "Ace")) {
            dealerCards.add(cards[hiddenIndex]);
            if(count(playerCards) == 21) { //if player also has blackjack
                push(21);
            }
            else {
                dealerWins("Blackjack");
            }
        }
        //only the player has blackjack
        else if(count(playerCards) == 21) {
            playerWins("Blackjack");
        }
    }



    //gets value of a card from its String value
    static int getCardValue(String card) {
        int value = 0;
        switch(card) {
            case "Ace":
                value = 1;
                break;
            case "Two":
                value = 2;
                break;
            case "Three":
                value = 3;
                break;
            case "Four":
                value = 4;
                break;
            case "Five":
                value = 5;
                break;
            case "Six":
                value = 6;
                break;
            case "Seven":
                value = 7;
                break;
            case "Eight":
                value = 8;
                break;
            case "Nine":
                value = 9;
                break;
            case "Ten":
                value = 10;
                break;
            case "Jack":
                value = 10;
                break;
            case "Queen":
                value = 10;
                break;
            case "King":
                value = 10;
                break;
        }
        return value;
    }

    //determines the value of all the cards in a hand, including determining the best value for an ace if the player has one
    static int count(ArrayList<String> hand) {
        int value = 0;
        for(int i = 0; i < hand.size(); i++) {
            value += getCardValue(hand.get(i));
        }
        
        if(hand.contains("Ace")) {
            value += 10; //always add 10 to represent Ace = 11
            if(value > 21) {
                value -=10; //remove this 10 to represent Ace = 1 when it cannot be 11
            }
        }
        return value;
    }

    static void hit() {

        System.out.print("\033[H\033[2J");  
        System.out.flush();  

        playerCards.add(cards[rand.nextInt(13)]);
        System.out.println("You draw: " + getCardValue(playerCards.get(playerCards.size() - 1)));
        System.out.println("Your Cards: " + playerCards);
        System.out.println("Your Count: " + count(playerCards));

        if(count(playerCards) > 21) {
            dealerWins("Bust");
        }
        else if (count(playerCards) == 21) {
            stand();
        }

        System.out.println("\nDealer's Cards: " + dealerCards);
        System.out.println("Dealer's Count: " + count(dealerCards) + "\n");

    }

    static void stand() {

        System.out.print("\033[H\033[2J");  
        System.out.flush();  

        System.out.println("Dealer Draws: " + cards[hiddenIndex]);
        dealerCards.add(cards[hiddenIndex]);
        System.out.println("Dealer's Cards: " + dealerCards);
        System.out.println("Dealer's Count: " + count(dealerCards) + "\n");

        while(count(dealerCards) < 17) {
            int tempIndex = rand.nextInt(13);
            dealerCards.add(cards[tempIndex]);
            System.out.println("Dealer Draws: " + cards[tempIndex]);
            System.out.println("Dealer Cards: " + dealerCards);
            System.out.println("Dealer Count: " + count(dealerCards));
        }

        System.out.println("\nYour Cards: " + playerCards);
        System.out.println("Your Count: " + count(playerCards) + "\n");
        
        if(count(dealerCards) > 21) {
            playerWins("Bust");
        }
        else if (count(playerCards) > count(dealerCards)) {
            playerWins("HigherCount");
        }
        else if(count(dealerCards) > count(playerCards)) {
            dealerWins("HigherCount");
        }
        else if(count(dealerCards) == count(playerCards)) {
            push(count(playerCards));
        }

        isGameOver = true;
    }

    static void doubleDown() {
        
        //clears screen
        System.out.print("\033[H\033[2J");  
        System.out.flush();  

        //playerBet was subtracted from the playerTotal, so if the playerTotal - playerBet >= playerBet, the player can double down
        if(playerTotal >= playerBet) { 
            
            playerCards.add(cards[rand.nextInt(13)]);
            System.out.println("You draw: " + getCardValue(playerCards.get(playerCards.size() - 1))); //prints most recently added card (above line)
            System.out.println("Your Cards: " + playerCards);
            System.out.println("Your Count: " + count(playerCards));

            if(count(playerCards) > 21) {
                playerTotal -= playerBet; //removes the additional bet that was doubled down
                dealerWins("Bust");
            }
            else {
                System.out.print("\033[H\033[2J");  
                System.out.flush();  

                System.out.println("Dealer Flips over: " + cards[hiddenIndex]);
                dealerCards.add(cards[hiddenIndex]);
                System.out.println("Dealer's Cards: " + dealerCards);
                System.out.println("Dealer's Count: " + count(dealerCards) + "\n");

                while(count(dealerCards) < 17) {
                    int tempIndex = rand.nextInt(13);
                    dealerCards.add(cards[tempIndex]);
                    System.out.println("Dealer Draws: " + cards[tempIndex]);
                    System.out.println("Dealer Cards: " + dealerCards);
                    System.out.println("Dealer Count: " + count(dealerCards));
                }

                System.out.println("Your Cards: " + playerCards);
                System.out.println("Your Count: " + count(playerCards) + "\n");

                System.out.println("\nDealer's Cards: " + dealerCards);
                System.out.println("Dealer's Count: " + count(dealerCards) + "\n");
        
                if(count(dealerCards) > 21) {
                    playerTotal += playerBet * 2; //the additional bet is doubled and added here
                    playerWins("Bust"); //the original bet is doubled and added in the correpsonding methods
                }
                else if (count(playerCards) > count(dealerCards)) {
                    playerTotal += playerBet * 2;
                    playerWins("HigherCount");
                }
                else if(count(dealerCards) > count(playerCards)) {
                    playerTotal -= playerBet;
                    dealerWins("HigherCount");
                }
                else if(count(dealerCards) == count(playerCards)) {
                    playerTotal += playerBet;
                    push(count(playerCards));
                }

            }
        }
        //Not enough to double down
        else {
            System.out.println("You do not have enough money to double down, your bet has been reinstated");
            playerTotal += playerBet; //returns the bet to the player and forces them to play again.
            isGameOver = true;
        }
    }


    static void dealerWins(String reason) {
        if(reason == "Bust") {
            System.out.println("Player Busts \n");
        }
        else if(reason == "HigherCount") {
            System.out.println("Dealer had " + count(dealerCards) + ", you only had " + count(playerCards) + ", you lose :(\n");
        }
        else if(reason == "Blackjack") {
            System.out.println("\nDealer has blackjack\n");
        }
        //the amount betted already has been subtracted from the player's total so the total does not change

        isGameOver = true;
    }

    static void playerWins(String reason) {
        if(reason == "Blackjack") {
            System.out.println("You have blackjack, you win!\n");
            playerTotal += playerBet + (playerBet * 1.5); //add lost bet plus the profit of 1.5x bet
        }
        else if(reason == "HigherCount") {
            System.out.println("you had " + count(playerCards) + ", the Dealer had " + count(dealerCards) + ", you win!\n");
            playerTotal += (playerBet * 2);
        }
        else if(reason == "Bust") {
            System.out.println("Dealer busted, you win!");
            playerTotal += (playerBet * 2);
        }

            isGameOver = true;
    }

    static void push(int count) {
        playerTotal += playerBet;
        System.out.println("Push, player and dealer both had " + count + "\n");
        isGameOver = true;
    }
}