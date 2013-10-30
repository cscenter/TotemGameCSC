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
    }
    final private int color;
    final private int number;
    final private CardType cardType;
    CardType getCardType(){
        return cardType;
    }
    public Card (int number){
        color = number % 10;
        this.number = number / 10;
        if (color == 5){
            switch (this.number / 10){
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
        }else{
            cardType = CardType.NORMAL;
        }
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