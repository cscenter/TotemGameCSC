package view;

import model.Player;
import utils.Configuration;

import java.awt.*;
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
    public void resize(int haracteristicScale) {
        xCoordinate = (int) ((haracteristicScale / 3.5) * Math.sin(angle * Math.PI / 180) + haracteristicScale / 2.2) +
            haracteristicScale / 15;
        yCoordinate = (int) ((haracteristicScale / 3.5) * Math.cos(angle * Math.PI / 180) + haracteristicScale / 2.5) +
                haracteristicScale / 15;
        closeCardCenterX = xCoordinate + CardView.getCardSize() / 2;
        closeCardCenterY = yCoordinate - CardView.getCardSize() / 2;
        openCardCenterX = closeCardCenterX;
        openCardCenterY = closeCardCenterY + (int) (CardView.getCardSize()*1.2);
        avatarHeight = haracteristicScale / 7;
        avatarWeight = haracteristicScale / 7;
        avatarCenterX = xCoordinate - avatarWeight / 2 - avatarWeight / 10;
        avatarCenterY = yCoordinate;
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
    public PlayerView(char newOpenCardKey, char newCatchTotemKey, Player player, double a) {
        openCardKey = newOpenCardKey;
        catchTotemKey = newCatchTotemKey;
        angle = a;
        xCoordinate = (int) ((scale / 3.5) * Math.sin(angle * Math.PI / 180) + scale / 2.2);
        yCoordinate = (int) ((scale / 3.5) * Math.cos(angle * Math.PI / 180) + scale / 2.5);
        if (player == null) {
            System.err.println("player is null!");
            throw new RuntimeException();
        }
        playerInfo = player;

    }

    /**
     * проверяет, легла ли точка на карту
     * @param p координаты точки
     * @return флаг попадания
     */
    public boolean isIn(Point p) {
        if ((p.getX() < xCoordinate + CardView.getCardSize()) && (p.getX() > xCoordinate - CardView.getCardSize())) {
            if ((p.getY() < yCoordinate + CardView.getCardSize() + 40) && (p.getY() > yCoordinate - 40)) {
                return true;
            }
        }
        return false;
    }

    /**
     * очищает прямоугольник с игроком
     * @param g ссылка на графику
     */
    public void clear(Graphics g) {
        g.clearRect(xCoordinate - (int) (CardView.getCardSize() * 2.1), yCoordinate, (int) (3.1 * CardView.getCardSize()), (int) (CardView.getCardSize() * 2.5));
    }

    /**
     * рисует игрока
     * @param g ссылка на графику
     * @param panel ссылка на панельку
     */
    public void drawPlayer(Graphics g, MyPanel panel, int basicFontSize){
        g.setColor(Color.cyan);
        g.drawImage(Configuration.getGallery().getImage("data/tboy.png"), avatarCenterX - avatarWeight / 2, avatarCenterY - avatarHeight / 2,
                avatarWeight, avatarHeight, panel);
        Font font = new Font("Tahoma", Font.BOLD, basicFontSize * 4 / 3);
        g.setFont(font);
        g.drawString(playerInfo.getName(), avatarCenterX - avatarWeight / 2, avatarCenterY - avatarHeight / 2 -
                basicFontSize);
        font = new Font("Tahoma", Font.BOLD, basicFontSize);
        g.setFont(font);
        String openCardsNumber = String.valueOf(playerInfo.getOpenCardsCount());
        g.drawString(openCardsNumber, openCardCenterX + CardView.getCardSize() / 2 + basicFontSize / 3,
                openCardCenterY + CardView.getCardSize() / 2);

        if (playerInfo.getOpenCardsCount() != 0) {
            Image image;
            try {
                image = topCardView.getCardImage();
                int halfCS = CardView.getCardSize() / 2;
                g.drawImage(image, openCardCenterX - halfCS, openCardCenterY - halfCS, 2 * halfCS, 2 * halfCS, panel);
            } catch (Exception e) {
                System.err.println("can't find image!");
            }
        }
        if (playerInfo.getCloseCardsCount() != 0) {
            int halfCS = CardView.getCardSize() / 2;
            g.drawImage(Configuration.getGallery().getImage("data/tback.jpg"), closeCardCenterX - halfCS, closeCardCenterY - halfCS,
                    2 * halfCS, 2 * halfCS, panel);
        }
        String closeCardsNumber = String.valueOf(playerInfo.getCloseCardsCount());
        g.drawString(closeCardsNumber, closeCardCenterX + CardView.getCardSize() / 2 + basicFontSize / 3,
                closeCardCenterY + CardView.getCardSize() / 2);
        String catchKey = String.valueOf(openCardKey) + ", " + String.valueOf(catchTotemKey);
        g.drawString(catchKey, closeCardCenterX, closeCardCenterY - CardView.getCardSize() / 2 - basicFontSize);
}
}
