import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.*;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CardView{
    public static final String DIRECTORY = "data/";
    private static ArrayList<String> cardsNames;
    public static ArrayList<String> getCardsNames(){
        return cardsNames;
    }
    private static int cardSize;
    public static int getCardSize(){
        return cardSize;
    }
    public CardView(Card card, String stringCard, Image cardImage, int id){
        this.card = card;
//        this.fileCard = fileCard;
        this.cardImage = cardImage;
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
    public static ArrayList<Integer> getCardsNumbers(){
        ArrayList<Integer> result = new ArrayList<>();
        cardsNames = new ArrayList<>();
        BufferedReader input;
        String classJar =
                CardView.class.getResource("/CardView.class").toString();
        if (classJar.startsWith("jar:")) {
            InputStream in;
            in = CardView.class.getResourceAsStream(DIRECTORY+"listOfCards.txt");
            input = new BufferedReader(new InputStreamReader(in));
        }else {
            try {
                input = new BufferedReader(new FileReader(DIRECTORY+"listOfCards.txt"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("AAA");
            }
        }
        String line;
        try {
            while ((line = input.readLine()) != null) {
                cardsNames.add(DIRECTORY+line);
                result.add(getCardNumber(line));
                System.err.println(line);

            }
            input.close();
        }catch (IOException e){
            e.printStackTrace();
        }

/*        CodeSource src = CardView.class.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL jar = src.getLocation();
            ZipInputStream zip = null;
            try {
                zip = new ZipInputStream(jar.openStream());
                while(true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null)
                        break;
                    String name = e.getName();

                    System.err.println(name);
                    if ((name.startsWith(DIRECTORY))&&(!name.equals(DIRECTORY))) {
                        result.add(getCardNumber(name));

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            if (result.size()>0){
                return result;
            }
        }


        File dir = new File(DIRECTORY);

        ArrayList<File> cardsFiles = new ArrayList<>(Arrays.asList(dir.listFiles()));

        for (int i = 0; i < cardsFiles.size(); i++){
            cardsNames.add(DIRECTORY+cardsFiles.get(i).getName());
            result.add(getCardNumber(i));
        }*/
        return result;
    }
    private static int getCardNumber(int index){
        Pattern numberPattern = Pattern.compile("[0-9]+");
        Matcher numberMatcher = numberPattern.matcher(cardsNames.get(index));
        numberMatcher.find();
        return Integer.parseInt(numberMatcher.group());
    }
    private static int getCardNumber(String str){
//        System.err.println(str);
        Pattern numberPattern = Pattern.compile("[0-9]+");
        Matcher numberMatcher = numberPattern.matcher(str);
        numberMatcher.find();
        return Integer.parseInt(numberMatcher.group());
    }
    public static Image initImage(String resourceName){
        URL url = CardView.class.getResource(resourceName);
        Image image = null;//returns BufferedImage
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//        image = rescaleImage(image, 10, 10);
        return image;
    }
    public static void resize(int haracteristicScale) {
        cardSize = haracteristicScale / 10;
    }
//    public CardView getCardView(Card card){
//      return
//}
}
