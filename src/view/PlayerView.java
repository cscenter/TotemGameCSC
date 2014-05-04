package view;

import model.Player;
import utils.Configuration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * класс, ответственный за графическое представление игрока
 */
public class PlayerView {
    /**
     * масштаб картинки
     */
    private static int scale;
    /**
     * верхняя карта
     */
    private CardView topCardView;
    private Image avatarImage;
    /**
     *
     * @return ссылку на изображение верхней карты
     */
    public Image getTopCardViewImage() {
        return topCardView.getCardImage();
    }
    /**
     * Информация об игроке
     */
    private Player playerInfo;

    /**
    * возвращает тип верхней карты
    */
    public model.Card.CardType getTopCardType() {
        return playerInfo.getTopCardType();
    }
    /**
     * координата по оси x
     */
    private int xCoordinate;
    /**
     * координата по оси y
     */
    private int yCoordinate;
    /**
     * угол от центра для игрока
     */

    private int closeCardCenterX;
    private int closeCardCenterY;
    private int openCardCenterX;
    private int openCardCenterY;
    private int avatarCenterX;
    private int avatarCenterY;
    private int avatarHeight;
    private int avatarWeight;
    private int nameXCoord;
    private int nameYCoord;
    private int closeCardsNumberCoordX;
    private int closeCardsNumberCoordY;
    private int openCardsNumberCoordX;
    private int openCardsNumberCoordY;
    private int keysCoordX;
    private int keysCoordY;
    private double angle;
    /**
     * клавиша открытия верхней карты
     */
    private char openCardKey;
    /**
     * клавиша захвата тотеме
     */
    private char catchTotemKey;

    /**
     * возвращает клавишу открытия карты
     * @return клавишу открытия карты
     */
    public char getOpenCardKey() {
        return openCardKey;
    }

    /**
     * возвращает клавишу захвата тотема
     * @return клавишу захвата тотема
     */
    public char getCatchTotemKey() {
        return catchTotemKey;
    }

    /**
     * изменяет масштаб картинок
     * @param size новый масштаб
     */
    public static void setScale(int size) {
        scale = size;

    }

    /**
     * задаёт новую верхнюю картинку
     * @param cardsView список из всех картинок
     */
    public void setTopCardView(ArrayList<CardView> cardsView) {
        //      System.err.println("we are in setTopCV!!!");
//        System.err.println(playerInfo.getTopOpenedCard().getCardNumber());
        topCardView = cardsView.get(playerInfo.getTopOpenedCard().getCardNumber());
    }

    /**
     * изменяет масштаб
     * @param haracteristicScale новый масштаб
     */
    public void resize(int haracteristicScale, MyPanel panel) {
        int basicFontSize = haracteristicScale / 35;
        Font font = new Font("Tahoma", Font.BOLD, basicFontSize * 4 / 3);
        int stew = panel.getFontMetrics(font).stringWidth(getPlayerName());
        openCardCenterX = (int) ((haracteristicScale * MyPanel.sizesDiv / 3.3)  * Math.sin(angle * Math.PI / 180) *
                (1 + Math.abs(Math.sin(2 * angle * Math.PI / 180))*0.3)
                + haracteristicScale * MyPanel.sizesDiv/ 2);
        openCardCenterY = (int) ((haracteristicScale / 3.5)  * Math.cos(angle * Math.PI / 180)  *
                (1 + Math.abs(Math.sin(2 * angle * Math.PI / 180))*0.3) + haracteristicScale / 2);

        xCoordinate = openCardCenterX; //(int) ((haracteristicScale / 3.5) * Math.sin(angle * Math.PI / 180) + haracteristicScale / 2.2) +
            //haracteristicScale / 15;
        yCoordinate = openCardCenterY; //(int) ((haracteristicScale / 3.5) * Math.cos(angle * Math.PI / 180) + haracteristicScale / 2.5) +
               // haracteristicScale / 15;
        closeCardCenterX = xCoordinate + CardView.getCardSize() / 2;
        closeCardCenterY = yCoordinate - CardView.getCardSize() / 2;
        avatarHeight = haracteristicScale / 20;
        avatarWeight = haracteristicScale / 20;
        avatarCenterX = xCoordinate - avatarWeight / 2 - avatarWeight / 10;
        avatarCenterY = yCoordinate;
        nameXCoord = avatarCenterX + avatarWeight*2;
        nameYCoord = avatarCenterY;
        openCardsNumberCoordX = 0;
        openCardsNumberCoordY = 0;
        closeCardsNumberCoordX = 0;
        closeCardsNumberCoordY = 0;

        Font font2 = new Font("Tahoma", Font.BOLD, basicFontSize);
        int snumW = panel.getFontMetrics(font2).stringWidth("888");
        int skeysW = panel.getFontMetrics(font2).stringWidth("w, w");
        //низ
        if ((openCardCenterX / MyPanel.sizesDiv < openCardCenterY) &&
                (haracteristicScale - openCardCenterX / MyPanel.sizesDiv < openCardCenterY)) {
            int CS = CardView.getCardSize();
            closeCardCenterX = xCoordinate + CS / 4;
            closeCardCenterY = yCoordinate - (int) (CS * 3.2 / 4);
            avatarCenterX = openCardCenterX - CS / 2 + avatarWeight;
            avatarCenterY = openCardCenterY + CS / 2 + avatarHeight;
            nameXCoord = avatarCenterX + avatarWeight;
            nameYCoord = avatarCenterY;

            openCardsNumberCoordX = openCardCenterX + CS * 11 / 20;
            openCardsNumberCoordY = openCardCenterY + CS / 2;
            closeCardsNumberCoordX = closeCardCenterX + CS * 11 / 40;
            closeCardsNumberCoordY = closeCardCenterY + CS / 4;

            keysCoordX = openCardCenterX - skeysW;
            keysCoordY = openCardCenterY - CS / 2 - basicFontSize / 2;
        }
        //верх
        if ((openCardCenterX / MyPanel.sizesDiv > openCardCenterY) &&
                (haracteristicScale - openCardCenterX / MyPanel.sizesDiv > openCardCenterY)) {
            int CS = CardView.getCardSize();
            closeCardCenterX = xCoordinate + CS / 4;
            closeCardCenterY = yCoordinate + (int) (CS * 3.2 / 4);
            avatarCenterX = openCardCenterX - CS / 2 + avatarWeight;
            avatarCenterY = openCardCenterY - CS / 2 - avatarHeight * 5 / 4;
            nameXCoord = avatarCenterX + avatarWeight;
            nameYCoord = avatarCenterY;

            openCardsNumberCoordX = openCardCenterX + CS * 11 / 20;
            openCardsNumberCoordY = openCardCenterY - CS / 2 + basicFontSize;
            closeCardsNumberCoordX = closeCardCenterX + CS * 11 / 40;
            closeCardsNumberCoordY = closeCardCenterY - CS / 4 + basicFontSize;

            keysCoordX = openCardCenterX - skeysW;
            keysCoordY = openCardCenterY + CS / 2 + basicFontSize * 5 / 4;


        }
        //право
        if ((openCardCenterX / MyPanel.sizesDiv >= openCardCenterY) &&
                (haracteristicScale - openCardCenterX / MyPanel.sizesDiv <= openCardCenterY)) {
            int CS = CardView.getCardSize();
            closeCardCenterX = xCoordinate - (int) (CS * (2 + 1 + 0.2) / 4);
            closeCardCenterY = yCoordinate + (int) (CS / 4);
            avatarCenterX = openCardCenterX + CS / 2 + avatarWeight;
            avatarCenterY = openCardCenterY - CS / 2 + avatarHeight;
            nameXCoord = Math.max(avatarCenterX - stew / 2, openCardCenterX + CS / 2);
            nameYCoord = avatarCenterY + avatarHeight + basicFontSize / 3;

            openCardsNumberCoordX = openCardCenterX + CS / 2 - snumW;
            openCardsNumberCoordY = openCardCenterY + CS / 2 + basicFontSize;
            closeCardsNumberCoordX = closeCardCenterX + CS / 4 - snumW;
            closeCardsNumberCoordY = closeCardCenterY + CS / 4 + basicFontSize;

            keysCoordX = openCardCenterX - CS / 2 - skeysW;
            keysCoordY = openCardCenterY - basicFontSize / 2;

        }
        //лево
        if ((openCardCenterX / MyPanel.sizesDiv <= openCardCenterY) &&
                (haracteristicScale - openCardCenterX / MyPanel.sizesDiv >= openCardCenterY)) {
            int CS = CardView.getCardSize();
            closeCardCenterX = xCoordinate + (int) (CS * (2 + 1 + 0.2) / 4);
            closeCardCenterY = yCoordinate + (int) (CS / 4);
            avatarCenterX = openCardCenterX - CS / 2 - avatarWeight * 3 / 2;
            avatarCenterY = openCardCenterY - CS / 2 + avatarHeight;
            nameXCoord = Math.min(avatarCenterX - stew / 2, openCardCenterX - CS / 2 - stew);
            nameYCoord = avatarCenterY + avatarHeight + basicFontSize / 3;

            openCardsNumberCoordX = openCardCenterX - CS / 2;
            openCardsNumberCoordY = openCardCenterY + CS / 2 + basicFontSize;
            closeCardsNumberCoordX = closeCardCenterX - CS / 4;
            closeCardsNumberCoordY = closeCardCenterY + CS / 4 + basicFontSize;

            keysCoordX = openCardCenterX + CS / 2 +  basicFontSize / 4;
            keysCoordY = openCardCenterY - basicFontSize / 2;

        }
    }

    Point getOpenCardCenter() {
        return new Point(openCardCenterX, openCardCenterY);
    }
    /**
     * возвращает имя игрока
     * @return имя игрока
     */
    public String getPlayerName() {
        return playerInfo.getName();
    }

    /**
     * конструктор. задаёт кнопки, угол и ссылку на информацию об игроке
     * @param newOpenCardKey кнопка открытия карты
     * @param newCatchTotemKey кнопка захвата тотема
     * @param player информация об игроке
     * @param a угол, на котором игрок будет располагаться
     */
    public PlayerView(char newOpenCardKey, char newCatchTotemKey, Player player, double a, Image ava) {
        openCardKey = newOpenCardKey;
        catchTotemKey = newCatchTotemKey;
        angle = a;
        avatarImage = ava;
        xCoordinate = (int) ((scale / 3.5) * Math.sin(angle * Math.PI / 180) + scale / 2.2);
        yCoordinate = (int) ((scale / 3.5) * Math.cos(angle * Math.PI / 180) + scale / 2.5);
        if (player == null) {
            System.err.println("player is null!");
            throw new RuntimeException();
        }
        playerInfo = player;

    }

    /**
     * проверяет, легла ли точка на аватарку
     * @param p координаты точки
     * @return флаг попадания
     */
    public boolean isIn(Point p) {
        return ((Math.abs(p.x - avatarCenterX) < avatarWeight / 2) && (Math.abs(p.y - avatarCenterY) < avatarHeight / 2));
    }

    /**
     * рисует игрока
     * @param g ссылка на графику
     * @param panel ссылка на панельку
     */
    public void drawPlayer(Graphics g, MyPanel panel, int basicFontSize, int whoWillGo){
        g.setColor(Color.cyan);
        g.drawImage(avatarImage, avatarCenterX - avatarWeight / 2, avatarCenterY - avatarHeight / 2,
                avatarWeight, avatarHeight, panel);
        Font font = new Font("Tahoma", Font.BOLD, basicFontSize * 4 / 3);
        g.setFont(font);
        g.drawString(playerInfo.getName(), nameXCoord, nameYCoord);
        font = new Font("Tahoma", Font.BOLD, basicFontSize);
        g.setFont(font);
        String openCardsNumber = String.valueOf(playerInfo.getOpenCardsCount());
        g.drawString(openCardsNumber, openCardsNumberCoordX, openCardsNumberCoordY);

        if (playerInfo.getOpenCardsCount() != 0) {
            Image image;
            try {
                image = topCardView.getCardImage();
                int halfCS = CardView.getCardSize() / 2;
                g.drawImage(image, openCardCenterX - halfCS, openCardCenterY - halfCS, 2 * halfCS, 2 * halfCS, panel);
            } catch (Exception e) {
                System.err.println("can't find image!");
            }
        } else {
            int halfCS = CardView.getCardSize() / 2;
            g.drawImage(Configuration.getGallery().getImage("alphaback.png"), openCardCenterX - halfCS, openCardCenterY - halfCS, 2 * halfCS, 2 * halfCS, panel);
            g.drawRect(openCardCenterX - halfCS, openCardCenterY - halfCS, 2 * halfCS, 2 * halfCS);
        }
        if (playerInfo.getCloseCardsCount() != 0) {
            int halfCS = CardView.getCardSize() / 4;
            g.drawImage(Configuration.getGallery().getImage("data/tback.jpg"), closeCardCenterX - halfCS, closeCardCenterY - halfCS,
                    2 * halfCS, 2 * halfCS, panel);
        }
        String closeCardsNumber = String.valueOf(playerInfo.getCloseCardsCount());
        g.drawString(closeCardsNumber, closeCardsNumberCoordX, closeCardsNumberCoordY);
        String catchKey = String.valueOf(openCardKey) + ", " + String.valueOf(catchTotemKey);
        g.drawString(catchKey, keysCoordX, keysCoordY);
//        g.drawImage(Configuration.getGallery().getImage("conf/1_sec.png"), 10, 10, 100, 100, panel);
        if (playerInfo.getId() == whoWillGo) {
            g.drawImage(Configuration.getGallery().getImage("conf/turn_switch_indicator.png"),
                avatarCenterX - avatarWeight*3/2, avatarCenterY - avatarHeight/2, avatarWeight, avatarHeight, panel);
            g.drawOval(avatarCenterX - avatarWeight*3/2, avatarCenterY - avatarHeight/2, avatarWeight, avatarHeight);

        }

    }
}
