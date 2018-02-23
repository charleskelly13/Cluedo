/*  Cluedo - Sprint 1
    Team: auroraBorealis
    Members: Oisin Quinn (16314071), Darragh Clarke (16387431), Charlie Kelly (16464276)
    "Aurora Borealis! At this time of year? At this time of day? In this part of the country? Localized entirely within your kitchen?" */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class creates the graphical interface of the program and initialises all of the pieces
 */
public class GUI extends JFrame {

    // These are the variables contained in the GUI - the board components and the pieces on the board
    private Counters counters;
    private Weapons weapons;
    private Rooms rooms;
    private String[] play =new String[6];
    private JPanel board;
    private JTextArea infoField;
    private JTextField userInput;
    private JScrollPane scrollPane;
    private BufferedImage boardImage;
    private int dieResult=0;
    private boolean quit=false;
    private int PlayTurn=0;
    private int dieRoll=0;//tracker used to stop more than one roll call per turn
    private int turnTrack=0;
    private String CurrPlay=play[0];
    // Squares that are marked 0 are inaccessible by the player (they are out of bounds)
    // Squares that are marked 1 are pathways that the player can walk on
    // Sqaures that are marked 2 are pathway squares that are adjacent to room entrances
    // Sqaures that are marked 3 are squares inside rooms
    // Sqaures that are marked 4 are room squares that are adjacent to room entrances
    private int[][] squareType = {
            {0,0,0,0,0,0,0,0,0,-1,0,0,0,0,-1,0,0,0,0,0,0,0,0,0},
            {3,3,3,3,3,3,0,1,1,1,3,3,3,3,1,1,1,0,3,3,3,3,3,3},
            {3,3,3,3,3,3,1,1,3,3,3,3,3,3,3,3,1,1,3,3,3,3,3,3},
            {3,3,3,3,3,3,1,1,3,3,3,3,3,3,3,3,1,1,3,3,3,3,3,3},
            {3,3,3,3,3,3,1,1,3,3,3,3,3,3,3,3,1,1,4,3,3,3,3,3},
            {3,3,3,3,3,3,1,2,4,3,3,3,3,3,3,4,2,1,2,3,3,3,3,0},
            {3,3,3,3,4,3,1,1,3,3,3,3,3,3,3,3,1,1,1,1,1,1,1,-1},
            {1,1,1,1,2,1,1,1,3,4,3,3,3,3,4,3,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,1,2,1,1,1,1,2,1,1,1,3,3,3,3,3,3},
            {3,3,3,3,3,1,1,1,1,1,1,1,1,1,1,1,1,2,4,3,3,3,3,3},
            {3,3,3,3,3,3,3,3,1,1,3,3,3,3,3,1,1,1,3,3,3,3,3,3},
            {3,3,3,3,3,3,3,3,1,1,3,3,3,3,3,1,1,1,3,3,3,3,3,3},
            {3,3,3,3,3,3,3,4,2,1,3,3,3,3,3,1,1,1,3,3,3,3,4,3},
            {3,3,3,3,3,3,3,3,1,1,3,3,3,3,3,1,1,1,1,1,2,1,2,0},
            {3,3,3,3,3,3,3,3,1,1,3,3,3,3,3,1,1,1,3,3,4,3,3,0},
            {3,3,3,3,3,3,4,3,1,1,3,3,3,3,3,1,1,3,3,3,3,3,3,3},
            {0,1,1,1,1,1,2,1,1,1,3,3,4,3,3,1,2,4,3,3,3,3,3,3},
            {-1,1,1,1,1,1,1,1,1,1,1,2,2,1,1,1,1,3,3,3,3,3,3,3},
            {0,1,1,1,1,1,2,1,1,3,3,4,4,3,3,1,1,1,3,3,3,3,3,0},
            {3,3,3,3,3,3,4,1,1,3,3,3,3,3,3,1,1,1,1,1,1,1,1,-1},
            {3,3,3,3,3,3,3,1,1,3,3,3,3,3,4,2,1,2,1,1,1,1,1,0},
            {3,3,3,3,3,3,3,1,1,3,3,3,3,3,3,1,1,4,3,3,3,3,3,3},
            {3,3,3,3,3,3,3,1,1,3,3,3,3,3,3,1,1,3,3,3,3,3,3,3},
            {3,3,3,3,3,3,3,1,1,3,3,3,3,3,3,1,1,3,3,3,3,3,3,3},
            {3,3,3,3,3,3,0,-1,0,0,0,0,0,0,0,0,1,0,3,3,3,3,3,3},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    };

    /**
     * This method creates the graphical interface for the program
     */
    public GUI(Counters counters, Weapons weapons, Rooms rooms) {
        this.counters = counters;
        this.weapons = weapons;
        this.rooms = rooms;
        
        board = new JPanel();
        // We use BorderLayout to easily have multiple components in the same panel
        setLayout(new BorderLayout());
        setSize(835, 690);
        setTitle("Cluedo");
        // Places the frame in the centre of the screen
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // This loads the image "cluedo_board.jpg", or shows the user an error
        boardImage = null;
        try {
            boardImage = ImageIO.read(this.getClass().getResource("cluedo_board.jpg"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: cannot load board image.");
        }

        infoField = new JTextArea(10, 15);
        // I setEditable to false so that the user can't edit the text on the right-hand size
        infoField.setEditable(false);
        infoField.setLineWrap(true);
        // I place the infoField inside a scrollpane so that the textArea doesn't fill up
        scrollPane = new JScrollPane(infoField);

        DefaultCaret caret = (DefaultCaret)infoField.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        userInput = new JTextField();
        userInput.setText("Enter text here (type 'help' for help)");
        infoField.append("Commands: \nMove Player Piece\n - intial for direction of movement e.g U/D/L/R \n" +
                "\nEnd Turn\n - \"done\"\n\nQuit Game\n - \"quit\"\n\n Roll Dice\n - roll\n\n");
        // This method creates the Counter objects
        initialiseCounters(counters);
        // This method creates the Weapon objects
        initialiseWeapons(weapons);


        // Adds the different sections to the GUI
        addComponents();

        // Displays the frame to the user
        setVisible(true);
        Iterator countersIterator = counters.iterator();
       
        while (countersIterator.hasNext()) {
            Counter currentCounter = (Counter)countersIterator.next();
            play[turnTrack]=currentCounter.getCounterName();
            turnTrack++;
        }

        CurrPlay=play[0];
        turn();
        // This action occurs when the user types "enter" in the userInput field
        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // Understands what the user enters and acts accordingly

                interpretInput(CurrPlay);
        	}
        };
        userInput.addActionListener(action); //Sets a button(enter) to activate the above listener
    }
  

   
    /**
     * This method runs when we setVisible(true) and when we repaint()
     * It paints the images and counters onto the board
     */
    private void turn()
    {
    		if(play[PlayTurn].equals("Scarlet"))
    		{
    			CurrPlay="Scarlet";
    			infoField.append(CurrPlay+" has started their turn\n");
    		}
    		else if(play[PlayTurn].equals("Mustard"))
    		{
    			CurrPlay="Mustard";
    			infoField.append(CurrPlay+" has started their turn\n");
    		}
    		else if(play[PlayTurn].equals("Peacock"))
    		{
    			CurrPlay="Peacock";
    			infoField.append(CurrPlay+" has started their turn\n");
    		}
    		else if(play[PlayTurn].equals("Plum"))
    		{
    			CurrPlay="Plum";
    			infoField.append(CurrPlay+" has started their turn\n");
    		}
    		else if(play[PlayTurn].equals("White"))
    		{
    			CurrPlay="White";
    			infoField.append(CurrPlay+" has started their turn\n");
    		}
    		else if(play[PlayTurn].equals("Green"))
    		{
    			CurrPlay="Green";
    			infoField.append(CurrPlay+" has started their turn\n");
    		}

        if (isRoom(CurrPlay)) {
            listExits(CurrPlay);
        }
    }
    
    public  int roll(){
    	Dice die = new Dice();
      	 dieResult = die.rollDice();
      	infoField.append("\nYou rolled a "+ dieResult+"\n");
      	 return dieResult;
    }
    public void paint(Graphics g) {
    	 
        // This line insures that the other components of the JFrame are visible
        super.paint(g);
        // We cast to Graphics2D to access more image drawing features
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(boardImage, 0, 25, this);

        drawCounters(g);
        drawWeapons(g);
    }

    /**
     * Draws the counters on top of the board in the correct locations
     */
    private void drawCounters(Graphics g) {
        for(Counter c : counters) {
            c.paintComponent(g);
        }
    }

    /**
     * Draws the weapons on top of the board in the correct locations
     */
    private void drawWeapons(Graphics g) {
        for(Weapon w : weapons) {
            w.paintComponent(g);
        }
    }

    /**
     * Reads text from userInput and interprets text accordingly
     */
    private void interpretInput( String name) {
        String inputtedText = userInput.getText();//takes info from the field
        userInput.setText("");//wipes the field after
      
       
        infoField.append(">" + inputtedText + "\n");//puts it into the panel
        String splitStr = inputtedText.replaceAll("\\s+","");// Splits the inputted string into an array based spaces
  
        if (splitStr.toLowerCase().equals("help")) {
            helpCommand();
        }  
        else if((splitStr.toLowerCase().equals("u") || splitStr.toLowerCase().equals("up")||splitStr.toLowerCase().equals("d") || splitStr.toLowerCase().equals("down")||splitStr.toLowerCase().equals("l") || splitStr.toLowerCase().equals("left")||splitStr.toLowerCase().equals("r") || splitStr.toLowerCase().equals("right"))) // If the first word is move or m in any format
        {
            
            if(dieResult>0)
            {
                if(moveCommand(splitStr, name)==true)
                {
                  dieResult=dieResult-1;
                }
            }
            else
            {
            	infoField.append("\n You have used all your movement.\n");
            }
        }
        else if(splitStr.toLowerCase().equals("quit"))
        {
        	infoField.append("Thank you for playing! Goodbye");

        	System.exit(0);
        }

        else if(splitStr.toLowerCase().equals("roll"))
        {	
        	if (dieRoll==0){
        	dieResult=roll();
        	//TODO This lets you move (basically) unlimitedly - for testing purposes only
            //dieResult=1000;
        	dieRoll++;
        	}
        	else{
        		infoField.append("You have already rolled this turn!");
        	}
        }
     
        else if(splitStr.toLowerCase().equals("done"))
        {
        	dieResult=0;
        	dieRoll=0;
        	infoField.append("\nPlayers turn has ended!\n");
        	PlayTurn=(PlayTurn+1)%turnTrack;
        	
        	 turn();
        	// Goes to the next players move
        } else if(Integer.parseInt(splitStr.toLowerCase()) > 0 && dieResult>0) {
            Counter c = Counters.get(CurrPlay);
            Room r = c.getCurrentRoom();

            switch (splitStr.toLowerCase()) {
                case "1":
                    c.setGridXY(r.getEntrances().get(0).getCol(), r.getEntrances().get(0).getRow());
                    break;
                case "2":
                    c.setGridXY(r.getEntrances().get(1).getCol(), r.getEntrances().get(1).getRow());
                    break;
                case "3":
                    c.setGridXY(r.getEntrances().get(2).getCol(), r.getEntrances().get(2).getRow());
                    break;
                case "4":
                    c.setGridXY(r.getEntrances().get(3).getCol(), r.getEntrances().get(3).getRow());
                    break;
            }
            int xValue = c.getGridX();
            int yValue = c.getGridY();
            if(squareType[yValue - 1][xValue] == 2) {
                c.setGridXY(xValue, yValue - 1);
            }
            if(squareType[yValue + 1][xValue] == 2) {
                c.setGridXY(xValue, yValue + 1);
            }
            if(squareType[yValue][xValue - 1] == 2) {
                c.setGridXY(xValue - 1, yValue);
            }
            if(squareType[yValue][xValue + 1] == 2) {
                c.setGridXY(xValue + 1, yValue - 1);
            }

            repaint();
            dieResult--;
        }

        else { 
        	infoField.append("\n Invalid command entered!\n");
        }
        
    }
   

   
	private boolean moveCommand(String splitStr, String colour) {
        // I added shortcuts for the directions to make testing easier
        // We can only move if the next square is either a pathway or is a room square adjacent to an entrance
		boolean moved=true;
//		int x=0;
//
//		if (isRoom(colour)) {
//            listExits(colour);
//        }
		
        switch (splitStr.toLowerCase()) { // Checks the movement direction entered
            case "up":
            case "u":
                if ((isPathway(colour, "u") || isEnterable(colour, "u")) && !isOccupied(colour, "u")) {
                    squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()] *= -1;
                    Counters.get(colour).moveUp(1);
                    squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()] *= -1;
                }
                else
                {
                	moved=false;
                }
                break;
            case "down":
            case "d":
                if ((isPathway(colour, "d") || isEnterable(colour, "d")) && !isOccupied(colour, "d")) {
                    squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()] *= -1;
                    Counters.get(colour).moveDown(1);
                    squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()] *= -1;
                }
                else
                {
                	moved=false;
                }
                break;
            case "left":
            case "l":
                if ((isPathway(colour, "l") || isEnterable(colour, "l")) && !isOccupied(colour, "l")) {
                    squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()] *= -1;
                    Counters.get(colour).moveLeft(1);
                    squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()] *= -1;
                }
                else
                {
                	moved=false;
                }
                break;
            case "right":
            case "r":
                if ((isPathway(colour, "r") || isEnterable(colour, "r")) && !isOccupied(colour, "r")) {
                    squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()] *= -1;
                    Counters.get(colour).moveRight(1);
                    squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()] *= -1;
                }
                else
                {
                	moved=false;
                }
                break;
            default:
                infoField.append("\nInvalid direction chosen\n");
                moved=false;
       
		}
        if(!moved)
        {
        	infoField.append("Error. Incorrect Movement!");
        }

        if(isRoom(colour)) {
            moveToRoomCentre(colour);
        }

        repaint(); // Repaints the board with the new location of the pieces
        return moved;
    }

    private boolean isRoom(String colour) {
        if (squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()] <= -3 || squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()] >= 3 ) {
            return true;
        }
        return false;
    }

    private void listExits(String colour) {
        Counter counter = Counters.get(colour);
        Room room = counter.getCurrentRoom();
        ArrayList<Coordinates> entrances = room.getEntrances();

        // Prints entrances
        infoField.append("Type the entrance number to leave through that entrance.\n");
        for (int i = 0; i < entrances.size(); i++) {
            Coordinates current = entrances.get(i);
            infoField.append("Entrance " + (i+1) + ": (" + current.getRow() + " , " + current.getCol() + ")\n");
        }


    }

    private void moveToRoomCentre(String colour) {
        Counter counter = Counters.get(colour);
        Coordinates coord = new Coordinates(counter.getGridX(), counter.getGridY());
        squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()] *= -1;
        Room room = null;

        for (Room r : rooms) {
            ArrayList<Coordinates> entrances = r.getEntrances();
            for (int i = 0; i < entrances.size(); i++) {
                if ((coord.getCol() == entrances.get(i).getCol()) && (coord.getRow() == entrances.get(i).getRow())) {
                    counter.setCurrentRoom(r);
                    room = r;
                }
            }
        }

        ArrayList<Coordinates> tokenSquares = room.getTokenSquares();
        Coordinates location = tokenSquares.get(room.getLastFilledToken() + 1);
        counter.setGridXY(location.getCol(), location.getRow());
        room.incremenetLastFilledToken();

    }

    /**
     * Checks if you can enter a certain room or not by looking at both the current square and the next square
     * @param colour the colour of the token
     * @param direction the direction you want to remove
     * @return a boolean representing if you can enter the room or not
     */
    private boolean isEnterable(String colour, String direction) {
        int nextSquareType;
        int currentSquareType = squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()];

        switch(direction) {
            case "u":
                nextSquareType = squareType[Counters.get(colour).getGridY()-1][Counters.get(colour).getGridX()];
                if (nextSquareType == 4 && currentSquareType == -2) {
                    return true;
                }
                break;
            case "d":
                nextSquareType = squareType[Counters.get(colour).getGridY()+1][Counters.get(colour).getGridX()];
                if (nextSquareType == 4 && currentSquareType == -2) {
                    return true;
                }
                break;
            case "l":
                nextSquareType = squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()-1];
                if (nextSquareType == 4 && currentSquareType == -2) {
                    return true;
                }
                break;
            case "r":
                nextSquareType = squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()+1];
                if (nextSquareType == 4 && currentSquareType == -2) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Checks if the next square is a pathway
     * @param colour the colour of the token you want to move
     * @param direction the direction you want to move
     * @return a boolean representing if the next square is a pathway or not
     */
    private boolean isPathway(String colour, String direction) {
        int nextSquareType;
        int currentSquareType = squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()];

        switch(direction) {
            case "u":
                nextSquareType = squareType[Counters.get(colour).getGridY()-1][Counters.get(colour).getGridX()];
                if (currentSquareType == 4 && nextSquareType == 1) {
                    return false;
                } else if (nextSquareType == 2 || nextSquareType == 1) {
                    return true;
                }
                break;
            case "d":
                nextSquareType = squareType[Counters.get(colour).getGridY()+1][Counters.get(colour).getGridX()];
                if (currentSquareType == 4 && nextSquareType == 1) {
                    return false;
                } else if (nextSquareType == 2 || nextSquareType == 1) {
                    return true;
                }
                break;
            case "l":
                nextSquareType = squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()-1];
                if (currentSquareType == 4 && nextSquareType == 1) {
                    return false;
                } else if (nextSquareType == 2 || nextSquareType == 1) {
                    return true;
                }
                break;
            case "r":
                nextSquareType = squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()+1];
                if (currentSquareType == 4 && nextSquareType == 1) {
                    return false;
                } else if (nextSquareType == 2 || nextSquareType == 1) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Checks if the next square is a pathway
     * @param colour the colour of the token you want to move
     * @param direction the direction you want to move
     * @return a boolean representing if the next square is a pathway or not
     */
    private boolean isOccupied(String colour, String direction) {
        int nextSquareType;

        switch(direction) {
            case "u":
                nextSquareType = squareType[Counters.get(colour).getGridY()-1][Counters.get(colour).getGridX()];
                if (nextSquareType < 0) {
                    return true;
                }
                break;
            case "d":
                nextSquareType = squareType[Counters.get(colour).getGridY()+1][Counters.get(colour).getGridX()];
                if (nextSquareType < 0) {
                    return true;
                }
                break;
            case "l":
                nextSquareType = squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()-1];
                if (nextSquareType < 0) {
                    return true;
                }
                break;
            case "r":
                nextSquareType = squareType[Counters.get(colour).getGridY()][Counters.get(colour).getGridX()+1];
                if (nextSquareType < 0) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Help command for when "help" is inputted by the user
     */
    private void helpCommand() {
        infoField.append("Commands: \nMove Player Piece\n - move (direction)\n" +
                "\nQuit Game\n - quit"+"\nEnd turn\n - end");
    }

    /**
     * Creates the weapons, and finally adds it to the board
     */
    private void initialiseWeapons(Weapons weapons) {
        for (Weapon w : weapons) {
            board.add(w);
        }
    }

    /**
     * Creates each counter and sets its location, and finally adds it to the board
     */
    private void initialiseCounters(Counters counters) {
        for (Counter c : counters) {
            board.add(c);
        }

    }

    /**
     * Adds each section of the GUI to the board
     */
    private void addComponents() {
        add(scrollPane, "East");
        add(userInput, "South");
        add(board, "Center");
    }
}