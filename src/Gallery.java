import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lavton on 02.03.14.
 */
public class Gallery {
    private ArrayList<String> cardsNames;
    private HashMap<String, Image> resources;

    public Image getImage(String name){
        return  resources.get(name);
    }
    public Gallery(){
        resources = new HashMap<>(100);
        cardsNames = new ArrayList<>(100);
        ClassLoader cl = Gallery.class.getClassLoader();
        ArrayList<String> imgNames = getCardsNames();
        for (String imgName : imgNames){
            Image image;
            URL url = cl.getResource(imgName);
            try {
                image = ImageIO.read(url);
            } catch (IOException e) {
                throw new RuntimeException("can't read images!");
            }
            resources.put(imgName, image);
        }
    }

    public ArrayList<String> getCardsNames(){
        if (cardsNames.isEmpty()){
            BufferedReader input;
            String classJar =
                    Gallery.class.getResource("/Gallery.class").toString();
            if (classJar.startsWith("jar:")) {
                InputStream in;
                in = Gallery.class.getResourceAsStream(Configuration.getDirectory()+"listOfCards.txt");
                input = new BufferedReader(new InputStreamReader(in));
            }else {
                try {
                    input = new BufferedReader(new FileReader(Configuration.getDirectory()+"listOfCards.txt"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("can't read names of images");
                }
            }
            String line;
            try {
                while ((line = input.readLine()) != null) {
    //                System.err.println(line);
                    cardsNames.add(Configuration.getDirectory()+line);
      //              result.add(getCardNumber(line));

                }
                input.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return cardsNames;
    }

    private static int getCardNumber(String str){
//        System.err.println(str);
        Pattern numberPattern = Pattern.compile("[0-9]+");
        Matcher numberMatcher = numberPattern.matcher(str);
        numberMatcher.find();
        return Integer.parseInt(numberMatcher.group());
    }
}
