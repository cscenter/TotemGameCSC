import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardView{
    private static int cardSize;
    public static int getCardSize(){
        return cardSize;
    }
    public CardView(ClassLoader cl, Card card, Image img, int id){
        this.card = card;
//        this.fileCard = fileCard;
            this.cardImage = img;
        this.id = id;
    }
    private final Card card;
//    private final File fileCard;
    private final Image cardImage;
    private final int id;

    public Image getCardImage(){
        return cardImage;
    }
    public int getId(){
        return id;
    }
    public static ArrayList<Integer> getCardsNumbers(Gallery dd){
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<String> cardsNames = dd.getCardsNames();
        for (String cardName : cardsNames){
            result.add(getCardNumber(cardName));
        }
        return result;
    }
    private static int getCardNumber(String str){
//        System.err.println(str);
        Pattern numberPattern = Pattern.compile("[0-9]+");
        Matcher numberMatcher = numberPattern.matcher(str);
        numberMatcher.find();
        return Integer.parseInt(numberMatcher.group());
    }
    public static void resize(int haracteristicScale) {
        cardSize = haracteristicScale / 10;
    }
//    public CardView getCardView(Card card){
//      return
//}
}
