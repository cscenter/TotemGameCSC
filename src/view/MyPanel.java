package view;

import controller.TotemClient;
import model.Card;
import model.Game;
import utils.Configuration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPanel extends JPanel {
    //    private model.Game client;
    private TotemClient client;
    private int panel_size;
    private boolean allOpenFlag;
    private boolean catchTotemModeFlag;
    private boolean multyDuelFlag;
    private int whoPlayed;
    private String message;
    private int mesOk;
    private int typeTotem;
    private int xMes;
    private TotemView totemV;
    private ArrayList<PlayerView> playersView;
    private int pos;
    private Timer timer;

    private ArrayList<CardView> cardsView;
    private Graphics g;

    public class TotemView {
        private model.Game.Totem totem;
        private int totemRad;
        private int xCoord;
        private int yCoord;

        public TotemView(model.Game.Totem totem1) {
            totem = totem1;
        }

        public void clearD(Graphics g) {
            g.clearRect(xCoord - CardView.getCardSize() / 2 - panel_size / 10 - 45, yCoord - CardView.getCardSize() / 2 - panel_size / 20,
                    (int) (CardView.getCardSize() * 2.5) + 45, CardView.getCardSize() + panel_size / 10 + 120);

        }

        public void drawTotem(Graphics g) throws IOException {
            //clearD(g);
            if (mesOk == 1) {
                //g.clearRect(0, panel_size/30 , 800, 30);
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

            g.fill3DRect(10, 10, 10, 10, allOpenFlag);

            if (typeTotem == 0) {
                Image img = Configuration.getGallery().getImage("data/totem.png");
                g.drawImage(img, xCoord - totemRad * 4 + 20, yCoord - totemRad * 2, null);
            }

            if (typeTotem == 1) {
                Image img = Configuration.getGallery().getImage("data/totemW.png");
                g.drawImage(img, xCoord - totemRad * 4 + 20, yCoord - totemRad * 2, null);
                typeTotem = 0;
            }
            if (typeTotem == 2) {
                Image img = Configuration.getGallery().getImage("data/totemR.png");
                g.drawImage(img, xCoord - totemRad * 4 + 20, yCoord - totemRad * 2, null);
                typeTotem = 0;
            }

            Font font = new Font("Tahoma", Font.BOLD, 15);
            g.setFont(font);
            g.drawChars(cardsCount.toCharArray(), 0, cardsCount.length(), xCoord - CardView.getCardSize() / 2 + panel_size / 60 + 10, yCoord + CardView.getCardSize() / 2 + panel_size / 30 + 40);


        }

        public boolean isIn(Point p) {
            return (p.distance(xCoord, yCoord) < totemRad);
        }

        public void resize(int haracteristicScale) {
            totemV.totemRad = (int) (haracteristicScale / 50.5);
            totemV.yCoord = (int) (haracteristicScale / 2.2);
            totemV.xCoord = totemV.yCoord + 50;
        }
    }

    private void reSize(int haracteristicScale) {
        panel_size = haracteristicScale;
        PlayerView.setScale(haracteristicScale);
        CardView.resize(haracteristicScale);
        totemV.resize(haracteristicScale);
        for (PlayerView player : playersView) {
            player.resize(haracteristicScale);
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        this.g = g;
        //убрать с консоли всё
        reSize(Math.min(g.getClipBounds().height, g.getClipBounds().width));

        Image img = Configuration.getGallery().getImage("data/b1.png");
        g.drawImage(img, 0, 0, panel_size + 25, panel_size, null);//MyPanel.HEIGHT, MyPanel.WIDTH, null );
        // g.draw

        try {
            totemV.drawTotem(g);
        } catch (IOException ex) {
            Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
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
/*        int x1 = client.getPlayer((all - 1 + client.getPlayerWhoWillGo()) % all).getXCoordinate(),
                y1 = client.getPlayer((all - 1 + client.getPlayerWhoWillGo()) % all).getYCoordinate(),
                x2 = client.getPlayer(client.getPlayerWhoWillGo()).getXCoordinate(),
                y2 = client.getPlayer(client.getPlayerWhoWillGo()).getYCoordinate();*/
        float t = (float) 2. / 5;
        float x, y;
        while (t < (float) 3. / 5) {
            x = t * x1 + (1 - t) * x2;
            y = t * y1 + (1 - t) * y2;
            g.setColor(Color.red);
            // g.fillRect((int)x, (int)y, 20, 20);
            /*try {
                //g.drawString(g.getClip()+"", 10, 30);
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
            }*/

            //MyPanel.this.repaint(100);
            //g.clearRect((int)x, (int)y, 20, 20);
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
            //    client.getPlayer(client.getPlayerWhoWillGo()).getXCoordinate()
        } catch (Exception ex) {
            Logger.getLogger(MyPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        // client.getPlayer(client.getPlayerWhoWillGo()).setGo(false);
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
        for (int i = 0; i < ktKeys.size(); i++) {
            playersView.add(new PlayerView(ocKeys.get(i), ktKeys.get(i), client.getPlayer(i), angle.get(i)));
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
                Game.ResultOfMakeMove resultOfMakeMove = Game.ResultOfMakeMove.CARD_OPENED;
                whoPlayed = 0;
                for (int i = 0; i < client.getPlayersCount(); i++) {
                    if (playersView.get(i).getOpenCardKey() == inputChar) {
//                        resultOfMakeMove = client.makeMove(i, Game.WhatPlayerDid.OPEN_NEW_CARD);
                        client.moveWithoutAnswer(i, Game.WhatPlayerDid.OPEN_NEW_CARD);
                        suchKeyHere = true;
                        whoPlayed = i;
                        break;
                    }
                    if (playersView.get(i).getCatchTotemKey() == inputChar) {
                        //                      resultOfMakeMove = client.makeMove(i, Game.WhatPlayerDid.TOOK_TOTEM);
                        client.moveWithoutAnswer(i, Game.WhatPlayerDid.TOOK_TOTEM);
                        suchKeyHere = true;
                        whoPlayed = i;
                        break;
                    }
                    repaint();
                }
                if (!(suchKeyHere)) {
                    mesOk = 1;
                    message = "Nobody have such key. Try again.";
                    System.out.println("Nobody have such key. Try again.\n");
                    MyPanel.this.repaint();
                }
                switch (resultOfMakeMove) {
                    case INCORRECT:
                        java.util.Timer timer = new java.util.Timer();
                        TimerTask task = new TimerTask() {

                            public void run() {
                                xMes += 10;
                                mesOk = 1;
                                message = "It's not your turn, " + client.getPlayer(whoPlayed).getName() + " Don't hurry! " + xMes + ' ';
                                MyPanel.this.repaint();
                            }
                        };
                        //while(xMes<100){
                        timer.schedule(task, 1000, 100);
                        if (xMes > 90) timer.cancel();
                        xMes = 0;
                        System.out.printf("It's not your turn, %s. Don't hurry!\n",
                                client.getPlayer(whoPlayed).getName());
                        break;
                    case TOTEM_WAS_CATCH_CORRECT:
                        mesOk = 1;
                        message = "You won duel, " + client.getPlayer(whoPlayed).getName() + " All your open cards and all cards under totem go to your opponent";
                        typeTotem = 2;
                        MyPanel.this.repaint();
                        System.out.printf("You won duel, %s! All your open cards and all cards under totem go to your opponent\n",
                                client.getPlayer(whoPlayed).getName());
                        break;
                    case TOTEM_WAS_CATCH_INCORRECT:
                        mesOk = 1;
                        message = "You mustn't take totem, " + client.getPlayer(whoPlayed).getName() + " So you took all open cards!";
                        typeTotem = 1;
                        MyPanel.this.repaint();
                        System.out.printf("You mustn't take totem, %s! So you took all open cards!\n",
                                client.getPlayer(whoPlayed).getName());
                        break;
                    case CARD_OPENED:
                        System.out.printf("%s open next card\n",
                                client.getPlayer(whoPlayed).getName());
                        try {
                            playersView.get(whoPlayed).setTopCardView(cardsView);
                        } catch (Exception e) {
                        }

                        break;
                    case NOT_DEFINED_CATCH:
//                        ArrayList <Integer> possibleLosers = client.checkDuelWithPlayer(client.getPlayer(whoPlayed));
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
                        MyPanel.this.repaint();

                        System.out.println("All players will open top cards. To do this, press Enter");
                        break;
                    default:
                        break;
                }

                for (int i = 0; i < client.getPlayersCount(); i++) {
                    if (client.getPlayer(i).getCardsCount() == 0) {
                        mesOk = 1;
                        message = "Player %s won! It's very good :)\n" + client.getPlayer(i).getName();
                        MyPanel.this.repaint();
                        System.out.printf("Player %s won! It's very good :)\n", client.getPlayer(i).getName());
                    }
                }
                MyPanel.this.repaint();
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
