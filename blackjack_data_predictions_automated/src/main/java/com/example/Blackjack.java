package com.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
public class Blackjack {

    //Win Condition Parameters: "Bust", "HigherCount", "Blackjack"


    static String[] cards = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

    static ArrayList<String> playerCards = new ArrayList<String>();
    static ArrayList<String> dealerCards = new ArrayList<String>();


    static boolean status = true; //determines whether or not to keep the game running, isGameOver determines if the game should 
                                  //continue to ask the player to hit or stand
    static boolean isGameOver;

    //Data variables
    static int initialPlayerCount;
    static int initialDealerCount;
    static int playerDecision = 0; //0 for stand, 1 for hit, 2 for double down
    static int outcome; //0 for lose 1 for win(includes push)
    static boolean didBlackjackOccur;
    static int hands = 0; //number of times the program will run blackjack
    static int blackjackCount = 0;





    public static void main(String[] args) {
        while(hands < 26000) {
            initialize();
            stand(); //change to hit or double down for more data
            //hit();
            updateSpreadsheet();
            playerCards.clear(); //clears player hand
            dealerCards.clear(); //clears dealer hand
            if(didBlackjackOccur == false) {
                System.out.println(hands);
                hands++;
            }
            
        }
        System.out.println(blackjackCount);
    }

     public static void updateSpreadsheet() {
        if(didBlackjackOccur == false) {
            String filePath = "C:\\Users\\dalep\\Desktop\\Coding\\Java Projects\\blackjack_data_predictions_automated\\BlackjackData_StandValues.xlsx";
            try (FileInputStream fis = new FileInputStream(filePath);
                XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

                // Access the desired sheet
                Sheet sheet = workbook.getSheetAt(0);

                // Find the next empty row
                int rowCount = sheet.getLastRowNum();
                Row row = sheet.createRow(++rowCount);

                // Update the row with game data
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(initialPlayerCount);

                Cell cell2 = row.createCell(1);
                cell2.setCellValue(initialDealerCount);

                Cell cell3 = row.createCell(2);
                cell3.setCellValue(playerDecision);

                Cell cell4 = row.createCell(3);
                cell4.setCellValue(outcome);

                // Write the updated workbook to the file
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                   workbook.write(fos);
                }

                }   catch (IOException e) {
                    e.printStackTrace();
            }
        }
        else {
            blackjackCount++;
        }
    }



    static Random rand = new Random(); //determines cards picked up from the deck
    static int hiddenIndex = rand.nextInt(13); //Index of dealer's hidden card that is drawn but kept flipped over

    static void initialize() {

        isGameOver = false;
        didBlackjackOccur = false;


        //PLAYER CARDS

        playerCards.add(cards[rand.nextInt(13)]); //Adds first card to the player's hand
        playerCards.add(cards[rand.nextInt(13)]); //adds second card to the player's hand
        initialPlayerCount = count(playerCards);

        //DEALER CARDS

        dealerCards.add(cards[rand.nextInt(13)]); //Adds shown dealer card
        initialDealerCount = count(dealerCards);
        

        //If the hidden card is an ace and the first card equals 10    OR    if the hidden card is equal to 10 and the first card is an ace, then the dealer has blackjack
        if((hiddenIndex == 0) && (getCardValue(dealerCards.get(0)) == 10) || (getCardValue(cards[hiddenIndex]) == 10 && dealerCards.get(0) == "Ace")) {
            didBlackjackOccur = true;
            dealerCards.add(cards[hiddenIndex]);
            if(count(playerCards) == 21) { //if player also has blackjack
                push();
            }
            else {
                dealerWins();
            }
        }
        //only the player has blackjack
        else if(count(playerCards) == 21) {
            didBlackjackOccur = true;
            playerWins();
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

    static void stand() {
        dealerCards.add(cards[hiddenIndex]);

        while(count(dealerCards) < 17) {
            int tempIndex = rand.nextInt(13);
            dealerCards.add(cards[tempIndex]);
        }
        
        if(count(dealerCards) > 21) {
            playerWins();
        }
        else if (count(playerCards) > count(dealerCards)) {
            playerWins();
        }
        else if(count(dealerCards) > count(playerCards)) {
            dealerWins();
        }
        else if(count(dealerCards) == count(playerCards)) {
            push();
        }

        isGameOver = true;
    }

    static void hit() {
            playerCards.add(cards[rand.nextInt(13)]);
            if(count(playerCards) > 21) {
                dealerWins();
            }
            else {
                dealerCards.add(cards[hiddenIndex]);
                while(count(dealerCards) < 17) {
                    int tempIndex = rand.nextInt(13);
                    dealerCards.add(cards[tempIndex]);

                }
                if(count(dealerCards) > 21) {
                    playerWins();
                }
                else if (count(playerCards) > count(dealerCards)) {
                    playerWins();
                }
                else if(count(dealerCards) > count(playerCards)) {
                    dealerWins();
                }
                else if(count(dealerCards) == count(playerCards)) {
                    push();
                }

            }
    }


    static void dealerWins() {
        outcome = 0;
        isGameOver = true;
    }

    static void playerWins() {
        outcome = 1;
        isGameOver = true;
    }

    static void push() {
        outcome = 1;
        isGameOver = true;
    }
}