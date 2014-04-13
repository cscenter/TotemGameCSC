package utils;

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
 *
 * в этом классе хранятся все картинки
 */
public class Gallery {
    /**
     * имена карт
     */
    private ArrayList<String> cardsNames;
    /**
     * имена картинок, которые не карты
     */
    private ArrayList<String> otherNames;
    /**
     * хешмапа, в которой по имени картинки возвращается ссылка на изображение
     */
    private HashMap<String, Image> resources;

    /**
     * возвращает ссылку на изображение по имени картинки, используя хеш-мапу
     * @param name имя картинки
     * @return ссылка на изображение
     */
    public Image getImage(String name) {
        return resources.get(name);
    }

    /**
     * конструктор. инициализирует переменные, пытается загрузить все картинки
     */
    public Gallery() {
        resources = new HashMap<>(100);
        cardsNames = new ArrayList<>(100);
        otherNames = new ArrayList<>();
        ClassLoader cl = Gallery.class.getClassLoader();
        ArrayList<String> imgNames = getCardsNames();
        getOtherPicturesNames();
//        imgNames.addAll(getOtherPicturesNames());       /* почему так не получается???*/
        for (String imgName : imgNames) {
            Image image;
            URL url = cl.getResource(imgName);
            try {
                image = ImageIO.read(url);
            } catch (IOException e) {
                throw new RuntimeException("can't read images!");
            }
            resources.put(imgName, image);
        }
        for (String name : otherNames) {
            Image image;
            URL url = cl.getResource(name);
            try {
                image = ImageIO.read(url);
            } catch (IOException e) {
                throw new RuntimeException("can't read images!");
            }
            resources.put(name, image);

        }
    }

    /**
     * возвращает список имён картинок, которые не карты
     * @return список имён картинок, которые не карты
     */
    private ArrayList<String> getOtherPicturesNames() {
        BufferedReader input;
        String classJar =
                Gallery.class.getResource("/MainTotemGame.class").toString();
        if (classJar.startsWith("jar:")) {
            InputStream in;
            in = Gallery.class.getResourceAsStream(Configuration.getDirectory() + "listOfPic.txt");
            input = new BufferedReader(new InputStreamReader(in));
        } else {
            try {
                //System.out.print(Configuration.getDirectory());
                input = new BufferedReader(new FileReader(Configuration.getDirectory() + "listOfPic.txt"));
            } catch (FileNotFoundException e) {
                throw new RuntimeException("can't read names of images!");
            }
        }
        String line;
        try {
            while ((line = input.readLine()) != null) {
                String imgName = Configuration.getDirectory() + line;
                otherNames.add(imgName);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return otherNames;
    }

    /**
     * возвращает список имён карт
     * @return возвращает список имён карт
     */
    public ArrayList<String> getCardsNames() {
        if (cardsNames.isEmpty()) {
            BufferedReader input;
            String classJar =
                    Gallery.class.getResource("/MainTotemGame.class").toString();
            if (classJar.startsWith("jar:")) {
                InputStream in;
                in = Gallery.class.getResourceAsStream(Configuration.getDirectory() + "listOfCards.txt");
                input = new BufferedReader(new InputStreamReader(in));
            } else {
                try {
                    input = new BufferedReader(new FileReader(Configuration.getDirectory() + "listOfCards.txt"));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("can't read names of images");
                }
            }
            String line;
            try {
                while ((line = input.readLine()) != null) {
                    //                System.err.println(line);
                    cardsNames.add(Configuration.getDirectory() + line);
                    //              result.add(getCardNumber(line));

                }
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cardsNames;
    }

    /**
     * по имени карты возвращает её номер
     * @param str имя карты
     * @return номер карты
     */
    public static int getCardNumber(String str) {
//        System.err.println(str);
        Pattern numberPattern = Pattern.compile("[0-9]+");
        Matcher numberMatcher = numberPattern.matcher(str);
        numberMatcher.find();
        return Integer.parseInt(numberMatcher.group());
    }
}
