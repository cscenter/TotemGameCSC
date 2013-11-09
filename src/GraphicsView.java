import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
/**
 * Класс, отвечающий за работу с игроками
 */
class GraphicsView extends JFrame{
    private Game myGame;
    private static int FRAME_SIZE;
    private char lastPressedKey;
    private boolean isPressed;

    private class MyPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            for (PlayerView playerView : playersView){
                Image image = Toolkit.getDefaultToolkit().getImage("data/052.jpg");
                g.drawImage(image, playerView.xСoordinate, playerView.yСoordinate, cardsView.cardSize, cardsView.cardSize, this);
                g.fillRect(playerView.xСoordinate - (int)(1.1 * cardsView.cardSize), playerView.yСoordinate,
                        cardsView.cardSize, cardsView.cardSize);
//                g.drawImage(image)
                //g.drawLine(playerView.xСoordinate, playerView.yСoordinate, playerView.xСoordinate+cardsView.cardSize, playerView.yСoordinate+10);
            }
//            g.drawLine(10,10,100,100);
        }
    }
    private class MyKeyListener implements KeyListener{
        public void keyTyped(KeyEvent k) {}
        // нажатие клавиши
        public void keyPressed(KeyEvent k) {
            lastPressedKey = k.getKeyChar();
            isPressed = true;
            return;
        }
        // отпускание нажатой клавиши
        public void keyReleased(KeyEvent k) {}
    }
    MyPanel myPanel;
    /**
     * во время запуска инициализируем playersView, потом game а потом уже запускаем саму игру
     */

    /**
     * Поля, которые нужно знать View о игроках
     */
    private class PlayerView{
        public int xСoordinate;
        public int yСoordinate;
        public double angle;
        public char openCardKey;
        public char catchTotemKey;
        public String playerViewName; //что делать с дублированием имени в PlayerView и Player?
        public PlayerView(char newOpenCardKey, char newCatchTotemKey, String name, double a){
            openCardKey = newOpenCardKey;
            catchTotemKey = newCatchTotemKey;
            playerViewName = name;
            angle = a;
            xСoordinate = (int)((FRAME_SIZE/3.5) * Math.sin(angle*Math.PI/180) + FRAME_SIZE / 2.2);
            yСoordinate = (int)((FRAME_SIZE/3.5) * Math.cos(angle*Math.PI/180) + FRAME_SIZE / 2.5);
//            System.out.println(xСoordinate + '_'+ yСoordinate);

        }
    }
    ArrayList <PlayerView> playersView;

    private class CardsView {
        public int cardSize;
        private ArrayList <File> cards;
        public CardsView(){
            File dir = new File("data/");
            cards = new ArrayList<>(Arrays.asList(dir.listFiles()));
            cardSize = FRAME_SIZE / 10;
        }
        public ArrayList<Integer> getCardsNumbers(){
            ArrayList<Integer> rezult = new ArrayList<>();
            for (int i = 0; i < cards.size(); i++){
                rezult.add(getCardNumber(i));
            }
            return rezult;
        }
        public int getCardNumber(int index){
            Pattern numberPattern = Pattern.compile("[0-9]+");
            Matcher numberMatcher = numberPattern.matcher(cards.get(index).getName());
            numberMatcher.find();
            return Integer.parseInt(numberMatcher.group());
        }
    }
    private CardsView cardsView;
    /**
     * ввести число игроков, а так же кнопки управления
     * по умолчанию:
     * 4 игрока, клавиши
     * q, w
     * e, r
     * t, y
     * u, i
     */
    private char getNewChar(String inputMessage, String errorOutputMessage){
        String inputString;
        char inputChar;
        Scanner scan = new Scanner(System.in);
        do{
            try{
                System.out.println(inputMessage);
                inputString = scan.nextLine();
                inputChar = inputString.charAt(0);
            } catch (StringIndexOutOfBoundsException e){
                System.out.printf("%s\n", errorOutputMessage);
                continue;
            }
            break;
        }while(true);
        return inputChar;
    }
    private void defaultSettings(ArrayList <String> rezultStrings){
        char inputOpenCardKey;
        char inputCatchTotemKey;
        int numberOfPeople = 4;
        inputOpenCardKey = 'q';
        inputCatchTotemKey = 'w';
        playersView.ensureCapacity(numberOfPeople);
        playersView.add(new PlayerView(inputOpenCardKey, inputCatchTotemKey, "Vasya", 0.0));
        rezultStrings.add("Vasya");

        inputOpenCardKey = 'e';
        inputCatchTotemKey = 'r';
        playersView.add(new PlayerView(inputOpenCardKey, inputCatchTotemKey, "Petya", 90.0));
        rezultStrings.add("Petya");

        inputOpenCardKey = 't';
        inputCatchTotemKey = 'y';
        playersView.add(new PlayerView(inputOpenCardKey, inputCatchTotemKey, "Gosha", 180.0));
        rezultStrings.add("Gosha");

        inputOpenCardKey = 'u';
        inputCatchTotemKey = 'i';
        playersView.add(new PlayerView(inputOpenCardKey, inputCatchTotemKey,"Manya", 270.0));
        rezultStrings.add("Manya");
        myPanel.repaint();
    }
    private int setNumberOfPlayers(){
        int numberOfPeople;
        Scanner scan = new Scanner(System.in);
        do {
            try{
                System.out.println("Please, insert number of players");
                numberOfPeople = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException | StringIndexOutOfBoundsException e){
                System.out.println("Integer must be integer, you, clever user! try again\n");
                scan.nextLine();
                continue;
            }
            System.out.printf("\n");
            if (numberOfPeople <= 1){
                System.out.printf("%d isn't correct number of players! try again\n", numberOfPeople);
            }else{
                break;
            }
        } while(true);
        return numberOfPeople;
    }
    private void notDefaultSettings(ArrayList<String> rezultStrings){
        int numberOfPeople = setNumberOfPlayers();
        Scanner scan = new Scanner(System.in);
        String inputString;
        char inputCatchTotemKey;
        char inputOpenCardKey;
        playersView.ensureCapacity(numberOfPeople);
        for (int i = 0; i < numberOfPeople; i++){
            String inputS;
            do {
                System.out.printf("player %d: insert your name\n", i+1);
                inputS = scan.nextLine();
                if (inputS.length()!=0){
                    break;
                }else{
                    System.out.println("Name can't be empty string! try again");
                }
            }while(true);
            rezultStrings.add(inputS);
            label:
            do {
                try{
                    System.out.printf("%s: insert key to open first card\n", inputS);
  //                  inputString = scan.nextLine();
//                    inputOpenCardKey = inputString.charAt(0);
                    Character tmp;// = inputOpenCardKey;
                    isPressed = false;
                    while (!(isPressed)){}
                    tmp = lastPressedKey;
                    inputOpenCardKey = tmp.toString().toLowerCase().charAt(0);
                    for (PlayerView p : playersView){
                        if ((inputOpenCardKey == p.catchTotemKey) || (inputOpenCardKey == p.openCardKey)){
                            System.out.printf("player %s already use this key. Try another one\n", p.playerViewName);
                            continue label;
                        }
                    }
                    break;
                }catch (StringIndexOutOfBoundsException e){
                    System.out.printf("%s, you know, what you just did?? You may drop the game!" +
                            " Thing about your behaviour and try once more!\n", inputS);
                }

            }while(true);
            label:
            do{
                try {
                    System.out.printf("%s: insert key to catch totem\n", inputS);
                    inputString = scan.nextLine();
                    inputCatchTotemKey = inputString.charAt(0);
                    Character tmp;// = inputCatchTotemKey;
                    while (!(isPressed)){}
                    tmp = lastPressedKey;
                    inputCatchTotemKey = tmp.toString().toLowerCase().charAt(0);
                    if (inputCatchTotemKey == inputOpenCardKey){
                        System.out.println("you already use this key for key that open last card! try another key");
                        continue;
                    }
                    for (PlayerView p : playersView){
                        if ((inputCatchTotemKey == p.catchTotemKey) || (inputCatchTotemKey == p.openCardKey)){
                            System.out.printf("player %s already use this key. Try another one\n", p.playerViewName);
                            continue label;
                        }
                    }
                    break;
                }catch (StringIndexOutOfBoundsException e){
                    System.out.printf("%s, you know, what you just did?? You may drop the game!" +
                            " Thing about your behaviour and try once more!\n", inputS);
                }

            }while (true);
            playersView.add(new PlayerView(inputOpenCardKey, inputCatchTotemKey, inputS,
                    (360.0 / numberOfPeople) * (playersView.size())));
        }

    }
    private ArrayList<String> startView(){
        ArrayList <String> rezultStrings = new ArrayList<>();
        boolean flag = true;
        int numberOfPeople = 4;
        do{
            Character inputChar;
            playersView = new ArrayList<>();
            inputChar = getNewChar("Use the default settings? (Y/N)", "No-No-NO, %username% ! Char means char, not empty string!");
            switch (inputChar.toString().toUpperCase().charAt(0)){ /* использовать параметры по умолчанию?*/
                case 'Y':           /*да*/
                    flag = false;
                    defaultSettings(rezultStrings);
                    break;
                case 'N':               /*нет*/
                    flag = false;
                    notDefaultSettings(rezultStrings);
                    break;
                default:
                    System.out.println("What are you doing?? Try once more!");
            }
        } while ((flag));

        /*показываем то, что получаем в итоге*/
        System.out.println("So, we have:");
        System.out.printf("Number of people: %d\n", numberOfPeople);
        for (PlayerView p : playersView){
            System.out.printf("Player %s has '%c' as key to open last card and '%c' as key to catch totem\n",
                    p.playerViewName, p.openCardKey, p.catchTotemKey);
        }
        return rezultStrings;
    }

    void printInformationAboutRound(){
        System.out.printf("name:            ");
        for (int i = 0; i < myGame.getPlayersCount(); i++){
            System.out.printf("%15s", myGame.getPlayer(i).getName());
        }
        System.out.printf("    under totem: \nall cards:     ");
        for (int i = 0; i < myGame.getPlayersCount(); i++){
            System.out.printf("%15s", myGame.getPlayer(i).getCardsCount());
        }

        System.out.printf("%15d\nclose cards    ", myGame.getCardsUnderTotemCount());
        for (int i = 0; i < myGame.getPlayersCount(); i++){
            System.out.printf("%15s", myGame.getPlayer(i).getCloseCardsCount());
        }
        System.out.printf("\nopen cards:    ");
        for (int i = 0; i < myGame.getPlayersCount(); i++){
            System.out.printf("%15s", myGame.getPlayer(i).getOpenCardsCount());
        }

        System.out.printf("\nlast open card:");
        for (int i = 0; i < myGame.getPlayersCount(); i++){
            if (myGame.getPlayer(i).getOpenCardsCount() > 0){
                System.out.printf("%15d", myGame.getPlayer(i).getTopOpenedCard().getCardNumber());
            } else {
                System.out.printf("              -");

            }
        }

        System.out.printf("\n\n");

        System.out.printf("Round %d, it's %s's turn\n", myGame.getTurnNumber(),
                myGame.getPlayer(myGame.getPlayerWhoWillGo()).getName());

    }

    int chooseOneOfPlayers(ArrayList<Integer> playersIndex){
        int looser;
        Scanner scan = new Scanner(System.in);
        do {
            try{
                System.out.println("Please, choose a player from this list");
                System.out.printf("name: ");
                for (Integer i : playersIndex){
                    System.out.printf("%15s", myGame.getPlayer(i).getName());
                }
                System.out.printf("\n");
                System.out.printf("type: ");
                for (int i = 0; i < playersIndex.size(); i++){
                    System.out.printf("%15d", i);
                }
                System.out.printf("\n");
                looser = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException | StringIndexOutOfBoundsException e){
                System.out.println("Integer must be integer, you, clever user! try again\n");
                scan.nextLine();
                continue;
            }
            if ((looser < 0) || (looser >= playersIndex.size())){
                System.out.printf("%d isn't correct number of player! try again\n", looser);
            }else{
                break;
            }
        } while(true);
        return looser;

    }
    public void run(){
        while (!(myGame.isGameEnded())){
            String inputString;
            Scanner scan = new Scanner(System.in);
            char inputChar;
            //           myGame.isRoundEnded = false;
            //          do{

            printInformationAboutRound();
            System.out.printf("insert key:\n");
            try {
                inputString = scan.nextLine();
                //          ReadableByteChannel in = Channels.newChannel(System.in);

                //            ByteBuffer charBuffer = ByteBuffer.allocate(512);

                //SelectionKey key = in.register
                //              in.read(charBuffer);
                //        charBuffer.rewind();
                //                 inputChar = charBuffer.getChar(0);
                isPressed = false;
                while (!(isPressed)){}
                inputChar = lastPressedKey;
                //inputChar = inputString.charAt(0);
                inputChar = (new Character(inputChar)).toString().toLowerCase().charAt(0);
            }catch (StringIndexOutOfBoundsException e){
                System.out.printf("You can't use empty string!\n");
                continue;
//                } catch (IOException e) {
                //                  e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            boolean suchKeyHere = false;
            Game.ResultOfMakeMove resultOfMakeMove = Game.ResultOfMakeMove.INCORRECT;
            int whoPlayed = 0;
            for (int i = 0; i < myGame.getPlayersCount(); i++){
                if (playersView.get(i).openCardKey == inputChar){
                    resultOfMakeMove = myGame.makeMove(i, Game.WhatPlayerDid.OPEN_NEW_CARD);
                    suchKeyHere = true;
                    whoPlayed = i;
                    break;
                }
                if (playersView.get(i).catchTotemKey == inputChar){
                    resultOfMakeMove = myGame.makeMove(i, Game.WhatPlayerDid.TOOK_TOTEM);
                    suchKeyHere = true;
                    whoPlayed = i;
                    break;
                }
            }
            if (!(suchKeyHere)){
                System.out.println("Nobody have such key. Try again.\n");
                continue;
            }
            switch (resultOfMakeMove){
                case INCORRECT:
                    System.out.printf("It's not your turn, %s. Don't hurry!\n",
                            myGame.getPlayer(whoPlayed).getName());
                    break;
                case TOTEM_WAS_CATCH_CORRECT:
                    System.out.printf("You won duel, %s! All your open cards and all cards under totem go to your opponent\n",
                            myGame.getPlayer(whoPlayed).getName());
                    break;
                case TOTEM_WAS_CATCH_INCORRECT:
                    System.out.printf("You mustn't take totem, %s! So you took all open cards!\n",
                            myGame.getPlayer(whoPlayed).getName());
                    break;
                case CARD_OPENED:

                    System.out.printf("%s open next card\n",
                            myGame.getPlayer(whoPlayed).getName());
                    break;
                case NOT_DEFINED_CATCH:
                    ArrayList <Integer> possibleLosers = myGame.checkDuelWithPlayer(myGame.getPlayer(whoPlayed));
                    if (myGame.getGameMode() == Game.GameMode.CATCH_TOTEM_MODE){
                        label:
                        do{
                            inputChar = getNewChar("You catch totem while there were a duel with you AND card 'arrows in'. Do you" +
                                    "want to use effect of won duel or of card? type (D/C)", "Try again!");
                            inputChar = (new Character(inputChar)).toString().toLowerCase().charAt(0);
                            switch (inputChar){
                                case 'd':
                                    if (possibleLosers.size() == 1){
                                        System.out.printf("All cards go to your opponent, %s\n", myGame.getPlayer(possibleLosers.get(0)));
                                        myGame.afterDuelMakeMove(whoPlayed, possibleLosers.get(0));
                                    }
                                    break label;
                                case 'c':
                                    System.out.println("You put all cards under totem");
                                    myGame.arrowsInMakeMove(whoPlayed);
                                    break label;
                                default:
                                    System.out.println("try again");
                            }
                        }while (true);
                    }
                    int looser = chooseOneOfPlayers(possibleLosers);
                    myGame.afterDuelMakeMove(whoPlayed,looser);
                    System.out.printf("All cards go to your opponent, %s\n", myGame.getPlayer(looser).getName());
                    break;
                case ALL_CARDS_OPENED:
                    System.out.println("All players will open top cards. To do this, press Enter");
                    scan.nextLine();
                    myGame.openAllTopCards();
                default:
            }

//            }while (!(myGame.isRoundEnded));
        }
        for (int i = 0; i < myGame.getPlayersCount(); i++){
            if (myGame.getPlayer(i).getCardsCount() == 0){
                System.out.printf("Player %s won! It's very good :)\n", myGame.getPlayer(i).getName());
            }
        }
    }
    public GraphicsView(){
        FRAME_SIZE = (Toolkit.getDefaultToolkit().getScreenSize().getHeight() > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) ?
        (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() : (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
        add(myPanel = new MyPanel());
        pack();
        addKeyListener(new MyKeyListener());
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardsView = new CardsView();
        myGame = new Game(startView(), cardsView.getCardsNumbers());
    }
}