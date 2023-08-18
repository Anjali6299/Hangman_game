import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class hangman implements ActionListener {

    String illegalChar = ".1,8!?2:;(3){4}9[5]@*~/\\6–#$7&^%0|`";
    Random shuffle = new Random();
    static int vibrationLength = 5;
    static int vibrationSpeed = 1;

    String incorrectGuesses = "";
    String selectedWord;
    String progressDone;
    String progress;
    String command;
    String input;

    char [] wordToGuess;
    char [] guesses;
    String [] wordFile;

    int att = 1;
    int wins = 0;
    int losses = 0;
    int lives = 6;
    int gameState = 0;
    int gameInstance = 0;
    int keyboardState = 0;


    JFrame mainWindow = new JFrame("Hangman");
    ImageIcon icon = new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\Others/titleicon.png");

    //Menubar
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("Menu");
    JMenuItem menuHome = new JMenuItem ("Home");
    JMenuItem menuResume = new JMenuItem ("Resume");
    JMenuItem menuStart = new JMenuItem ("Restart");
    JLabel scoreCount = new JLabel();

    //Home
    JLabel title = new JLabel(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\Others/Title.png"));
    JLabel hangmanAnimation = new JLabel(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\Others/hangman2.gif"));
    JButton buttonNewGame = new JButton(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonSprites/newgame.png"));
    JButton buttonResume = new JButton(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonSprites/resume1.png"));
    JButton buttonOptions = new JButton(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonSprites/buttonOptions.png"));
    JButton buttonExit = new JButton(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonSprites/exit1.png"));
    JLabel eye = new JLabel(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\Others/img.png"));
    JLabel credits = new JLabel("Created By Anjali");

    //Game
    JLabel hint = new JLabel();
    JLabel lineTop = new JLabel();
    JLabel attemptCount = new JLabel("Attempt #" + att);
    JLabel lifeCount = new JLabel("Lives left: " + lives);
    JLabel progressVisual = new JLabel();
    JLabel progressWord = new JLabel();
    JLabel lineBottom = new JLabel();
    JLabel guide = new JLabel("Enter a letter: ");
    JButton buttonKeyboard = new JButton(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonSprites/buttonKeyboard.png"));
    JTextField entry = new JTextField();
    JButton submit = new JButton("Submit");
    JButton endPlayAgain = new JButton("Play Again");
    JButton endHome = new JButton("Leave");

    //Keyboard
    JPanel keyboardWindow = new JPanel();
    JPanel keyPanel = new JPanel();

    //Window
    public hangman() {

        //Frame

        mainWindow.setSize(440, 450);
        mainWindow.setResizable(false);
        mainWindow.setIconImage(icon.getImage());
        mainWindow.getContentPane().setBackground(Color.WHITE);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setLayout(null);

        //Action Listeners
        buttonResume.addActionListener(this);
        buttonNewGame.addActionListener(this);
        buttonExit.addActionListener(this);
        entry.addActionListener(this);
        buttonKeyboard.addActionListener(this);
        submit.addActionListener(this);
        endPlayAgain.addActionListener(this);
        endHome.addActionListener(this);

        //Hover Effect Mouse Listeners
        buttonResume.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonResume.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonHovers/resume2.png"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonResume.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonSprites/resume1.png"));
            }
        });

        buttonNewGame.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonNewGame.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonHovers/hoverNewGame.png"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonNewGame.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonSprites/newgame.png"));
            }
        });

        buttonExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonExit.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonHovers/exit2.png"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonExit.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonSprites/exit1.png"));
            }
        });

        buttonKeyboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonKeyboard.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonHovers/hoverKeyboard.png"));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonKeyboard.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images\\GUI\\ButtonHovers/buttonKeyboard.png"));
            }
        });


        //Menu Bar
        menuBar.setVisible(false);
        mainWindow.setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        fileMenu.add(menuHome);
        fileMenu.add(menuResume);
        fileMenu.add(menuStart);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(scoreCount);

        menuHome.addActionListener(this);
        menuResume.addActionListener(this);
        menuStart.addActionListener(this);

        // Keyboard
        mainWindow.add(keyboardWindow);
        keyboardWindow.setBounds(-1, 270, 420, 100);
        keyboardWindow.setBackground(Color.decode("#f5f5f5"));
        keyboardWindow.setVisible(false);
        keyboardWindow.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1), BorderFactory.createEmptyBorder(4, 5, 5, 5)));
        /*-----Key Container-----*/
        keyboardWindow.add(keyPanel);
        keyPanel.setBackground(Color.decode("#f5f5f5"));
        keyPanel.setPreferredSize(new Dimension(327, 79));
        createButtons(keyPanel);

        startHomeScreen();
    }

    //Keyboard Buttons
    public void createButtons(JPanel keyPanel) {
        JButton[] buttons = new JButton[26];
        String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z" };

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(letters[i]);
            buttons[i].setPreferredSize(new Dimension(27, 20));
            buttons[i].setFocusPainted(false);
            buttons[i].setBorder(BorderFactory.createRaisedBevelBorder());
            buttons[i].setBackground(Color.decode("#fafafa"));
            buttons[i].setMargin(new Insets(0, 0, 0, 0));
            buttons[i].setFont(new Font("Calibri", Font.BOLD, 11));
            buttons[i].setActionCommand(letters[i]);
            buttons[i].addActionListener(this);
            buttons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered (java.awt.event.MouseEvent me) {
                    JButton event = (JButton) me.getSource();
                    event.setBorder(BorderFactory.createLineBorder(Color.decode("#f5f5f5"), 2));
                }
                public void mousePressed (java.awt.event.MouseEvent me) {
                    JButton event = (JButton) me.getSource();
                    event.setBorder(BorderFactory.createLoweredBevelBorder());
                    event.setContentAreaFilled(false);
                }

                public void mouseExited (java.awt.event.MouseEvent me) {
                    JButton event = (JButton) me.getSource();
                    event.setBorder(BorderFactory.createRaisedBevelBorder());
                    event.setContentAreaFilled(true);
                }
            });

            keyPanel.add(buttons[i]);
        }
    }

    //First Home Screen
    public void startHomeScreen() {

        mainWindow.getContentPane().setBackground(Color.white);

        //Title
        title.setBounds(20, 52, 250, 60);
        title.setEnabled(true);
        title.setVisible(true);
        title.setBackground(Color.gray);
        mainWindow.add(title);

        //Hangman Animation
        hangmanAnimation.setBounds(270, 32, 160, 185);
        hangmanAnimation.setEnabled(true);
        hangmanAnimation.setVisible(true);
        mainWindow.add(hangmanAnimation);

        //Resume Button
        buttonResume.setVisible(false);

        //New Game Button
        buttonNewGame.setBounds(119, 170, 120, 50);
        buttonNewGame.setContentAreaFilled(false);
        buttonNewGame.setBorderPainted(false);
        buttonNewGame.setOpaque(false);
        buttonNewGame.setEnabled(true);
        buttonNewGame.setVisible(true);
        mainWindow.add(buttonNewGame);



        //Exit Button
        buttonExit.setBounds(124, 220, 120, 50);
        buttonExit.setContentAreaFilled(false);
        buttonExit.setBorderPainted(false);
        buttonExit.setOpaque(false);
        buttonExit.setEnabled(true);
        buttonExit.setVisible(true);
        mainWindow.add(buttonExit);

        //Credits
        credits.setFont(new Font("Calibri", Font.PLAIN, 9));
        credits.setBounds(25, 363, 290, 20);
        credits.setEnabled(true);
        credits.setVisible(true);
        mainWindow.add(credits);

        //creator
        eye.setBounds(5, 362, 20, 20);
        eye.setEnabled(true);
        eye.setVisible(true);
        mainWindow.add(eye);

        //Loads Main Window After Everything's Done
        mainWindow.setVisible(true);

    }

    //Home Screen
    public void homeScreen() {

        mainWindow.getContentPane().setBackground(Color.WHITE);


        menuHome.setEnabled(false);

        hint.setVisible(false);
        lineTop.setVisible(false);
        attemptCount.setVisible(false);
        lifeCount.setVisible(false);
        progressVisual.setVisible(false);
        progressWord.setVisible(false);
        lineBottom.setVisible(false);
        guide.setVisible(false);
        entry.setVisible(false);
        buttonKeyboard.setVisible(false);
        submit.setVisible(false);
        endPlayAgain.setVisible(false);
        endHome.setVisible(false);

        //Title
        title.setBounds(20, 52, 250, 60);
        title.setEnabled(true);
        title.setVisible(true);
        mainWindow.add(title);

        //Hangman Animation
        hangmanAnimation.setBounds(270, 32, 160, 185);
        hangmanAnimation.setEnabled(true);
        hangmanAnimation.setVisible(true);
        mainWindow.add(hangmanAnimation);

        //Resume Button
        buttonResume.setBounds(130, 180, 120, 50);
        buttonResume.setContentAreaFilled(false);
        buttonResume.setBorderPainted(false);
        buttonResume.setOpaque(false);
        buttonResume.setEnabled(true);
        buttonResume.setVisible(true);
        mainWindow.add(buttonResume);

        //New Game Button
        buttonNewGame.setBounds(119, 210, 120, 50);
        buttonNewGame.setContentAreaFilled(false);
        buttonNewGame.setBorderPainted(false);
        buttonNewGame.setOpaque(false);
        buttonNewGame.setEnabled(true);
        buttonNewGame.setVisible(true);
        mainWindow.add(buttonNewGame);



        //Exit Button
        buttonExit.setBounds(125, 250, 120, 50);
        buttonExit.setContentAreaFilled(false);
        buttonExit.setBorderPainted(false);
        buttonExit.setOpaque(false);
        buttonExit.setEnabled(true);
        buttonExit.setVisible(true);
        mainWindow.add(buttonExit);

        //Credits
        credits.setFont(new Font("Calibri", Font.PLAIN, 9));
        credits.setBounds(25, 363, 290, 20);
        credits.setEnabled(true);
        credits.setVisible(true);
        mainWindow.add(credits);

        //creator
        eye.setBounds(5, 362, 20, 20);
        eye.setEnabled(true);
        eye.setVisible(true);
        mainWindow.add(eye);

    }

    //Game Initialization
    public void newGame() {

        gameState = 0;
        mainWindow.getContentPane().setBackground(Color.WHITE);

        menuHome.setEnabled(true);
        menuResume.setVisible(false);
        menuStart.setText("Restart");


        title.setVisible(false);
        hangmanAnimation.setVisible(false);
        buttonResume.setVisible(false);
        buttonNewGame.setVisible(false);
        buttonOptions.setVisible(false);
        buttonExit.setVisible(false);
        eye.setVisible(false);
        credits.setVisible(false);

        endPlayAgain.setVisible(false);
        endHome.setVisible(false);

        //Reset Game
        att = 1;
        lives = 6;
        incorrectGuesses = "";

        //Hint
        getRandomList();
        hint.setBounds(35, 10, 290, 30);
        hint.setHorizontalAlignment(SwingConstants.CENTER);

        //Top Line
        lineTop.setText("------------------------------------------------------------------------------------------------");
        lineTop.setBounds(20, 30, 440, 30);

        //Attempt Count
        attemptCount.setBounds(25, 50, 290, 30);

        //Life Count
        lifeCount.setBounds(240, 50, 290, 30);

        //Hangman
        progressVisual.setBounds(110, 82, 100, 150);
        printHangman();

        //Word Progression
        progressWord.setText("Word to Guess: " + progress);
        progressWord.setBounds(14, 240, 320, 30);
        progressWord.setHorizontalAlignment(SwingConstants.CENTER);

        //Bottom Line
        lineBottom.setText("---------------------------------------------------------------------------------------------");
        lineBottom.setBounds(20, 260, 440, 30);

        //Guide
        guide.setBounds(20, 280, 320, 30);
        guide.setHorizontalAlignment(SwingConstants.CENTER);

        //Display Keyboard Button
        buttonKeyboard.setBounds(5, 349, 22, 14);
        buttonKeyboard.setContentAreaFilled(false);
        buttonKeyboard.setBorderPainted(false);
        buttonKeyboard.setFocusPainted(false);
        buttonKeyboard.setOpaque(true);

        //Input
        entry.setText("Enter your guess");
        entry.setMargin(new Insets(0, 10, 0, 10));
        entry.setBounds(68, 315, 110, 30);

        //Submission Button
        submit.setBounds(193, 315, 90, 29);
        submit.setFocusPainted(false);

        //Play Again Button
        endPlayAgain.setBounds(75, 315, 110, 29);
        endPlayAgain.setFocusPainted(false);

        //Home Button
        endHome.setBounds(200, 315, 80, 29);
        endHome.setFocusPainted(false);

        //Adds Game GUI to Window
        mainWindow.add(hint);
        mainWindow.add(lineTop);
        mainWindow.add(attemptCount);
        mainWindow.add(lifeCount);
        mainWindow.add(progressVisual);
        mainWindow.add(progressWord);
        mainWindow.add(lineBottom);
        mainWindow.add(guide);
        mainWindow.add(entry);
        mainWindow.add(buttonKeyboard);
        mainWindow.add(submit);
        mainWindow.add(endPlayAgain);
        mainWindow.add(endHome);

        //Enables Game GUI
        hint.setVisible(true);
        lineTop.setVisible(true);
        attemptCount.setVisible(true);
        lifeCount.setVisible(true);
        progressVisual.setVisible(true);
        progressWord.setVisible(true);
        lineBottom.setVisible(true);
        guide.setVisible(true);
        entry.setVisible(true);
        buttonKeyboard.setVisible(true);
        submit.setVisible(true);

        guide.setText("Enter a letter: ");
        guide.setForeground(Color.darkGray);

        //Initializes Words and Updates Variables
        printWord();
        updateVariables();

    }

    //Resume Game GUI
    public void resumeGame() {

        mainWindow.getContentPane().setBackground(Color.WHITE);

        menuHome.setEnabled(true);
        menuResume.setVisible(false);

        //Removes Home GUI
        title.setVisible(false);
        hangmanAnimation.setVisible(false);
        buttonResume.setVisible(false);
        buttonNewGame.setVisible(false);
        buttonOptions.setVisible(false);
        buttonExit.setVisible(false);
        eye.setVisible(false);
        credits.setVisible(false);

        //Enables Game GUI
        hint.setVisible(true);
        lineTop.setVisible(true);
        attemptCount.setVisible(true);
        lifeCount.setVisible(true);
        progressVisual.setVisible(true);
        progressWord.setVisible(true);
        lineBottom.setVisible(true);
        buttonKeyboard.setVisible(true);
        guide.setVisible(true);
        buttonKeyboard.setBounds(5, 350, 22, 14);

        /*-----Checks if Game is Complete-----*/
        if (gameState == 1 || gameState == 2) {
            entry.setVisible(false);
            submit.setVisible(false);
            buttonKeyboard.setVisible(false);
            endPlayAgain.setVisible(true);
            endHome.setVisible(true);
        }
        else {
            entry.setVisible(true);
            submit.setVisible(true);
            buttonKeyboard.setVisible(true);
            endPlayAgain.setVisible(false);
            endHome.setVisible(false);
        }

    }


    public void updateVariables() {
        printHangman();
        reprintWord();
        entry.setText(null);
        scoreCount.setText("W: " + wins + " L: " + losses + "  ");
        progressWord.setText("Guess the word: " + progress);
        attemptCount.setText("Attempt - " + att);
        lifeCount.setText("Lives left: " + lives);
    }

    //Random Word Select
    public String getWord() {
        wordFile = textFile();

        int n = wordFile.length;
        int r = shuffle.nextInt(n);
        String word = wordFile[r];

        return word;
    }

    //Select Random Word
    public String getRandomList() {
        String chosenList = "";

        int i = shuffle.nextInt(5);
        switch (i) {
            /*-----Word Banks-----*/
            case 0:
                String ourPlanet = "E:\\hangman_game\\Hangman_game\\Files/WordLists/ourPlanets.txt";
                chosenList = ourPlanet;
                hint.setText("Hint:Planet name");
                break;

            case 1:
                String basicWords = "E:\\hangman_game\\Hangman_game\\Files/WordLists/basicWords.txt";
                chosenList = basicWords;
                hint.setText("Hint: Any English Word");
                break;

            case 2:
                String programmingTerms = "E:\\hangman_game\\Hangman_game\\Files/WordLists/codingJargons.txt";
                chosenList = programmingTerms;
                hint.setText("Hint: Programming Term");
                break;

            case 3:
                String dataTypes = "E:\\hangman_game\\Hangman_game\\Files/WordLists/dataTypes.txt";
                chosenList = dataTypes;
                hint.setText("Hint:Java Data Type ");
                break;

            case 4:
                String fruits = "E:\\hangman_game\\Hangman_game\\Files/WordLists/fruitList.txt";
                chosenList = fruits;
                hint.setText("Hint: Fruit name");
                break;
        }

        return chosenList;
    }

    //File Reader
    public String[] textFile() {
        BufferedReader reader = null;
        String wordBank = getRandomList();
        ArrayList<String> wordList = new ArrayList<String>();
        try {
            reader = new BufferedReader(new FileReader(wordBank));
            String s = null;
            while ((s = reader.readLine()) != null) {
                wordList.add(s);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        } return wordList.toArray(new String[wordList.size()]);
    }


    public void printWord() {
        progress = "";
        selectedWord = getWord();
        progressDone = selectedWord.replaceAll(".(?!$)", "$0 ");
        wordToGuess = selectedWord.toLowerCase().toCharArray();
        guesses = new char[wordToGuess.length];
        for (int i = 0; i < guesses.length; i++) {
            guesses[i] = '?';
            progress += guesses[i] + " ";
        }
    }

    //Updates Progress
    public void reprintWord() {
        progress = "";
        for (int i = 0; i < guesses.length; i++) {
            progress += guesses[i] + " ";
        }
    }

    //Displays Guessed Word
    public void displayIncorrect() {
        progressWord.setText("Incorrect Guesses: " + incorrectGuesses.toLowerCase());
        javax.swing.Timer timer = new javax.swing.Timer (3000, e -> {
            progressWord.setText("Guess the word: " + progress);
        });
        timer.setRepeats(false);
        timer.start();
    }

    //Action Listener
    public void actionPerformed (ActionEvent e) {

        // Home
        if (e.getSource() == menuHome || e.getSource() == endHome) {
            //Removes Menu Bar
            menuBar.setVisible(false);
            keyboardWindow.setVisible(false);
            keyboardState = 0;

            menuResume.setVisible(true);
            mainWindow.setTitle("Home");
            System.out.println("\nGoing to Home Screen...");
            homeScreen();
        }

        //Resume
        if (e.getSource() == menuResume || e.getSource() == buttonResume) {
            //Adds Menu Bar
            menuBar.setVisible(true);
            mainWindow.setTitle("Game #" + gameInstance);
            System.out.println("\nResuming Game...");
            resumeGame();
        }

        //New Game
        else if (e.getSource() == menuStart || e.getSource() == buttonNewGame || e.getSource() == endPlayAgain) {
            //Adds Menu Bar
            menuBar.setVisible(true);

            gameInstance++;
            keyboardState = 0;
            mainWindow.setTitle("Game #" + gameInstance);
            System.out.println("\nStarting New Game...");
            newGame();
        }

        //Exit Menu
        else if (e.getSource() == buttonExit) {
            mainWindow.setTitle("Exit");
            System.out.println("\nExiting Game...");
            JOptionPane.showMessageDialog(mainWindow, "   Thanks for playing!!", "Message", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }

        //Keyboard
        else if (e.getSource() == buttonKeyboard) {
            if (keyboardState == 0) {
                keyboardWindow.setVisible(true);
                entry.setVisible(false);
                submit.setVisible(false);
                buttonKeyboard.setBounds(5, 250, 22, 14);
                keyboardState = 1;
            }
            else if (keyboardState == 1) {
                keyboardWindow.setVisible(false);
                entry.setVisible(true);
                submit.setVisible(true);
                buttonKeyboard.setBounds(5, 349, 22, 14);
                keyboardState = 0;
            }
        }

        //Submit Button
        else if (e.getSource() == submit || e.getSource() == entry) {

            input = entry.getText();


            if (!Arrays.equals(guesses, wordToGuess)) {


                if (input.length() != 1 || illegalChar.contains(input) || incorrectGuesses.contains(input.toUpperCase()) || progress.toUpperCase().contains(input.toUpperCase())) {


                    if (input.equalsIgnoreCase("answer")) {
                        guide.setForeground(Color.darkGray);
                        guide.setText("The word is: \"" + selectedWord + "\". )");
                        updateVariables();
                    }
                    else if (input.equalsIgnoreCase("heal")) {
                        lives = 6;
                        guide.setForeground(Color.PINK);
                        guide.setText("Healed. ( ͡° ͜ʖ ͡°)");
                        updateVariables();
                    }
                    else if (input.equalsIgnoreCase("incorrect")) {
                        updateVariables();
                        displayIncorrect();
                    }
                    else if (input.equalsIgnoreCase("end")) {
                        lives = 0;
                        updateVariables();
                    }
                    /*-----Errors-----*/
                    else if (input.trim().isEmpty()) {
                        guide.setForeground(Color.GRAY);
                        guide.setText("Blank input detected! Try again. ");
                        updateVariables();
                    }
                    else if (illegalChar.contains(input.trim())) {
                        guide.setForeground(Color.GRAY);
                        guide.setText("Illegal character detected! Try again. ");
                        updateVariables();
                    }
                    else if (input.length() > 1) {
                        guide.setForeground(Color.GRAY);
                        guide.setText("Multiple inputs detected! Try again. ");
                        updateVariables();
                    }
                    else if (incorrectGuesses.contains(input.toUpperCase())) {
                        guide.setForeground(Color.GRAY);
                        guide.setText("Letter is already guessed! Try another one.");
                        updateVariables();
                        displayIncorrect();
                    }
                    else if (progress.toUpperCase().contains(input.toUpperCase())) {
                        guide.setForeground(Color.GRAY);
                        guide.setText("Letter is already guessed! Try another one.");
                        updateVariables();
                    }
                    else {
                        System.out.println("\n Input Error Found.");
                        System.exit(-1);
                    }
                }


                else if (selectedWord.toLowerCase().contains(input.toLowerCase())) {
                    att++;
                    guide.setForeground(Color.darkGray);
                    for (int i = 0; i < wordToGuess.length; i++) {
                        if (input.toLowerCase().charAt(0) == wordToGuess[i]) {
                            guesses[i] = input.toLowerCase().charAt(0);
                            guide.setText("Letter \"" + input.toUpperCase() + "\" is correct. Keep it up! ");
                        }
                    }
                    updateVariables();
                }


                else if (!selectedWord.toLowerCase().contains(input.toLowerCase())) {
                    att++;
                    lives--;
                    guide.setForeground(Color.darkGray);
                    guide.setText("Letter \"" + input.toUpperCase() + "\" does not exist. Try again!");

                    vibrate(mainWindow);

                    //Updates Incorrect Guesses
                    if (!selectedWord.toUpperCase().contains(input.toUpperCase())) {
                        incorrectGuesses += input.toUpperCase();
                        if (lives != 0 && !Arrays.equals(guesses, wordToGuess)) {
                            incorrectGuesses += ", ";
                        }
                    }

                    updateVariables();
                }

                //Game Over
                if (lives == 0) {
                    entry.setVisible(false);
                    submit.setVisible(false);
                    buttonKeyboard.setVisible(false);
                    keyboardWindow.setVisible(false);
                    endPlayAgain.setVisible(true);
                    endHome.setVisible(true);

                    losses++;
                    gameState = 2;
                    updateVariables();
                    progressWord.setText("Guess the word: " + progressDone);
                    guide.setForeground(Color.RED);
                    guide.setText("Game over! ");
                    JOptionPane.showMessageDialog(mainWindow, " You lost ! The word was \"" + selectedWord + "\".", "Message", JOptionPane.PLAIN_MESSAGE);
                    menuStart.setText("New Game");
                }

            }


            if (Arrays.equals(guesses, wordToGuess) || input.trim().equalsIgnoreCase(selectedWord)) {
                entry.setVisible(false);
                submit.setVisible(false);
                buttonKeyboard.setVisible(false);
                keyboardWindow.setVisible(false);
                endPlayAgain.setVisible(true);
                endHome.setVisible(true);

                wins++;
                gameState = 1;
                updateVariables();
                progressWord.setText("Word to Guess: " + progressDone);
                guide.setForeground(Color.GREEN);
                guide.setText("Well done, you get to live another day!");
                JOptionPane.showMessageDialog(mainWindow, "You got the word in " + att + " attempts with " + lives + " lives left. Congratulations!", "Message", JOptionPane.PLAIN_MESSAGE);
                menuStart.setText("New Game");
            }

        }


        else if (e.getActionCommand().length() == 1) {

            keyboardWindow.setVisible(false);
            entry.setVisible(true);
            submit.setVisible(true);
            buttonKeyboard.setBounds(5, 349, 22, 14);
            keyboardState = 0;

            command = e.getActionCommand();

            if (!Arrays.equals(guesses, wordToGuess)) {

                //Error Handler
                if (incorrectGuesses.contains(command.toUpperCase()) || progress.contains(command.toLowerCase())) {
                    if (incorrectGuesses.contains(command.toUpperCase())) {
                        guide.setForeground(Color.GRAY);
                        guide.setText("Letter is already guessed! Try another one.");
                        updateVariables();
                        displayIncorrect();
                    }
                    else if (progress.contains(command.toLowerCase())) {
                        guide.setForeground(Color.GRAY);
                        guide.setText("Letter is already guessed! Try another one.");
                        updateVariables();
                    }
                }


                else if (selectedWord.toLowerCase().contains(command.toLowerCase())) {
                    att++;
                    guide.setForeground(Color.darkGray);
                    for (int i = 0; i < wordToGuess.length; i++) {
                        if (command.toLowerCase().charAt(0) == wordToGuess[i]) {
                            guesses[i] = command.toLowerCase().charAt(0);
                            guide.setText("Letter \"" + command.toUpperCase() + "\" is correct. Good job!");
                        }
                    }
                    updateVariables();
                }


                else if (!selectedWord.toLowerCase().contains(command.toLowerCase())) {
                    att++;
                    lives--;
                    guide.setForeground(Color.darkGray);
                    guide.setText("Letter \"" + command.toUpperCase() + "\" does not exist. Try again!");

                    vibrate(mainWindow);

                    //Updates Incorrect Guesses
                    if (!selectedWord.toUpperCase().contains(command.toUpperCase())) {
                        incorrectGuesses += command.toUpperCase();
                        if (lives != 0 && !Arrays.equals(guesses, wordToGuess)) {
                            incorrectGuesses += ", ";
                        }
                    }

                    updateVariables();
                }

                //Game Over
                if (lives == 0) {
                    entry.setVisible(false);
                    submit.setVisible(false);
                    buttonKeyboard.setVisible(false);
                    keyboardWindow.setVisible(false);
                    endPlayAgain.setVisible(true);
                    endHome.setVisible(true);

                    losses++;
                    gameState = 2;
                    updateVariables();
                    progressWord.setText("Word to Guess: " + progressDone);
                    guide.setForeground(Color.RED);
                    guide.setText("Game over! You've used up all your lives.");
                    JOptionPane.showMessageDialog(mainWindow, " You died! The word was \"" + selectedWord + "\".", "Message", JOptionPane.PLAIN_MESSAGE);
                    menuStart.setText("New Game");
                }
            }


            if (Arrays.equals(guesses, wordToGuess)) {
                entry.setVisible(false);
                submit.setVisible(false);
                buttonKeyboard.setVisible(false);
                keyboardWindow.setVisible(false);
                endPlayAgain.setVisible(true);
                endHome.setVisible(true);

                wins++;
                gameState = 1;
                updateVariables();
                progressWord.setText("Word to Guess: " + progressDone);
                guide.setForeground(Color.GREEN);
                guide.setText("Well done, you get to live another day!");
                JOptionPane.showMessageDialog(mainWindow, "You got the word in " + att + " attempts with " + lives + " lives left. Congratulations!", "Message", JOptionPane.PLAIN_MESSAGE);
                menuStart.setText("New Game");
            }

        }

    }

    //Vibrate Window
    public static void vibrate(JFrame mainWindow) {
        try {
            final int originalX = mainWindow.getLocationOnScreen().x;
            final int originalY = mainWindow.getLocationOnScreen().y;
            for (int i = 0; i < vibrationLength; i++) {
                Thread.sleep(10);
                mainWindow.setLocation(originalX, originalY + vibrationSpeed);
                Thread.sleep(10);
                mainWindow.setLocation(originalX, originalY - vibrationSpeed);
                Thread.sleep(10);
                mainWindow.setLocation(originalX + vibrationSpeed, originalY);
                Thread.sleep(10);
                mainWindow.setLocation(originalX, originalY);
            }
        }

        catch (Exception err) {
            err.printStackTrace();
        }
    }

    //Hangman Art
    public void printHangman() {
        switch (lives) {
            case 6:
                progressVisual.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images/HangmanArt/start.png"));
                break;

            case 5:
                progressVisual.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images/HangmanArt/1.png"));
                break;

            case 4:
                progressVisual.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images/HangmanArt/2.png"));
                break;

            case 3:
                progressVisual.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images/HangmanArt/3.png"));
                break;

            case 2:
                progressVisual.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images/HangmanArt/4.png"));
                break;

            case 1:
                progressVisual.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images/HangmanArt/5.png"));
                break;

            default:
                progressVisual.setIcon(new ImageIcon("E:\\hangman_game\\Hangman_game\\Files\\Images/HangmanArt/game_over.png"));
                break;
        }
    }

}



