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

    public ArrayList<CardView> cardsView;
    private class TotemView{
        Game.Totem totem;
        int totemRad;
        int xCoord;
        int yCoord;
        public TotemView (Game.Totem totem1){
            totem = totem1;
        }
        public void clearD(Graphics g){
            g.clearRect(xCoord-CardView.cardSize/2-panel_size/10, yCoord-CardView.cardSize/2-panel_size/20,
                    (int)(CardView.cardSize*2.5), CardView.cardSize+panel_size/10);

        }
        public void drawTotem(Graphics g){
            clearD(g);
            String cardsCount = String.valueOf(totem.getCardsCount());
            g.drawChars(cardsCount.toCharArray(), 0, cardsCount.length(), xCoord-CardView.cardSize/2+panel_size/60, yCoord+CardView.cardSize/2+panel_size/30);
            if (totem.getCardsCount()!=0){
                g.drawRect(xCoord-CardView.cardSize/2, yCoord-CardView.cardSize/2, CardView.cardSize, CardView.cardSize);
            }
            g.drawOval(xCoord-totemRad, yCoord-totemRad,2*totemRad,2*totemRad);

        }
        public boolean isIn(Point p){
            return (p.distance(xCoord, yCoord)<totemRad);
        }
    }
    TotemView totemV;
    public PlayerView initPlayerView(char newOpenCardKey, char newCatchTotemKey, String name, double a){
        return new PlayerView(newOpenCardKey, newCatchTotemKey, name, a);
    }
    ArrayList <PlayerView> playersView;

    private void reSize(int haracteristicScale){
        panel_size = haracteristicScale;
//        setPreferredSize(new Dimension(panel_size, panel_size));
        CardView.cardSize = panel_size / 10;
        PlayerView.setScale(haracteristicScale);
        totemV.totemRad = (int)(panel_size/50.5);
        totemV.xCoord = totemV.yCoord = (int)(panel_size/2.2);
        for (PlayerView player : playersView){
            player.xCoordinate = (int)((panel_size/3.5) * Math.sin(player.angle*Math.PI/180) + panel_size / 2.2);
            player.yCoordinate = (int)((panel_size/3.5) * Math.cos(player.angle*Math.PI/180) + panel_size / 2.5);

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
        reSize(Math.min(g.getClipBounds().height, g.getClipBounds().width));
        totemV.drawTotem(g);
        for (PlayerView player : playersView){
            player.drawPlayer(g, this);
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
            g.clearRect(20, panel_size-70, panel_size-30, 75);
        }
        g.clearRect(0,0,panel_size, 20);
        String whoPlayedMes = "It's "+playersView.get(myGame.getPlayerWhoWillGo()).playerViewName+"'s turn!";
        g.drawChars(whoPlayedMes.toCharArray(), 0, whoPlayedMes.length(), 20,20);
    }
    public MyMouseListener initMyMouseListener(){
        return new MyMouseListener();
    }
    public MyKeyListener initMyKeyListener(){
        return new MyKeyListener();
    }
    public void initTotemV(){
        totemV = new TotemView(myGame.totem);
        totemV.totem = myGame.totem;
    }


    public void initiation(Game game, ArrayList<Character> ktKeys, ArrayList<Character> ocKeys, ArrayList<Double> angle){
        myGame = game;
        playersView = new ArrayList<>(ktKeys.size());
        cardsView = new ArrayList<>();
        for (int i=0; i<400; i++){//Как сделать нормально?
            cardsView.add(null);
        }
        for (File cardI : CardView.cardsFiles){
            Pattern numberPattern = Pattern.compile("[0-9]+");
            Matcher numberMatcher = numberPattern.matcher(cardI.getName());
            numberMatcher.find();
            int num = Integer.parseInt(numberMatcher.group());
            CardView cardView = new CardView();
            for (Card card : myGame.allCards){
                if (card.getCardNumber() == num){
                    cardView.card = card;
                    cardView.id = num;
                    cardView.cardImage = Toolkit.getDefaultToolkit().getImage(cardI.toString());
                    cardView.fileCard = cardI;
                    break;
                }
            }
            cardsView.set(num, cardView);
        }
        for (int i=0; i<ktKeys.size(); i++){
            playersView.add(new PlayerView(ocKeys.get(i), ktKeys.get(i), myGame.getPlayer(i), angle.get(i)));
        }
        totemV = new TotemView(game.totem);
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
                if (totemV.isIn(p)){
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
                    repaint();
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
                        playersView.get(whoPlayed).topCardView = cardsView.get(playersView.get(whoPlayed).playerInfo.getTopOpenedCard().getCardNumber());

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
                        for (PlayerView player : playersView){
                            player.topCardView = cardsView.get(player.playerInfo.getTopOpenedCard().getCardNumber());
                        }
                        MyPanel.this.repaint();
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
                    for (PlayerView player : playersView){
                        player.topCardView = cardsView.get(player.playerInfo.getTopOpenedCard().getCardNumber());
                    }
                    allOpenFlag = false;
                }

            }
            MyPanel.this.repaint();
        }
    }

}
