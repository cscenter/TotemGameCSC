public enum ExtraCard {
ExtraCardArrowsOut(501),
ExtraCardArrowsIn(502),
ExtraCardColorMode(503);

private int value;
private ExtraCard(int value){
this.value = value;
}

public int getValue() {
return value;
}
};


class Card
{
    int color;
    int number;
    
    
    public int getCardNumber()
    {
        return color*100 + number;
    }
}