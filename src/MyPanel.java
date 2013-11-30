import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

public class MyPanel extends JPanel {
    private Game myGame;
    private int panel_size;
    private boolean allOpenFlag;
    private boolean catchTotemModeFlag;
    private boolean multyDuelFlag;
    private int whoPlayed;

    private ArrayList<CardView> cardsView;
    private Graphics g;
    public class TotemView{
        private Game.Totem totem;
        private int totemRad;
        private int xCoord;
        private int yCoord;
        public TotemView (Game.Totem totem1){
            totem = totem1;
        }
        public void clearD(Graphics g){
            g.clearRect(xCoord-CardView.getCardSize()/2-panel_size/10, yCoord-CardView.getCardSize()/2-panel_size/20,
                    (int)(CardView.getCardSize()*2.5), CardView.getCardSize()+panel_size/10);

        }
        public void drawTotem(Graphics g, int type) throws IOException{
            clearD(g);
            
            String cardsCount = String.valueOf(totem.getCardsCount());
          //  g.drawChars(cardsCount.toCharArray(), 0, cardsCount.length(), xCoord-CardView.getCardSize()/2+panel_size/60, yCoord+CardView.getCardSize()/2+panel_size/30);
            if (totem.getCardsCount()!=0){
                g.fill3DRect(10, 10, 10, 10, allOpenFlag);
              if (type == 0)  g.drawImage(ImageIO.read(new File("data/totem.jpg")), xCoord - totemRad*4, yCoord - totemRad*2, null);
              if (type == 1)  g.drawImage(ImageIO.read(new File("data/totemW.jpg")), xCoord - totemRad*4, yCoord - totemRad*2, null);
              if (type == 2)  g.drawImage(ImageIO.read(new File("data/totemR.jpg")), xCoord - totemRad*4, yCoord - totemRad*2, null);

              /* String s = null;
                s += xCoord - totemRad*4;
                s += ' ';
                s += yCoord - totemRad*2;
                  
                g.drawString(s, xCoord - totemRad*4,yCoord - totemRad*2 );*/
               // printf("coord is %d #d\n", xCoord - totemRad*4, yCoord - totemRad*2);
         //g.drawRect(xCoord-CardView.getCardSize()/2, yCoord-CardView.getCardSize()/2, CardView.getCardSize(), CardView.getCardSize());
            }
            else
            // g.drawOval(xCoord-totemRad, yCoord-totemRad,2*totemRad,2*totemRad);
             //g.drawImage(ImageIO.read(new File("table.jpg")), 50, 50, null);

             //g.drawImage(ImageIO.read(new File("table.jpg")), xCoord - totemRad*14, yCoord - totemRad*10, null);
            g.drawImage(ImageIO.read(new File("data/totem.jpg")), xCoord - totemRad*4, yCoord - totemRad*2, null);
           // g.drawOval(xCoord-totemRad, yCoord-totemRad,2*totemRad,2*totemRad);
           // g.drawOval();

        }
        public boolean isIn(Point p){
            return (p.distance(xCoord, yCoord)<totemRad);
        }

        public void resize(int haracteristicScale) {
            totemV.totemRad = (int)(haracteristicScale/50.5);
            totemV.xCoord = totemV.yCoord = (int)(haracteristicScale/2.2);
        }
    }
    private TotemView totemV;
    private ArrayList <PlayerView> playersView;

    private void reSize(int haracteristicScale){
        panel_size = haracteristicScale;
        PlayerView.setScale(haracteristicScale);
        CardView.resize(haracteristicScale);
        totemV.resize(haracteristicScale);
        for (PlayerView player : playersView){
            player.resize(haracteristicScale);
        }

    }
    @Override
    protected void paintComponent(Graphics g) {
        this.g = g;
        //убрать с консоли всё
        reSize(Math.min(g.getClipBounds().height, g.getClipBounds().width));
      try {
            totemV.drawTotem(g, 0);
        } catch (IOException ex) {
            Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (PlayerView player : playersView){
            try {
                player.drawPlayer(g, this);
            } catch (IOException ex) {
                Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        String whoPlayedMes = "It's "+playersView.get(myGame.getPlayerWhoWillGo()).getPlayerName()+"'s turn!";
        g.drawChars(whoPlayedMes.toCharArray(), 0, whoPlayedMes.length(), 20,20);
        try {
            g.drawImage(ImageIO.read(new File("data/tboy_go1.jpg")),    myGame.getPlayer(myGame.getPlayerWhoWillGo()).getXCoordinate() -110, myGame.getPlayer(myGame.getPlayerWhoWillGo()).getYCoordinate(), null);
            //    myGame.getPlayer(myGame.getPlayerWhoWillGo()).getXCoordinate()
        } catch (IOException ex) {
            Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
       
    public MyMouseListener initMyMouseListener(){
        return new MyMouseListener();
    }
    public MyKeyListener initMyKeyListener(){
        return new MyKeyListener();
    }

    public void initiation(Game game, ArrayList<Character> ktKeys, ArrayList<Character> ocKeys, ArrayList<Double> angle){
        myGame = game;
        playersView = new ArrayList<>(ktKeys.size());
        cardsView = new ArrayList<>();
        for (int i=0; i<400; i++){//Как сделать нормально?
            cardsView.add(null);
        }
        for (File cardI : CardView.getCardsFiles()){
            Pattern numberPattern = Pattern.compile("[0-9]+");
            Matcher numberMatcher = numberPattern.matcher(cardI.getName());
            numberMatcher.find();
            int num  =0;
            try{
            if ((cardI.getName().startsWith("t")) != true ) 
            {
                
                num = Integer.parseInt(numberMatcher.group());
            }}
           catch(Exception e){
               System.out.print(cardI.getName());
               
           }
        
            
            for (Card card : myGame.getAllCards()){
                if (card.getCardNumber() == num){
                    CardView cardView = new CardView(card, cardI, Toolkit.getDefaultToolkit().getImage(cardI.toString()), num);
                    cardsView.set(num, cardView);
                    break;
                }
            }
        }
        for (int i=0; i<ktKeys.size(); i++){
            playersView.add(new PlayerView(ocKeys.get(i), ktKeys.get(i), myGame.getPlayer(i), angle.get(i)));
        }
        totemV = new TotemView(game.getTotem());
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
                Character inputChar = k.getKeyChar();
                inputChar = (new Character(inputChar)).toString().toLowerCase().charAt(0);
                boolean suchKeyHere = false;
                Game.ResultOfMakeMove resultOfMakeMove = Game.ResultOfMakeMove.INCORRECT;
                whoPlayed = 0;
                for (int i = 0; i < myGame.getPlayersCount(); i++){
                    if (playersView.get(i).getOpenCardKey() == inputChar){
                        resultOfMakeMove = myGame.makeMove(i, Game.WhatPlayerDid.OPEN_NEW_CARD);
                        suchKeyHere = true;
                        whoPlayed = i;
                        break;
                    }
                    if (playersView.get(i).getCatchTotemKey() == inputChar){
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
                        try {
                            totemV.drawTotem(g, 1);
                          } catch (IOException ex) {
                             Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                     //  g.drawImage(ImageIO.read(new File("totem.jpg")), 275, 303, null);
                        break;
                    case CARD_OPENED:
                        System.out.printf("%s open next card\n",
                                myGame.getPlayer(whoPlayed).getName());
                        playersView.get(whoPlayed).setTopCardView(cardsView);

                        break;
                    case NOT_DEFINED_CATCH:
//                        ArrayList <Integer> possibleLosers = myGame.checkDuelWithPlayer(myGame.getPlayer(whoPlayed));
                        if (myGame.getGameMode() == Game.GameMode.CATCH_TOTEM_MODE){
                            catchTotemModeFlag = true;
                        }else{
                            multyDuelFlag = true;
                        }
                        break;
                    case ALL_CARDS_OPENED:
                        allOpenFlag = true;
                        for (PlayerView player : playersView){
                            player.setTopCardView(cardsView);
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
                        player.setTopCardView(cardsView);
                    }
                    allOpenFlag = false;
                }

            }
            MyPanel.this.repaint();
        }
    }

}
