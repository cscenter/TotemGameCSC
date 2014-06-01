package view;

import controller.TotemClient;
import model.Card;
import model.Game;
import utils.Configuration;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyPanel extends JPanel {
    private TotemClient client;
    private int panel_size;
    private boolean catchTotemModeFlag;
    private boolean multyDuelFlag;
    private int whoPlayed;
    private boolean isGameEnded = false;
    private String message;
    private int mesOk;
    private int typeTotem;
    private Point lastClicedPlace = null;
    static double sizesDiv = 4.0 / 3;
    private int xMes;
    int basicFontSize = 15;
    private TotemView totemV;
    private ArrayList<PlayerView> playersView;
    private int pos;
    private Timer openCardTimer;
    private Timer gameEndTimer;
    private int timeToEndGame;
    private boolean openCardAnimFlag;
    private int timeToOpenCard = 0;
    class AnimListenerCardOpen implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            repaint();
            timeToOpenCard++;
        }
    }
    class AnimListenerGameEnded implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            repaint();
            timeToEndGame++;
        }
    }


    private Timer arrowsOutTimer;
    private boolean arrowsOutAnimFlag;
    private int timeOfArrowsOut = 4;
    class AnimListenerArrowsOut implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            repaint();
            timeOfArrowsOut--;
        }
    }


    private ArrayList<CardView> cardsView;
    private WhatRepaint whatRepaint;
    private enum WhatRepaint {
        NEW_CARD,
        NEW_CARD_WITH_ARROWS_OUT,
        NOT_DEF,
        ALL_OPEN_TOP_CARD_COUNT,
        ALL_OPEN_TOP_CARD_ANIM,
        GAME_END;
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
            String cardsCount = String.valueOf(totem.getCardsCount());


            if (typeTotem == 1) {
                setTotemAura(PlayerView.PlayerAura.RED);
//                Image img2 = Configuration.getGallery().getImage("data/conf/sq_red.png");
  //              g.drawImage(img2, xCoord - totemWeight*3/3, yCoord - totemHeight*3/3, totemWeight*6/3, totemHeight*6/3, MyPanel.this);
                typeTotem = 0;
            }
            if (typeTotem == 2) {
                setTotemAura(PlayerView.PlayerAura.GREEN);
//                Image img2 = Configuration.getGallery().getImage("data/conf/sq_green.png");
  //              g.drawImage(img2, xCoord - totemWeight*3/3, yCoord - totemHeight*3/3, totemWeight*6/3, totemHeight*6/3, MyPanel.this);
                typeTotem = 0;
            }
            drawTotemAura (g, MyPanel.this);
            setTotemAura(PlayerView.PlayerAura.NOT);

            Image img = Configuration.getGallery().getImage("data/totem.png");
            g.drawImage(img, xCoord - totemWeight /2, yCoord - totemHeight / 2, totemWeight, totemHeight, MyPanel.this);

            Font font = new Font("Tahoma", Font.BOLD, basicFontSize);
            g.setColor(Color.cyan);
            g.setFont(font);
            int stringw = g.getFontMetrics(font).stringWidth(cardsCount);
            g.drawString(cardsCount, xCoord - stringw / 2, yCoord + totemHeight / 2);
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
        private PlayerView.PlayerAura totemAura = PlayerView.PlayerAura.NOT;
        public void setTotemAura(PlayerView.PlayerAura pa){
            totemAura = pa;
        }
        private void drawTotemAura(Graphics g, MyPanel panel) {
            Image image = null;
            switch (totemAura) {
                case GREEN:
                    image = Configuration.getGallery().getImage("data/conf/sq_green.png");
                break;
                case RED:
                    image = Configuration.getGallery().getImage("data/conf/sq_red.png");
                break;
                case BLUE:
                    image = Configuration.getGallery().getImage("data/conf/sq_blue.png");
                break;
            }
            if (totemAura != PlayerView.PlayerAura.NOT) {
                g.drawImage(image, xCoord - totemWeight*3/3, yCoord - totemHeight*3/3, totemWeight*6/3, totemHeight*6/3, panel);
            }

        }
    }

    private void reSize(int haracteristicScale) {
        basicFontSize = haracteristicScale / 35;
        panel_size = haracteristicScale;
        CardView.resize(haracteristicScale);
        PlayerView.setScale(haracteristicScale);
        totemV.resize(haracteristicScale);
        for (PlayerView player : playersView) {
            player.resize(haracteristicScale, this);
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
                reSize(Math.min(g.getClipBounds().height, (int) (g.getClipBounds().width  / sizesDiv)));
                repaintAll(g);
                break;
            case NEW_CARD_WITH_ARROWS_OUT:
            case NEW_CARD:
                int who = whatRepaint.lastPlayerWhoAct;
                PlayerView currPlayer = playersView.get(who);
                Point p = currPlayer.getOpenCardCenter();
                int sizeX1 = CardView.getCardSize();
                int sizeY1 = CardView.getCardSize();
                int sizeX2 = (int) (CardView.getCardSize() * 1.1);
                int sizeY2 = (int) (CardView.getCardSize() * 1.1);
                Image cardI = currPlayer.getTopCardViewImage();                
                makeImageBiggerAnimation(g, 10, cardI, p, sizeX1, sizeY1, sizeX2, sizeY2);
                break;
            case ALL_OPEN_TOP_CARD_COUNT:
                reSize(Math.min(g.getClipBounds().height, (int) (g.getClipBounds().width  / sizesDiv)));
                repaintAll(g);
                if (playersView.get(whatRepaint.lastPlayerWhoAct).getTopCardType() == 
                           Card.CardType.ARROWS_OUT) {
                    Font font = new Font("Tahoma", Font.BOLD, panel_size/5);
                    g.setColor(Color.blue);
                    g.setFont(font);
                    String count = String.valueOf(timeOfArrowsOut);
                    g.drawString(count, panel_size/2, panel_size/2);
                    if (timeOfArrowsOut <= 0) {
                        arrowsOutTimer.stop();
                        arrowsOutAnimFlag = false;
                        reSize(Math.min(g.getClipBounds().height, (int) (g.getClipBounds().width  / sizesDiv)));
                        client.openAllTopCards();
                        for (PlayerView player : playersView) {
                            player.setTopCardView(cardsView);
                        }                    
                        timeToOpenCard = 0;
                        AnimListenerCardOpen animListener = new AnimListenerCardOpen();
                        openCardAnimFlag = true;
                        openCardTimer = new Timer(50, animListener);
                        openCardTimer.start();
                        whatRepaint = WhatRepaint.ALL_OPEN_TOP_CARD_ANIM;
                    }
                } else {
                    arrowsOutTimer.stop();
                    arrowsOutAnimFlag = false;
                    whatRepaint = WhatRepaint.NOT_DEF;
                    reSize(Math.min(g.getClipBounds().height, (int) (g.getClipBounds().width  / sizesDiv)));                    
                }
                break;
            case ALL_OPEN_TOP_CARD_ANIM:
                for (int i = 0; i < Configuration.getNumberOfPlayers(); i++) {
                    currPlayer = playersView.get(i);
                    p = currPlayer.getOpenCardCenter();
                    sizeX1 = CardView.getCardSize();
                    sizeY1 = CardView.getCardSize();
                    sizeX2 = (int) (CardView.getCardSize() * 1.1);
                    sizeY2 = (int) (CardView.getCardSize() * 1.1);
                    cardI = currPlayer.getTopCardViewImage();
                    makeImageBiggerAnimation(g, 10, cardI, p, sizeX1, sizeY1, sizeX2, sizeY2);
                }
                break;
            case GAME_END:
                gameEndAnimation(g);
            break;
        }

        if (isGameEnded) {
            if (whatRepaint == WhatRepaint.NOT_DEF) {
                whatRepaint = WhatRepaint.GAME_END;
                timeToEndGame = 0;
                AnimListenerGameEnded animListener = new AnimListenerGameEnded();
                gameEndTimer = new Timer(100, animListener);
                gameEndTimer.start();
            }
        }
        if (lastClicedPlace != null) {
         //   drawTarget(g, lastClicedPlace);
//            lastClicedPlace = null;
        }

    }

    private void repaintAll(Graphics g) {
        int x = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        g.clearRect(0, 0, x, y);
        Image img = Configuration.getGallery().getImage("data/b1.png");
        g.drawImage(img, 0, 0, (int) (panel_size * sizesDiv), panel_size,  null);
        totemV.drawTotem(g);
        int whoWillGo = client.getPlayerWhoWillGo();
        for (PlayerView player : playersView) {
            player.drawPlayer(g, this, basicFontSize, whoWillGo);
        }
    }
    public void repaintModel(Queue<Game.WhatPlayerDid> whatDid,
                            boolean isSmbOpen,boolean isSmbdCatch) {
        Game.ResultOfMakeMove resultOfMakeMove;
        whoPlayed = 0;
        if (isSmbdCatch){
            Game.WhatPlayerDid what = whatDid.remove();
            whoPlayed = what.whoWasIt;
            ArrayList<Integer> possibleLosers = client.checkDuelWithPlayer(client.getPlayer(whoPlayed));
            for (Integer i : possibleLosers) {
                    playersView.get(i).setPlayerAura(PlayerView.PlayerAura.BLUE);
            }
            resultOfMakeMove = client.makeMove(what);
            switch (resultOfMakeMove) {
                case TOTEM_WAS_CATCH_CORRECT:
                    mesOk = 1;
                    message = "You won duel, " + client.getPlayer(whoPlayed).getName() + " All your open cards and all cards under totem go to your opponent";
                    typeTotem = 2;
//                    MyPanel.this.repaint();
                    System.out.printf("You won duel, %s! All your open cards and all cards under totem go to your opponent\n",
                            client.getPlayer(whoPlayed).getName());
                    playersView.get(whoPlayed).setPlayerAura(PlayerView.PlayerAura.GREEN);
                    playersView.get(possibleLosers.get(0)).setPlayerAura(PlayerView.PlayerAura.RED);
                    break;
                case TOTEM_WAS_CATCH_INCORRECT:
                    mesOk = 1;
                    message = "You mustn't take totem, " + client.getPlayer(whoPlayed).getName() + " So you took all open cards!";
                    typeTotem = 1;
                    playersView.get(whoPlayed).setPlayerAura(PlayerView.PlayerAura.RED);
  //                  MyPanel.this.repaint();
                    System.out.printf("You mustn't take totem, %s! So you took all open cards!\n",
                            client.getPlayer(whoPlayed).getName());
                    break;
                case NOT_DEFINED_CATCH:
                    if (client.getGameMode() == Game.GameMode.CATCH_TOTEM_MODE) {
                        catchTotemModeFlag = true;
                        totemV.setTotemAura(PlayerView.PlayerAura.BLUE);
                    } else {
                        multyDuelFlag = true;
                    }
                    break;
                default:
                    break;
            }
        }

        if (isSmbOpen){
            while (whatDid.size() > 1){
                whatDid.remove();
            }
            Game.WhatPlayerDid whatPlayerDid = whatDid.remove();
            whoPlayed = whatPlayerDid.whoWasIt;
            resultOfMakeMove = client.makeMove(whatPlayerDid);

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

                    if (client.getPlayer(whoPlayed).getCardsCount() == 1) {

                    }

                    if (! openCardAnimFlag) {
                        timeToOpenCard = 0;
                        AnimListenerCardOpen animListener = new AnimListenerCardOpen();
                        openCardAnimFlag = true;
                        openCardTimer = new Timer(50, animListener);
                        openCardTimer.start();
                    }
                    break;
                case NOT_DEFINED_CATCH:
                    if (client.getGameMode() == Game.GameMode.CATCH_TOTEM_MODE) {
                        catchTotemModeFlag = true;
                        totemV.setTotemAura(PlayerView.PlayerAura.BLUE);
                    } else {
                        multyDuelFlag = true;
                    }
                    break;
                case ALL_CARDS_OPENED:
                    playersView.get(whoPlayed).setTopCardView(cardsView);
                    whatRepaint = WhatRepaint.NEW_CARD_WITH_ARROWS_OUT;
                    whatRepaint.lastPlayerWhoAct = whoPlayed;
                    if (! openCardAnimFlag) {
                        timeToOpenCard = 0;
                        AnimListenerCardOpen animListener = new AnimListenerCardOpen();
                        openCardAnimFlag = true;
                        openCardTimer = new Timer(50, animListener);
                        openCardTimer.start();
                    }
                    arrowsOutAnimFlag = true;
/*                    for (PlayerView player : playersView) {
                        player.setTopCardView(cardsView);
                    }
*/
                    mesOk = 1;
                    message = "All players will open top cards. To do this, press Enter";
    //                MyPanel.this.repaint();

                    System.out.println("All players will open top cards. To do this, press Enter");
                    break;
                default:
                    break;
            }
        }
        for (int i = 0; i < client.getPlayersCount(); i++) {
            if (client.getPlayer(i).getCardsCount() == 0) {
                isGameEnded = true;
            }
        }


        if (isSmbdCatch || isSmbOpen) {
            repaint();
        }
    }
    private int checkWhoHasArrowsOut() {
        for (int i = 0; i < playersView.size(); i++) {
            PlayerView player = playersView.get(i);
            if (player.getTopCardType() == Card.CardType.ARROWS_OUT) {
                return i;
            }
        }
        return -1;
    }


    private void gameEndAnimation(Graphics g) {
        Image img = Configuration.getGallery().getImage("data/conf/game_end_r.png");
        repaintAll(g);
        int tMax = 10;
        int v0 = 0;
        int a = (panel_size - v0 * tMax) * 2 / (tMax * tMax);
        int size = 2 * panel_size - v0 * tMax - a * tMax * tMax / 2;

        if (timeToEndGame <= tMax + 1) {
            size = 2 * panel_size - v0 * timeToEndGame - a * timeToEndGame * timeToEndGame / 2;
        }

        if (timeToEndGame >= tMax + 1) {
            for (int i = 0; i < client.getPlayersCount(); i++) {
                if (client.getPlayer(i).getCardsCount() == 0) {
                    playersView.get(i).drawPlayerAtEnd(g, this, basicFontSize, true);
                } else {
                    playersView.get(i).drawPlayerAtEnd(g, this, basicFontSize, false);
                }
            }

            gameEndTimer.stop();
        }
        g.drawImage(img, (int) (((panel_size - size)/2) * sizesDiv), (panel_size - size)/2, 
            (int) (size * sizesDiv), size,  null);

    }

    private void makeImageBiggerAnimation(Graphics g, int steps, Image image, Point center,
                                          int sizeXBegin, int sizeYBegin, int sizeXEnd, int sizeYEnd) {
        if (timeToOpenCard >= steps) {
            openCardTimer.stop();
            timeToOpenCard = 0;
            openCardAnimFlag = false;
            if (whatRepaint == WhatRepaint.NEW_CARD_WITH_ARROWS_OUT) {
                timeOfArrowsOut = 4;
                AnimListenerArrowsOut animListener = new AnimListenerArrowsOut();
                arrowsOutAnimFlag = true;
                int whoAct = whatRepaint.lastPlayerWhoAct;
                whatRepaint = WhatRepaint.ALL_OPEN_TOP_CARD_COUNT;
                whatRepaint.lastPlayerWhoAct = whoAct;
                arrowsOutTimer = new Timer (1000, animListener);
                arrowsOutTimer.start();
                System.err.println("AAAAAAAAAAAAAAAAA");
            } else if (whatRepaint == WhatRepaint.ALL_OPEN_TOP_CARD_ANIM) {
                int playerWithArrows = checkWhoHasArrowsOut();
                if (playerWithArrows != -1) {
                    whatRepaint = WhatRepaint.ALL_OPEN_TOP_CARD_COUNT;
                    whatRepaint.lastPlayerWhoAct = playerWithArrows;
                    timeOfArrowsOut = 4;
                    AnimListenerArrowsOut animListener = new AnimListenerArrowsOut();
                    arrowsOutAnimFlag = true;
                    arrowsOutTimer = new Timer (1000, animListener);
                    arrowsOutTimer.start();
                }
            } else {
                arrowsOutAnimFlag = false;
                whatRepaint = WhatRepaint.NOT_DEF;
            }
            repaintAll(g);

        }
        int currXSize = (sizeXEnd - sizeXBegin) * timeToOpenCard / steps + sizeXBegin;
        int currYSize = (sizeYEnd - sizeYBegin) * timeToOpenCard / steps + sizeYBegin;
        g.drawImage(image, center.x - currXSize / 2, center.y - currYSize / 2,
                currXSize, currYSize, this);
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
        openCardAnimFlag = false;
        arrowsOutAnimFlag = false;
        for (int i = 0; i < 400; i++) {
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
       значит надо вычесть - 360.0 * client.getWhatPlayer() / ;
        */
        for (int i = 0; i < ktKeys.size(); i++) {
            double currentAngle = angle.get(i);
            currentAngle = currentAngle - 360.0 * client.getWhatPlayer() / Configuration.getNumberOfPlayers();
            playersView.add(new PlayerView(ocKeys.get(i), ktKeys.get(i), client.getPlayer(i), currentAngle,
                    Configuration.getGallery().getImage("data/avatars/"+i+".png")));
        }
        totemV = new TotemView(client.getTotem());
        addMouseListener(initMyMouseListener());
    }


    public class MyMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (isGameEnded) {
                return;
            }
            Point p = e.getPoint();
//            System.out.println("Mouse cliced "+p);
            if (catchTotemModeFlag) {
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
            lastClicedPlace = p;
            for (int i = 0; i < client.getPlayersCount(); i++) {
                if (client.getPlayer(i).getCardsCount() == 0) {
                    isGameEnded = true;
                }
            }

            MyPanel.this.repaint();
        }
    }


    public class MyKeyListener extends KeyAdapter {
        // нажатие клавиши
        @Override
        public void keyPressed(KeyEvent k) {
            if (isGameEnded) {
                return;
            }
            Character inputChar = k.getKeyChar();
            inputChar = (new Character(inputChar)).toString().toLowerCase().charAt(0);
            boolean suchKeyHere = false;
            for (int i = 0; i < client.getPlayersCount(); i++) {
                if (! arrowsOutAnimFlag) {

                    if (playersView.get(i).getOpenCardKey() == inputChar) {
                        Game.WhatPlayerDid what = Game.WhatPlayerDid.OPEN_NEW_CARD;
                        what.whoWasIt = i;
                        client.moveWithoutAnswer(what);
                        suchKeyHere = true;
                        break;
                    }
                }

                if (playersView.get(i).getCatchTotemKey() == inputChar) {
                    Game.WhatPlayerDid what = Game.WhatPlayerDid.TOOK_TOTEM;
                    what.whoWasIt = i;
                    client.moveWithoutAnswer(what);
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
//            MyPanel.this.repaint();
        }
    }

}
