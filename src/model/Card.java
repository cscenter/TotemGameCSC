package model;

/**
 * Класс - одна карта. У неё есть цвет, id и тип
 */
public class Card {
    /**
     * цвет карты закодированный в число
     * 1 - синий
     * 2 - жёлтый
     * 3 - зелёный
     * 4 - красный
     * 5 - спец. карта
     */
    final private int color;
    /**
     * форма карты, закодированныая в число
     */
    final private int number;
    /**
     * тип карты
     */
    final private CardType cardType;

    /**
     * тип карты:
     * обычная, "стрелки наружу", "стрелки внутрь", "цветные стрелки"
     */
    public enum CardType {
        NORMAL,
        ARROWS_OUT,
        ARROWS_IN,
        ARROWS_COLORED,
    }

    /**
     * преобразование в строчку. Отличается от стандартного наличием ведущих нулей
     * @return строковая кодировка карты
     */
    @Override
    public String toString() {
        int num = getCardNumber();
        if (num < 100) {
            return "0" + num;
        }
        return num + "";
    }

    /**
     * @return тип карты
     */
    CardType getCardType() {
        return cardType;
    }

    /**
     * по числу генерирует карту
     * @param number код карты
     */
    public Card(int number) {
        color = number % 10;
        this.number = number / 10;
        if (color == 5) {
            switch (this.number / 10) {
                case 0:
                    cardType = CardType.ARROWS_COLORED;
                    break;
                case 1:
                    cardType = CardType.ARROWS_IN;
                    break;
                case 2:
                    cardType = CardType.ARROWS_OUT;
                    break;
                default:
                    cardType = CardType.NORMAL;
            }
        } else {
            cardType = CardType.NORMAL;
        }
    }

    /**
     * @return номер+цвет карты
     */
    public int getCardNumber() {
        return number * 10 + color;
    }

    /**
     * @return цвет карты
     */
    public int getCardColor() {
        return color;
    }

    /**
     * @return номер формы карты
     */
    public int getCardFormNumber() {
        return number;
    }
}