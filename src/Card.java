/**
 * Класс - одна карта. У неё есть цвет, id и тип
*/
class Card{
    public enum CardType{
        NORMAL,
        ARROWS_OUT,
        ARROWS_IN,
        ARROWS_COLORED,
//        HAND_IN,
//        ARROWS_TO_NEXT;
    };
    final private int color;
    final private int number;
    final private CardType cardType;
    CardType getCardType(){
        return cardType;
    }
    public Card(int c, int n){
        color = c;
        number = n;
        cardType = CardType.NORMAL;
    }
    public int getCardNumber(){
        return number * 10 + color;
    }
    public int getCardColor(){
        return color;
    }
    public int getCardFormNumber(){
        return number;
    }
}