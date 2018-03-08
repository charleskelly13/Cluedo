/*  Cluedo - Sprint 2
    Team: auroraBorealis
    Members: Oisin Quinn (16314071), Darragh Clarke (16387431), Charlie Kelly (16464276)
    "Aurora Borealis! At this time of year? At this time of day? In this part of the country? Localized entirely within your kitchen?" */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * This class allows users to input user name and pick which token they wish to play
 * it also decides which player goes first
 */
class GameSetup {

    private Counters counters;
    private Cards cards;
    int a = 0;


    GameSetup(Counters counters) {
        this.counters = counters;
        playerCountSelect();
    }

    /**
     * Lets the player select the number of characters
     */
    private void playerCountSelect() {
        //the below two strings and jOptionPane are used to allow the user to pick how many users will play
        String[] playerCount = (new String[]{"2", "3", "4", "5", "6"});
        String userChoice = (String) JOptionPane.showInputDialog(null, "Select How Many Players You Want", "Cluedo", JOptionPane.QUESTION_MESSAGE, new ImageIcon("src/cluedo.png"), playerCount, playerCount[0]);

        if (userChoice == null) {//if the user hits cancel the game is quit
            System.out.println("You have exited the game,Thanks For Playing!");
            System.exit(0);
        }

        //depending on what they pick this switch statement sends the correct number of players to character select
        switch (userChoice) {
            case "2":
                CharacterSelect(2);
                break;
            case "3":
                CharacterSelect(3);
                break;
            case "4":
                CharacterSelect(4);
                break;
            case "5":
                CharacterSelect(5);
                break;
            case "6":
                CharacterSelect(6);
                break;

        }
    }

    /**
     * Lets the player select which character to play as and enter their own name
     */
    private void CharacterSelect(int PlayerCount) {
        int rollFirst;
        //this string stores all the possible characters
        String[] Characters = (new String[]{"Plum", "White", "Scarlet", "Green", "Mustard", "Peacock"});
        //this list will store the above string in list format , both are needed for the below loop
        List<String> CharacterList = new ArrayList<>(Arrays.asList(Characters));
        for (int i = 0; i < PlayerCount; i++) {
            String userName =(String) JOptionPane.showInputDialog(null,"Enter Your Username","Cluedo",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("src/cluedo.png"),null,"");
            if (userName == null) {//if the user cancels , exit the code
                System.out.println("You have exited the game,Thanks For Playing!");
                System.exit(0);
            }
            String userChoice = (String) JOptionPane.showInputDialog(null, "Player " + (i + 1) + " Please Select A Character", "Cluedo", JOptionPane.QUESTION_MESSAGE, new ImageIcon("src/cluedo.png"), Characters, Characters[0]);
            if (userChoice == null) {//if the user cancels exit the code
                System.out.println("You have exited the game,Thanks For Playing!");
                System.exit(0);
            }
            CharacterList.remove(userChoice);//remove the player the user chose from the list
            Characters = CharacterList.toArray(new String[0]);//characters gets updated with the new list


            // There are two dice, so we roll twice
           //Dice die = new Dice();
           //rollFirst = die.roll();
           //rollFirst += die.roll();
            counters.createCounter(userName, userChoice, 0);//send the username and their character choice to be created and placed on the board

            int Players = 0;
            for (Counter currentCounter : counters) {
                Players++;
            }
			
			

        }
    }

}
