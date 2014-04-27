package view;

import controller.TotemClient;
import model.Card;
import model.Game;
import utils.Configuration;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPanel extends JPanel {
    private TotemClient client;
    private int panel_size;
    private boolean allOpenFlag;
    private boolean catchTotemModeFlag;
    private boolean multyDuelFlag;
    private int whoPlayed;
    private String message;
    private int mesOk;
    private int typeTotem;
    static double sizesDiv = 4.0 / 3;
    private int xMes;
    int basicFontSize = 15;
    private TotemView totemV;
    private ArrayList<PlayerView> playersView;
    private int pos;
    private Timer timer;

    private ArrayList<CardView> cardsView;
    private WhatRepaint whatRepaint;
    private enum WhatRepaint {
        NEW_CARD,
        NOT_DEF;
        public int lastPlayerWhoAct;
    }
    /**
     * класс, отвечающий за отрисовку тотема
     */
    public class TotemView {
        private model.Game.Totem totem;
        private int totemHeight;
        private int totemWeight;
        private int xCoord;
        private int yCoord;
        /**
         * Привязка тотема-модели
         * @param totem1
         */
        public TotemView(model.Game.Totem totem1) {
            totem = totem1;
        }

        public void drawTotem(Graphics g) {
/*            if (mesOk == 1) {
                Font font = new Font("Arial", Font.BOLD, 15);
                Font oldFont = g.getFont();
                g.setColor(Color.red);
                g.setFont(font);
                message += xMes;
                g.drawChars(message.toCharArray(), 0, message.length(), 20 + xMes, panel_size / 30 + 15);
                mesOk = 0;
                g.setFont(oldFont);
                g.setColor(Color.BLACK);
            } else {
                //  g.clearRect(0, panel_size/30 , 800, 30);
                message = "";
            }
            String cardsCount = String.valueOf(totem.getCardsCount());
            // g.drawChars(cardsCount.toCharArray(), 0, cardsCount.length(), xCoord-graphics.CardView.getCardSize()/2+panel_size/60, yCoord+graphics.CardView.getCardSize()/2+panel_size/30);

      //      g.fill3DRect(10, 10, 10, 10, allOpenFlag);
  */
            String cardsCount = String.valueOf(totem.getCardsCount());
            if (typeTotem == 0) {
                Image img = Configuration.getGallery().getImage("data/totem.png");
                g.drawImage(img, xCoord - totemWeight /2, yCoord - totemHeight / 2, totemWeight, totemHeight, MyPanel.this);
//                g.drawImage(img, xCoord - (int)(totemRad * 4.5), yCoord - totemRad * 2, null);
            }

            if (typeTotem == 1) {
                Image img = Configuration.getGallery().getImage("data/totemW.png");
                g.drawImage(img, xCoord - totemWeight /2, yCoord - totemHeight / 2, totemWeight, totemHeight, MyPanel.this);
                typeTotem = 0;
            }
            if (typeTotem == 2) {
                Image img = Configuration.getGallery().getImage("data/totemR.png");
                g.drawImage(img, xCoord - totemWeight /2, yCoord - totemHeight / 2, totemWeight, totemHeight, MyPanel.this);
                typeTotem = 0;
            }

            Font font = new Font("Tahoma", Font.BOLD, basicFontSize);
            g.setColor(Color.cyan);
            g.setFont(font);
            g.drawString(cardsCount, xCoord - (int)(basicFontSize / 2.5), yCoord + totemHeight / 2);
//            g.drawChars(cardsCount.toCharArray(), 0, cardsCount.length(), xCoord - CardView.getCardSize() / 2 + panel_size / 60 + 10, yCoord + CardView.getCardSize() / 2 + panel_size / 30 + 40);


        }

        public boolean isIn(Point p) {
            return ((Math.abs(p.x - xCoord) < totemWeight / 2) && (Math.abs(p.y - yCoord) < totemHeight / 2 ));
        }

        public void resize(int haracteristicScale) {
            totemV.totemHeight = (int) (haracteristicScale / 7);
            totemV.totemWeight = (int) (haracteristicScale / 10);
            totemV.yCoord = (int) (haracteristicScale / 2);
            totemV.xCoord = (int) (totemV.yCoord * sizesDiv + haracteristicScale / 30);
        }
    }

    private void reSize(int haracteristicScale) {
        basicFontSize = haracteristicScale / 35;
        panel_size = haracteristicScale;
        CardView.resize(haracteristicScale);
        PlayerView.setScale(haracteristicScale);
        totemV.resize(haracteristicScale);
        for (PlayerView player : playersView) {
            player.resize(haracteristicScale);
        }

    }

    /**
     * функция для отладки. Рисует, где находится заданная точка
     * @param p точка, которую хотим показать
     */
    void drawTarget (Graphics g, Point p) {
        int r = 10;
        Color c = g.getColor();
        g.setColor(Color.red);
        int delta = (int) (r * Math.sqrt(2) / 2);
        g.drawLine(p.x - delta, p.y - delta, p.x + delta, p.y + delta);
        g.drawLine(p.x + delta, p.y - delta, p.x - delta, p.y + delta);
        g.drawRect(p.x - delta, p.y - delta,
                2 * delta, 2 * delta);
        g.setColor(c);
    }
    @Override
    protected void paintComponent(Graphics g) {
        //убрать с консоли всё
        switch (whatRepaint){
            case NOT_DEF:
//                super.repaint();
                reSize(Math.min(g.getClipBounds().height, g.getClipBounds().width));
                repaintAll(g);
/*                Image img = Configuration.getGallery().getImage("data/b1.png");
                g.drawImage(img, 0, 0, panel_size + 25, panel_size, null);//MyPanel.HEIGHT, MyPanel.WIDTH, null );
                // g.draw

                totemV.drawTotem(g);
                for (PlayerView player : playersView) {
                    try {
                        player.drawPlayer(g, this);
                    } catch (Exception ex) {
                        Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (catchTotemModeFlag) {
                    g.drawChars(("Do you want to use effect of won duel or of Arrows-In card? Clicked on Totem/player").toCharArray(), 0,
                            ("Do you want to use effect of won duel or of Arrows-In card? Clicked on Totem/player").length(), 30, panel_size - 50);
                } else {
                    g.clearRect(20, panel_size - 70, panel_size - 30, 70);
                }
                if (multyDuelFlag) {
                    g.drawChars("You one duel, here is several loosers. clicked to one of them".toCharArray(), 0, "You one duel, here is several loosers. clicked to one of them".length(),
                            30, panel_size - 50);
                } else {
                    g.clearRect(20, panel_size - 70, panel_size - 30, 75);
                }
                g.clearRect(0, 0, panel_size, 20);
                String whoPlayedMes = "It's " + playersView.get(client.getPlayerWhoWillGo()).getPlayerName() + "'s turn!";
                g.drawChars(whoPlayedMes.toCharArray(), 0, whoPlayedMes.length(), 20, 20);
                int all = client.getPlayersCount();
                int x1 = playersView.get((all - 1 + client.getPlayerWhoWillGo()) % all).getXCoordinate();
                int y1 = playersView.get((all - 1 + client.getPlayerWhoWillGo()) % all).getYCoordinate();
                int x2 = playersView.get(client.getPlayerWhoWillGo()).getXCoordinate();
                int y2 = playersView.get(client.getPlayerWhoWillGo()).getYCoordinate();
                float t = (float) 2. / 5;
                float x, y;
                while (t < (float) 3. / 5) {
                    x = t * x1 + (1 - t) * x2;
                    y = t * y1 + (1 - t) * y2;
                    g.setColor(Color.red);
                    t = t + (float) (1. / 20);
                }
                int plWhoGo = 0;
                for (int i = 0; i < client.getPlayersCount(); i++) {
                    if (client.getPlayer(i).isGO())
                        plWhoGo = i;
                    client.getPlayer(i).setGo(false);

                }
                client.getPlayer(client.getPlayerWhoWillGo()).setGo(true);
                try {
                    Image imgU = Configuration.getGallery().getImage("data/arrow_u.png");
                    Image imgD = Configuration.getGallery().getImage("data/arrow_d.png");

                    int playerX = playersView.get(client.getPlayerWhoWillGo()).getXCoordinate(); // client.getPlayer(client.getPlayerWhoWillGo()).getXCoordinate();
                    int playerY = playersView.get(client.getPlayerWhoWillGo()).getYCoordinate(); // client.getPlayer(client.getPlayerWhoWillGo()).getYCoordinate();

                    if (plWhoGo % 4 == 1) {
                        g.drawImage(imgU, playerX - 60, playerY + 150, 100, 250, null);
                    }
                    if (plWhoGo % 4 == 2) {
                        g.drawImage(imgU, playerX + 150, playerY, 100, 250, null);
                    }
                    if (plWhoGo % 4 == 3) {
                        g.drawImage(imgD, playerX - 60, playerY - 250, 100, 250, null);
                    }
                    if (plWhoGo % 4 == 0) {
                        g.drawImage(imgD, playerX - 300, playerY - 120, 100, 250, null);
                    }

                    g.drawImage(Configuration.getGallery().getImage("data/tboy-go1.png"), playerX - 110, playerY, null);
                } catch (Exception ex) {
                    Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
                }     */
                break;
            case NEW_CARD:
                int who = whatRepaint.lastPlayerWhoAct;
                PlayerView currPlayer = playersView.get(who);
                Point p = currPlayer.getOpenCardCenter();
                int sizeX1 = CardView.getCardSize();
                int sizeY1 = CardView.getCardSize();
                int sizeX2 = CardView.getCardSize() * 2;
                int sizeY2 = CardView.getCardSize() * 2;
                Image cardI = currPlayer.getTopCardViewImage();
                makeImageBiggerAnimation(g, 10, cardI, p, sizeX1, sizeY1, sizeX2, sizeY2);
        }
    }

    private void repaintAll(Graphics g) {
        Image img = Configuration.getGallery().getImage("data/b1.png");
        g.drawImage(img, 0, 0, (int) (panel_size * sizesDiv), panel_size,  null);
        totemV.drawTotem(g);
        for (PlayerView player : playersView) {
            player.drawPlayer(g, this, basicFontSize);
        }
    }
    public void repaintModel(Queue<Integer> whoDid, Queue<Game.WhatPlayerDid> whatDid,
                            boolean isSmbOpen,boolean isSmbdCatch) {
        Game.ResultOfMakeMove resultOfMakeMove;
        whoPlayed = 0;
        if (isSmbdCatch){
            whoPlayed = whoDid.remove();
            resultOfMakeMove = client.makeMove(whoPlayed, whatDid.remove());
            switch (resultOfMakeMove) {
                case TOTEM_WAS_CATCH_CORRECT:
                    mesOk = 1;
                    message = "You won duel, " + client.getPlayer(whoPlayed).getName() + " All your open cards and all cards under totem go to your opponent";
                    typeTotem = 2;
//                    MyPanel.this.repaint();
                    System.out.printf("You won duel, %s! All your open cards and all cards under totem go to your opponent\n",
                            client.getPlayer(whoPlayed).getName());
                    break;
                case TOTEM_WAS_CATCH_INCORRECT:
                    mesOk = 1;
                    message = "You mustn't take totem, " + client.getPlayer(whoPlayed).getName() + " So you took all open cards!";
                    typeTotem = 1;
  //                  MyPanel.this.repaint();
                    System.out.printf("You mustn't take totem, %s! So you took all open cards!\n",
                            client.getPlayer(whoPlayed).getName());
                    break;
                case NOT_DEFINED_CATCH:
                    if (client.getGameMode() == Game.GameMode.CATCH_TOTEM_MODE) {
                        catchTotemModeFlag = true;
                    } else {
                        multyDuelFlag = true;
                    }
                    break;
                default:
                    break;
            }

            for (int i = 0; i < client.getPlayersCount(); i++) {
                if (client.getPlayer(i).getCardsCount() == 0) {
                    mesOk = 1;
                    message = "Player %s won! It's very good :)\n" + client.getPlayer(i).getName();
    //                MyPanel.this.repaint();
                    System.out.printf("Player %s won! It's very good :)\n", client.getPlayer(i).getName());
                }
            }
        }

        if (isSmbOpen){
            while (whoDid.size() > 1){
                whatDid.remove();
                whoDid.remove();
            }
            whoPlayed = whoDid.remove();
            resultOfMakeMove = client.makeMove(whoPlayed, whatDid.remove());

            switch (resultOfMakeMove) {
                case INCORRECT:
                    xMes = 0;
                    System.out.printf("It's not your turn, %s. Don't hurry!\n",
                            client.getPlayer(whoPlayed).getName());
                    break;
                case CARD_OPENED:
                    System.out.printf("%s open next card\n",
                            client.getPlayer(whoPlayed).getName());
                    playersView.get(whoPlayed).setTopCardView(cardsView);
                    whatRepaint = WhatRepaint.NEW_CARD;
                    whatRepaint.lastPlayerWhoAct = whoPlayed;
                    timeToOpen = 0;
                    AnimListenerCardOpen animListener = new AnimListenerCardOpen();
                    timer = new Timer(100, animListener);
                    timer.start();
                    break;
                case NOT_DEFINED_CATCH:
                    if (client.getGameMode() == Game.GameMode.CATCH_TOTEM_MODE) {
                        catchTotemModeFlag = true;
                    } else {
                        multyDuelFlag = true;
                    }
                    break;
                case ALL_CARDS_OPENED:
                    allOpenFlag = true;
                    for (PlayerView player : playersView) {
                        player.setTopCardView(cardsView);
                    }

                    mesOk = 1;
                    message = "All players will open top cards. To do this, press Enter";
    //                MyPanel.this.repaint();

                    System.out.println("All players will open top cards. To do this, press Enter");
                    break;
                default:
                    break;
            }
        }

        if (isSmbdCatch || isSmbOpen) {
            repaint();
        }
    }

    private void makeImageBiggerAnimation(Graphics g, int steps, Image image, Point center,
                                          int sizeXBegin, int sizeYBegin, int sizeXEnd, int sizeYEnd) {
        if (timeToOpen >= steps) {
            timer.stop();
            timeToOpen = 0;
            whatRepaint = WhatRepaint.NOT_DEF;
            repaintAll(g);
        }
        int currXSize = (sizeXEnd - sizeXBegin) * timeToOpen / steps + sizeXBegin;
        int currYSize = (sizeYEnd - sizeYBegin) * timeToOpen / steps + sizeYBegin;
        g.drawImage(image, center.x - currXSize / 2, center.y - currYSize / 2,
                currXSize, currYSize, this);
    }
    private int timeToOpen = 0;
    class AnimListenerCardOpen implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            repaint();
            timeToOpen++;
        }
    }
    public MyMouseListener initMyMouseListener() {
        return new MyMouseListener();
    }

    public MyKeyListener initMyKeyListener() {
        return new MyKeyListener();
    }

    //вместо конструктора
    public void initiation(TotemClient tClient, ArrayList<Character> ktKeys, ArrayList<Character> ocKeys, ArrayList<Double> angle) {
        client = tClient;
        sizesDiv = 4.0/3;
        whatRepaint = WhatRepaint.NOT_DEF;
        whatRepaint.lastPlayerWhoAct = 0;
        playersView = new ArrayList<>(ktKeys.size());
        cardsView = new ArrayList<>();
        for (int i = 0; i < 400; i++) {//Как сделать нормально?
            cardsView.add(null);
        }
        for (String cardI : Configuration.getGallery().getCardsNames()) {
            Pattern numberPattern = Pattern.compile("[0-9]+");
            Matcher numberMatcher = numberPattern.matcher(cardI);
            numberMatcher.find();
            int num = 0;
            num = Integer.parseInt(numberMatcher.group());


            for (Card card : client.getAllCards()) {
                if (card.getCardNumber() == num) {
                    ClassLoader cl = MyPanel.class.getClassLoader();

//                    Image image = (new ImageIcon(cl.getResource(cardI))).getImage();
                    CardView cardView = new CardView(card, Configuration.getGallery().getImage(cardI), num);
                    cardsView.set(num, cardView);
                    break;
                }
            }
        }
      /* хотим, чтобы клиент, за которого отвечает этот вид был внизу.
      было (360.0 * i / Configuration.getNumberOfPlayers());
      хотим (360.0 * (i - №клиента)  / Configuration.getNumberOfPlayers());
       значит надо вычесть - 360.0 * client.getWhatPlayer() / Configuration.getNumberOfPlayers();
        */
        for (int i = 0; i < ktKeys.size(); i++) {
            double currentAngle = angle.get(i);
            currentAngle = currentAngle - 360.0 * client.getWhatPlayer() / Configuration.getNumberOfPlayers();
            playersView.add(new PlayerView(ocKeys.get(i), ktKeys.get(i), client.getPlayer(i), currentAngle,
                    Configuration.getGallery().getImage("data/avatars/"+i+".png")));
        }
        totemV = new TotemView(client.getTotem());
    }


    public class MyMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (catchTotemModeFlag) {
                Point p = e.getLocationOnScreen();
                ArrayList<Integer> possibleLosers = client.checkDuelWithPlayer(client.getPlayer(whoPlayed));
                for (int i = 0; i < playersView.size(); i++) {
                    if (playersView.get(i).isIn(p)) {
                        int looser = i;
                        client.afterDuelMakeMove(whoPlayed, looser);
                        catchTotemModeFlag = false;
                        break;
                    }
                }
                if (totemV.isIn(p)) {
                    catchTotemModeFlag = false;
                    client.arrowsInMakeMove(whoPlayed);
                    catchTotemModeFlag = false;
                }
            }
            if (multyDuelFlag) {
                Point p = e.getLocationOnScreen();
                ArrayList<Integer> possibleLosers = client.checkDuelWithPlayer(client.getPlayer(whoPlayed));
                for (int i = 0; i < playersView.size(); i++) {
                    if (playersView.get(i).isIn(p)) {
                        int looser = i;
                        client.afterDuelMakeMove(whoPlayed, looser);
                        multyDuelFlag = false;
                        break;
                    }
                }
            }
        }
    }


    public class MyKeyListener extends KeyAdapter {
        // нажатие клавиши
        @Override
        public void keyPressed(KeyEvent k) {
            if (!allOpenFlag) {
                Character inputChar = k.getKeyChar();
                inputChar = (new Character(inputChar)).toString().toLowerCase().charAt(0);
                boolean suchKeyHere = false;
                for (int i = 0; i < client.getPlayersCount(); i++) {
                    if (playersView.get(i).getOpenCardKey() == inputChar) {
                        client.moveWithoutAnswer(i, Game.WhatPlayerDid.OPEN_NEW_CARD);
                        suchKeyHere = true;
                        break;
                    }
                    if (playersView.get(i).getCatchTotemKey() == inputChar) {
                        client.moveWithoutAnswer(i, Game.WhatPlayerDid.TOOK_TOTEM);
                        suchKeyHere = true;
                        break;
                    }
                }
                if (!(suchKeyHere)) {
                    mesOk = 1;
                    message = "Nobody have such key. Try again.";
                    System.out.println("Nobody have such key. Try again.\n");
                    MyPanel.this.repaint();
                }
            } else {
                if (k.getKeyCode() == KeyEvent.VK_ENTER) {
                    client.openAllTopCards();
                    for (PlayerView player : playersView) {
                        player.setTopCardView(cardsView);
                    }
                    allOpenFlag = false;
                }

            }

            MyPanel.this.repaint();

        }
    }

}
