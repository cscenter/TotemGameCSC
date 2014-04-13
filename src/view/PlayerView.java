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
        xCoordinate = (int) ((haracteristicScale / 3.5) * Math.sin(angle * Math.PI / 180) + haracteristicScale / 2.2) + 40;
        yCoordinate = (int) ((haracteristicScale / 3.5) * Math.cos(angle * Math.PI / 180) + haracteristicScale / 2.5) + 20;

    }

    /**
     * возвращает имя игрока
     * @return имя игрока
     */
    public String getPlayerName() {
        return playerInfo.getName();
    }

    /**
     * возвращает координату игрока по оси x
     * @return координата игрока по оси x
     */
    public int getXCoordinate(){
        return xCoordinate;
    }

    /**
     * возвращает координату по оси y
     * @return координата по оси y
     */
    public int getYCoordinate(){
        return yCoordinate;
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
        // player.setCoordinate(xCoordinate, yCoordinate);
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
    public void drawPlayer(Graphics g, MyPanel panel){
        //clear(g);
        //if (playerInfo.isGO() == false)

        g.drawImage(Configuration.getGallery().getImage("data/tboy.png"), xCoordinate - 110, yCoordinate, panel);
        Font font = new Font("Tahoma", Font.BOLD, 20);
        Font oldFont = g.getFont();
        g.setFont(font);
        g.drawString(playerInfo.getName(), xCoordinate - CardView.getCardSize() / 3, yCoordinate - scale / 30);

        font = new Font("Tahoma", Font.BOLD, 15);
        g.setFont(font);
        String openCardsNumber = String.valueOf(playerInfo.getOpenCardsCount());
        Color oldColor = g.getColor();
        Color newColor = new Color(0, 0, 0);
        g.setColor(newColor);
        g.drawChars(openCardsNumber.toCharArray(), 0, openCardsNumber.length(), xCoordinate + scale / 60 + 70, yCoordinate + CardView.getCardSize() + scale / 30);

        if (playerInfo.getOpenCardsCount() != 0) {
            Image image;
            try {
                image = topCardView.getCardImage();
                g.drawImage(image, xCoordinate, yCoordinate + CardView.getCardSize(), CardView.getCardSize(), CardView.getCardSize(), panel);
            } catch (Exception e) {
                System.err.println("can't find image!");
            }

            g.drawImage(Configuration.getGallery().getImage("data/tback.jpg"), xCoordinate, yCoordinate, CardView.getCardSize(), CardView.getCardSize(), panel);
        } else if (playerInfo.getCloseCardsCount() != 0)
            g.drawImage(Configuration.getGallery().getImage("data/tback.jpg"), xCoordinate, yCoordinate, CardView.getCardSize(), CardView.getCardSize(), panel);

        String closeCardsNumber = String.valueOf(playerInfo.getCloseCardsCount());
        g.drawChars(closeCardsNumber.toCharArray(), 0, closeCardsNumber.length(), xCoordinate + scale / 60 + 70, yCoordinate + CardView.getCardSize() + scale / 30 - CardView.getCardSize());
        g.setColor(oldColor);
//        playerInfo.setCoordinate(xCoordinate, yCoordinate);

        String catchKey = String.valueOf(openCardKey) + ", " + String.valueOf(catchTotemKey);
        //g.setFont(font);
        g.drawChars(catchKey.toCharArray(), 0, catchKey.length(), xCoordinate - (int) (CardView.getCardSize() * 1.5), yCoordinate - scale / 30);
        //g.setFont(oldFont);
    }
}
