/*public enum ExtraCard {
    extraCardArrowsOut(501),
    extraCardArrowsIn(502),
    extraCardColorMode(503);

    private int value;
    private ExtraCard(int value){
        this.value = value;
    }

    public int GetValue() {
        return value;
    }
};
  */

class Card{
    int color;
    int number;
    
    
    public int GetCardNumber()    {
        return color*100 + number;
    }
}