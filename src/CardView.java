import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardView{
    static ArrayList<File> cardsFiles;

    public static int cardSize;
    Card card;
    File fileCard;
    Image cardImage;
    int id;
    public CardView(File fc){
        fileCard = fc;
    }

    public CardView(){
        File dir = new File(GraphicsView.DIRECTORY);
//        cards = new ArrayList<>();

        cardsFiles = new ArrayList<>(Arrays.asList(dir.listFiles()));
//        CardView.cardSize = size / 6;

//        image = new ArrayList<>(168);
//        for (int i=0; i<400; i++){
        //          image.add(null);
        //    }
        for (File cardI : cardsFiles){
            Pattern numberPattern = Pattern.compile("[0-9]+");
            Matcher numberMatcher = numberPattern.matcher(cardI.getName());
            numberMatcher.find();
            int num = Integer.parseInt(numberMatcher.group());
            Image im = Toolkit.getDefaultToolkit().getImage(cardI.toString());
            CardView cardView = new CardView(cardI);

//            image.set(num, im);
        }
    }
    public static ArrayList<Integer> getCardsNumbers(){
        ArrayList<Integer> result = new ArrayList<>();
        File dir = new File(GraphicsView.DIRECTORY);

        cardsFiles = new ArrayList<>(Arrays.asList(dir.listFiles()));

        for (int i = 0; i < cardsFiles.size(); i++){
            result.add(getCardNumber(i));
        }
        return result;
    }
    public static int getCardNumber(int index){
        Pattern numberPattern = Pattern.compile("[0-9]+");
        Matcher numberMatcher = numberPattern.matcher(cardsFiles.get(index).getName());
        numberMatcher.find();
        return Integer.parseInt(numberMatcher.group());
    }
//    public CardView getCardView(Card card){
//      return
//}
}
