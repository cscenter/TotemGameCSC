import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardView{
    public static final String DIRECTORY = "data/";
    private static ArrayList<File> cardsFiles;
    public static ArrayList<File> getCardsFiles(){
        return cardsFiles;
    }
    private static int cardSize;
    public static int getCardSize(){
        return cardSize;
    }
    public CardView(Card card, File fileCard, Image cardImage, int id){
        this.card = card;
        this.fileCard = fileCard;
        this.cardImage = cardImage;
        this.id = id;
    }
    private final Card card;
    private final File fileCard;
    private final Image cardImage;
    private final int id;

    public Image getCardImage(){
        return cardImage;
    }
    public int getId(){
        return id;
    }
    public static ArrayList<Integer> getCardsNumbers(){
        ArrayList<Integer> result = new ArrayList<>();
        File dir = new File(DIRECTORY);

        cardsFiles = new ArrayList<>(Arrays.asList(dir.listFiles()));
        
        for (int i = 0; i < cardsFiles.size(); i++){
           try{
            if ((cardsFiles.get(i).getName().startsWith("t")) != true ) 
            {
                
                result.add(getCardNumber(i));
            }}
           catch(Exception e){
               System.out.print(cardsFiles.get(i).getName());
           }
        }
        return result;
    }
    private static int getCardNumber(int index){
        Pattern numberPattern = Pattern.compile("[0-9]+");
        Matcher numberMatcher = numberPattern.matcher(cardsFiles.get(index).getName());
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
