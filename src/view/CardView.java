package view;

import model.Card;
import utils.Configuration;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * визуальная информация о карте
 */
public class CardView {
    /**
     * размер карты
     */
    private static int cardSize;
    /**
     * ссылка на саму карту
     */
    private final Card card;
    /**
     * ссылка на картинку карты
     */
    private final Image cardImage;
    /**
     * код карты
     */
    private final int id;

    /**
     * возвращает размер карты
     * @return размер карты
     */
    public static int getCardSize() {
        return cardSize;
    }

    /**
     * конструктор. инициализирует пременные
     * @param card описание карты
     * @param img изображение карты
     * @param id код карты
     */
    public CardView(Card card, Image img, int id) {
        this.card = card;
        this.cardImage = img;
        this.id = id;
    }

    /**
     * вовращает изображение карты
     * @return изображение карты
     */
    public Image getCardImage() {
        return cardImage;
    }

    /**
     * возвращает код карты
     * @return код карты
     */
    public int getId() {
        return id;
    }

    /**
     * возвращает список из номеров карт
     * @see utils.Gallery#getCardsNames()
     * @return список из номеров карт
     */
    public static ArrayList<Integer> getCardsNumbers() {
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<String> cardsNames = Configuration.getGallery().getCardsNames();
        for (String cardName : cardsNames) {
            result.add(getCardNumber(cardName));
        }
        return result;
    }

    /**
     * возвращает номер карты по строке
     * @see utils.Gallery#getCardNumber(String)
     * @param str строка с номеров карты
     * @return число - номер карты
     */
    private static int getCardNumber(String str) {
//        System.err.println(str);
        return Configuration.getGallery().getCardNumber(str);
    }

    /**
     * делает изменение размера картинки по характеристическому масштабу
     * @param haracteristicScale характеристический масштаб
     */
    public static void resize(int haracteristicScale) {
        cardSize = haracteristicScale / 10;
    }
}
