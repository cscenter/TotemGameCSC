import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPanel extends JPanel {
    Game myGame;
    public int panel_size;
    public char lastPressedKey;
    public boolean allOpenFlag;
    public boolean catchTotemModeFlag;
    public boolean multyDuelFlag;
    public int whoPlayed;

    public class CardsView {
        public int cardSize;
        private ArrayList <File> cards;
        ArrayList<Image> image;

        public CardsView(){
            File dir = new File(GraphicsView.DIRECTORY);
            cards = new ArrayList<>(Arrays.asList(dir.listFiles()));
            cardSize = panel_size / 6;
            image = new ArrayList<>(168);
            for (int i=0; i<400; i++){
                image.add(null);
            }
            for (File cardI : cards){
                Pattern numberPattern = Pattern.compile("[0-9]+");
                Matcher numberMatcher = numberPattern.matcher(cardI.getName());
                numberMatcher.find();
                int num = Integer.parseInt(numberMatcher.group());
                Image im = Toolkit.getDefaultToolkit().getImage(cardI.toString());
                image.set(num, im);
            }
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
    public CardsView cardsView;
    public void initCardsView(){
        cardsView = new CardsView();
    }
    private class TotemView{
        int totemRad;
        int xCoord;
        int yCoord;
        public void clearD(Graphics g){
            g.clearRect(xCoord-cardsView.cardSize/2-panel_size/10, yCoord-cardsView.cardSize/2-panel_size/20,
                    (int)(cardsView.cardSize*2.5), cardsView.cardSize+panel_size/10);

        }
        public void drawTotem(Graphics g){
            clearD(g);
            String cardsCount = String.valueOf(myGame.getCardsUnderTotemCount());
            g.drawChars(cardsCount.toCharArray(), 0, cardsCount.length(), xCoord-cardsView.cardSize/2+panel_size/60, yCoord+cardsView.cardSize/2+panel_size/30);
            if (myGame.getCardsUnderTotemCount()!=0){
                g.drawRect(xCoord-cardsView.cardSize/2, yCoord-cardsView.cardSize/2, cardsView.cardSize, cardsView.cardSize);
            }
            g.drawOval(xCoord-totemRad, yCoord-totemRad,2*totemRad,2*totemRad);

        }
    }
    TotemView totemV;
    public PlayerView initPlayerView(char newOpenCardKey, char newCatchTotemKey, String name, double a){
        return new PlayerView(newOpenCardKey, newCatchTotemKey, name, a);
    }
    public class PlayerView{
        private Player playerInfo;
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
            xСoordinate = (int)((panel_size/3.5) * Math.sin(angle*Math.PI/180) + panel_size / 2.2);
            yСoordinate = (int)((panel_size/3.5) * Math.cos(angle*Math.PI/180) + panel_size / 2.5);
        }
        public void connectWithInfo(Player player){
            playerInfo = player;
        }
        public boolean isIn(Point p){
            if ((p.getX()<xСoordinate+cardsView.cardSize)&&(p.getX()>xСoordinate-cardsView.cardSize)){
                if ((p.getY()<yСoordinate+cardsView.cardSize+40)&&(p.getY()>yСoordinate-40)){
                    return true;
                }
            }
            return false;
        }
        public void clear(Graphics g){
            g.clearRect(xСoordinate-(int)(cardsView.cardSize*2.1), yСoordinate, (int)(3.1*cardsView.cardSize), (int)(cardsView.cardSize*2.5));
        }
        public void drawPlayer(Graphics g){
            clear(g);
            g.drawChars(playerViewName.toCharArray(), 0, playerViewName.length(), xСoordinate - cardsView.cardSize / 3, yСoordinate - panel_size / 30);
            String openCardsNumber = String.valueOf(playerInfo.getOpenCardsCount());
            g.drawChars(openCardsNumber.toCharArray(), 0, openCardsNumber.length(),xСoordinate+panel_size/60, yСoordinate+cardsView.cardSize+panel_size/30);

            if (playerInfo.getOpenCardsCount()!=0){
                Image image = cardsView.image.get(playerInfo.getTopOpenedCard().getCardNumber());
                g.drawImage(image, xСoordinate, yСoordinate, cardsView.cardSize, cardsView.cardSize, MyPanel.this);
            }
            String closeCardsNumber = String.valueOf(playerInfo.getCloseCardsCount());
            g.drawChars(closeCardsNumber.toCharArray(), 0, closeCardsNumber.length(),  xСoordinate-cardsView.cardSize+panel_size/60, yСoordinate+cardsView.cardSize+panel_size/30);

            if (playerInfo.getCloseCardsCount()!=0){
                g.fillRect(xСoordinate - (int)(1.1 * cardsView.cardSize), yСoordinate, cardsView.cardSize, cardsView.cardSize);

            }

        }
    }
    ArrayList <PlayerView> playersView;

    private void reSize(int haracteristicScale){
        panel_size = haracteristicScale;
//        setPreferredSize(new Dimension(panel_size, panel_size));
        cardsView.cardSize = panel_size / 10;
        totemV.totemRad = (int)(panel_size/50.5);
        totemV.xCoord = totemV.yCoord = (int)(panel_size/2.2);
        for (PlayerView player : playersView){
            player.xСoordinate = (int)((panel_size/3.5) * Math.sin(player.angle*Math.PI/180) + panel_size / 2.2);
            player.yСoordinate = (int)((panel_size/3.5) * Math.cos(player.angle*Math.PI/180) + panel_size / 2.5);

        }

    }
    @Override
    protected void paintComponent(Graphics g) {
        //get clip bounds
        //отделить панель и все остальные
        //sublime
        //String.valueOf
        //выделить переменные отдльно (name=player.getName()) etc.
        //пихнуть создание Image в CardView
        //class for TotemView
        //убрать с консоли всё
        //Поля в сеттеры-геттеры
        //result через s пишется
        reSize(g.getClipBounds().height);
        totemV.drawTotem(g);
        for (PlayerView player : playersView){
            player.drawPlayer(g);
        }
        if (catchTotemModeFlag){
            g.drawChars(("Do you want to use effect of won duel or of Arrows-In card? Clicked on Totem/player").toCharArray(), 0,
                    ("Do you want to use effect of won duel or of Arrows-In card? Clicked on Totem/player").length(), 30, panel_size-50);
        }else{
            g.clearRect(20, panel_size-70, panel_size-30, 70);
        }
        if (multyDuelFlag){
            g.drawChars("You one duel, here is several loosers. clicked to one of them".toCharArray(), 0, "You one duel, here is several loosers. clicked to one of them".length(),
                    30, panel_size-50);
        }else{
            g.clearRect(20, panel_size-70, panel_size-30, 70);
        }
    }
    public MyMouseListener initMyMouseListener(){
        return new MyMouseListener();
    }
    public MyKeyListener initMyKeyListener(){
        return new MyKeyListener();
    }
    public void initTotemV(){
        totemV = new TotemView();
    }
    public class MyMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (catchTotemModeFlag){
                Point p = e.getLocationOnScreen();
                ArrayList<Integer> possibleLosers = myGame.checkDuelWithPlayer(myGame.getPlayer(whoPlayed));
                for (int i=0; i<playersView.size();i++){
                    if (playersView.get(i).isIn(p)){
                        int looser = i;
                        myGame.afterDuelMakeMove(whoPlayed,looser);
                        catchTotemModeFlag = false;
                        break;
                    }
                }
                if (p.distance(panel_size/2.2,panel_size/2.2)<20){
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
    }
    public class MyKeyListener extends KeyAdapter {
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
                MyPanel.this.repaint();
            }else{

                if (k.getKeyCode()==KeyEvent.VK_ENTER){
                    myGame.openAllTopCards();
                    allOpenFlag = false;
                }

            }
            MyPanel.this.repaint();
        }
    }

}
