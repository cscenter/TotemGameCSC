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
    private boolean allOpenFlag;
    private boolean catchTotemModeFlag;
    private boolean multyDuelFlag;
    private int whoPlayed;
    String directory = "data/";
    private class MyPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            FRAME_SIZE = (Toolkit.getDefaultToolkit().getScreenSize().getHeight() > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) ?
            (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() : (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
            cardsView.cardSize = FRAME_SIZE / 10;
            g.clearRect((int)(FRAME_SIZE/2.2)-cardsView.cardSize/2-30, (int)(FRAME_SIZE/2.2)-cardsView.cardSize/2-30,
                    (int)(cardsView.cardSize*1.8), cardsView.cardSize+50);
            g.drawChars((new Integer(myGame.getCardsUnderTotemCount())).toString().toCharArray(), 0,
                    Integer.toString(myGame.getCardsUnderTotemCount()).length(),(int)(FRAME_SIZE/2.2)-cardsView.cardSize/2+10, (int)(FRAME_SIZE/2.2)+cardsView.cardSize/2+20);
            if (myGame.getCardsUnderTotemCount()!=0){
                g.drawRect((int)(FRAME_SIZE/2.2)-cardsView.cardSize/2, (int)(FRAME_SIZE/2.2)-cardsView.cardSize/2, cardsView.cardSize, cardsView.cardSize);
            }
            g.drawOval((int)(FRAME_SIZE/2.2)-10, (int)(FRAME_SIZE/2.2)-10,20,20);
            for (int i=0; i<playersView.size(); i++){
                PlayerView playerView = playersView.get(i);
                playerView.xСoordinate = (int)((FRAME_SIZE/3.5) * Math.sin(playerView.angle*Math.PI/180) + FRAME_SIZE / 2.2);
                playerView.yСoordinate = (int)((FRAME_SIZE/3.5) * Math.cos(playerView.angle*Math.PI/180) + FRAME_SIZE / 2.5);
                g.drawChars(playerView.playerViewName.toCharArray(), 0, playerView.playerViewName.length(), playerView.xСoordinate-cardsView.cardSize/3, playerView.yСoordinate -20);
                g.clearRect(playerView.xСoordinate-(int)(cardsView.cardSize*2.1), playerView.yСoordinate, (int)(2.5*cardsView.cardSize), (int)(cardsView.cardSize*2.5));
                g.drawChars(Integer.toString(myGame.getPlayer(i).getOpenCardsCount()).toCharArray(), 0,
                        Integer.toString(myGame.getPlayer(i).getOpenCardsCount()).length(), playerView.xСoordinate+10, playerView.yСoordinate+cardsView.cardSize+20);

                if (myGame.getPlayer(i).getOpenCardsCount()!=0){
                    Image image = Toolkit.getDefaultToolkit().getImage(directory+
                            +myGame.getPlayer(i).getTopOpenedCard().getCardNumber()+".jpg");
                    g.drawImage(image, playerView.xСoordinate, playerView.yСoordinate, cardsView.cardSize, cardsView.cardSize, this);
                }
                g.drawChars(Integer.toString(myGame.getPlayer(i).getCloseCardsCount()).toCharArray(), 0,
                        Integer.toString(myGame.getPlayer(i).getCloseCardsCount()).length(), playerView.xСoordinate-cardsView.cardSize+10, playerView.yСoordinate+cardsView.cardSize+20);

                if (myGame.getPlayer(i).getCloseCardsCount()!=0){
                    g.fillRect(playerView.xСoordinate - (int)(1.1 * cardsView.cardSize), playerView.yСoordinate,
                            cardsView.cardSize, cardsView.cardSize);

                }
            }
            if (catchTotemModeFlag){
                g.drawChars(("Do you want to use effect of won duel or of Arrows-In card? Clicked on Totem/player").toCharArray(), 0,
                        ("Do you want to use effect of won duel or of Arrows-In card? Clicked on Totem/player").length(), 30, FRAME_SIZE-50);
            }else{
                g.clearRect(20, FRAME_SIZE-70, FRAME_SIZE-30, 70);
            }
            if (multyDuelFlag){
                g.drawChars("You one duel, here is several loosers. clicked to one of them".toCharArray(), 0, "You one duel, here is several loosers. clicked to one of them".length(),
                        30, FRAME_SIZE-50);
            }else{
                g.clearRect(20, FRAME_SIZE-70, FRAME_SIZE-30, 70);
            }

//            g.drawLine(10,10,100,100);
        }
    }
    private class MyMouseListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            if (catchTotemModeFlag){
                Point p = e.getLocationOnScreen();
                ArrayList <Integer> possibleLosers = myGame.checkDuelWithPlayer(myGame.getPlayer(whoPlayed));
                for (int i=0; i<playersView.size();i++){
                    if (playersView.get(i).isIn(p)){
                        int looser = i;
                        myGame.afterDuelMakeMove(whoPlayed,looser);
                        catchTotemModeFlag = false;
                        break;
                    }
                }
                if (p.distance(FRAME_SIZE/2.2,FRAME_SIZE/2.2)<20){
                    catchTotemModeFlag = false;
                    myGame.arrowsInMakeMove(whoPlayed);
                    catchTotemModeFlag = false;
                }
            }
            if (multyDuelFlag){
                Point p = e.getLocationOnScreen();
                ArrayList <Integer> possibleLosers = myGame.checkDuelWithPlayer(myGame.getPlayer(whoPlayed));
                for (int i=0; i<playersView.size();i++){
                    if (playersView.get(i).isIn(p)){
                        int looser = i;
                        myGame.afterDuelMakeMove(whoPlayed,looser);
                        multyDuelFlag = false;
                        break;
                    }
                }
            }


        }

//        System.out.printf("All cards go to your opponent, %s\n", myGame.getPlayer(looser).getName());


        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
    private class MyKeyListener implements KeyListener{
        @Override
        public void keyTyped(KeyEvent k) {}
        // нажатие клавиши
        @Override
        public void keyPressed(KeyEvent k) {
            if (!allOpenFlag){
                lastPressedKey = k.getKeyChar();
                Character inputChar;
                inputChar = lastPressedKey;
                inputChar = (new Character(inputChar)).toString().toLowerCase().charAt(0);
                boolean suchKeyHere = false;
                Game.ResultOfMakeMove resultOfMakeMove = Game.ResultOfMakeMove.INCORRECT;
                whoPlayed = 0;
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
                            catchTotemModeFlag = true;
                        }else{
                            multyDuelFlag = true;
                        }
                        break;
                    case ALL_CARDS_OPENED:
                        allOpenFlag = true;
                        System.out.println("All players will open top cards. To do this, press Enter");
                        break;
                    default:
                        break;
                }

                for (int i = 0; i < myGame.getPlayersCount(); i++){
                    if (myGame.getPlayer(i).getCardsCount() == 0){
                        System.out.printf("Player %s won! It's very good :)\n", myGame.getPlayer(i).getName());
                    }
                }
                myPanel.repaint();
            }else{

                if (k.getKeyCode()==KeyEvent.VK_ENTER){
                    myGame.openAllTopCards();
                    allOpenFlag = false;
                }

            }
            myPanel.repaint();
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
        public boolean isIn(Point p){
            if ((p.getX()<xСoordinate+cardsView.cardSize)&&(p.getX()>xСoordinate-cardsView.cardSize)){
                if ((p.getY()<yСoordinate+cardsView.cardSize+40)&&(p.getY()>yСoordinate-40)){
                    return true;
                }
            }
            return false;
        }
    }
    ArrayList <PlayerView> playersView;

    private class CardsView {
        public int cardSize;
        private ArrayList <File> cards;
        public CardsView(){
            File dir = new File(directory);
            cards = new ArrayList<>(Arrays.asList(dir.listFiles()));
            cardSize = FRAME_SIZE / 6;
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
//        myPanel.repaint();
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
                    inputString = scan.nextLine();
                    inputOpenCardKey = inputString.charAt(0);
                    Character tmp = inputOpenCardKey;
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
                    Character tmp = inputCatchTotemKey;
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
    public GraphicsView(){
        FRAME_SIZE = (Toolkit.getDefaultToolkit().getScreenSize().getHeight() > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) ?
        (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() : (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
        cardsView = new CardsView();
        myGame = new Game(startView(), cardsView.getCardsNumbers());

        add(myPanel = new MyPanel());
        pack();
        addKeyListener(new MyKeyListener());
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}