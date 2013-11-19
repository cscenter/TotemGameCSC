import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Класс, отвечающий за работу с игроками
 * В графическом режиме
 */
class GraphicsView extends JFrame{
    private Game myGame;
    private static int FRAME_SIZE;
    private MyPanel myPanel;
    
    /**
     * Поля, которые нужно знать View о игроках
     */
    /**
     * ввести число игроков, а так же кнопки управления
     * по умолчанию:
     * 4 игрока, клавиши
     * q, w
     * e, r
     * t, y
     * u, i
     */
    private char getNewChar(String inputMessage, String errorOutputMessage){
        String inputString;
        char inputChar;
        Scanner scan = new Scanner(System.in);
        do{
            try{
                System.out.println(inputMessage);
                inputString = scan.nextLine();
                inputChar = inputString.charAt(0);
            } catch (StringIndexOutOfBoundsException e){
                System.out.printf("%s\n", errorOutputMessage);
                continue;
            }
            break;
        }while(true);
        return inputChar;
    }
    private void defaultSettings(ArrayList <String> rezultStrings){
        char inputOpenCardKey;
        char inputCatchTotemKey;
        int numberOfPeople = 4;
//        inputOpenCardKey = 'q';
//        inputCatchTotemKey = 'w';
//        myPanel.playersView.ensureCapacity(numberOfPeople);
    //    myPanel.playersView.add(myPanel.initPlayerView(inputOpenCardKey, inputCatchTotemKey, "Vasya", 0.0));
        rezultStrings.add("Vasya");

//        inputOpenCardKey = 'e';
//        inputCatchTotemKey = 'r';
  //      myPanel.playersView.add(myPanel.initPlayerView(inputOpenCardKey, inputCatchTotemKey, "Petya", 90.0));
        rezultStrings.add("Petya");

//        inputOpenCardKey = 't';
  //      inputCatchTotemKey = 'y';
    //    myPanel.playersView.add(myPanel.initPlayerView(inputOpenCardKey, inputCatchTotemKey, "Gosha", 180.0));
        rezultStrings.add("Gosha");

//        inputOpenCardKey = 'u';
  //      inputCatchTotemKey = 'i';
    //    myPanel.playersView.add(myPanel.initPlayerView(inputOpenCardKey, inputCatchTotemKey, "Manya", 270.0));
        rezultStrings.add("Manya");
//        myPanel.repaint();
    }
    private int setNumberOfPlayers(){
        int numberOfPeople;
        Scanner scan = new Scanner(System.in);
        do {
            try{
                System.out.println("Please, insert number of players");
                numberOfPeople = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException | StringIndexOutOfBoundsException e){
                System.out.println("Integer must be integer, you, clever user! try again\n");
                scan.nextLine();
                continue;
            }
            System.out.printf("\n");
            if (numberOfPeople <= 1){
                System.out.printf("%d isn't correct number of players! try again\n", numberOfPeople);
            }else{
                break;
            }
        } while(true);
        return numberOfPeople;
    }
    private void notDefaultSettings(ArrayList<String> rezultStrings){
        int numberOfPeople = setNumberOfPlayers();
        Scanner scan = new Scanner(System.in);
        String inputString;
        char inputCatchTotemKey;
        char inputOpenCardKey;
//        myPanel.playersView.ensureCapacity(numberOfPeople);
        for (int i = 0; i < numberOfPeople; i++){
            String inputS;
            do {
                System.out.printf("player %d: insert your name\n", i+1);
                inputS = scan.nextLine();
                if (inputS.length()!=0){
                    break;
                }else{
                    System.out.println("Name can't be empty string! try again");
                }
            }while(true);
            rezultStrings.add(inputS);
            label:
            do {
                try{
                    System.out.printf("%s: insert key to open first card\n", inputS);
                    inputString = scan.nextLine();
                    inputOpenCardKey = inputString.charAt(0);
                    Character tmp = inputOpenCardKey;
                    inputOpenCardKey = tmp.toString().toLowerCase().charAt(0);
  /*                  for (PlayerView p : myPanel.playersView){
                        if ((inputOpenCardKey == p.getCatchTotemKey()) || (inputOpenCardKey == p.getOpenCardKey())){
                            System.out.printf("player %s already use this key. Try another one\n", p.getPlayerName());
                            continue label;
                        }
                    }
*/                    break;
                }catch (StringIndexOutOfBoundsException e){
                    System.out.printf("%s, you know, what you just did?? You may drop the game!" +
                            " Thing about your behaviour and try once more!\n", inputS);
                }

            }while(true);
/*            label:
            do{
                try {
                    System.out.printf("%s: insert key to catch totem\n", inputS);
                    inputString = scan.nextLine();
                    inputCatchTotemKey = inputString.charAt(0);
                    Character tmp = inputCatchTotemKey;
                    tmp = myPanel.lastPressedKey;
                    inputCatchTotemKey = tmp.toString().toLowerCase().charAt(0);
                    if (inputCatchTotemKey == inputOpenCardKey){
                        System.out.println("you already use this key for key that open last card! try another key");
                        continue;
                    }
                    for (PlayerView p : myPanel.playersView){
                        if ((inputCatchTotemKey == p.catchTotemKey) || (inputCatchTotemKey == p.openCardKey)){
                            System.out.printf("player %s already use this key. Try another one\n", p.playerViewName);
                            continue label;
                        }
                    }
                    break;
                }catch (StringIndexOutOfBoundsException e){
                    System.out.printf("%s, you know, what you just did?? You may drop the game!" +
                            " Thing about your behaviour and try once more!\n", inputS);
                }

            }while (true);
            myPanel.playersView.add(myPanel.initPlayerView(inputOpenCardKey, inputCatchTotemKey, inputS,
                    (360.0 / numberOfPeople) * (myPanel.playersView.size())));*/
        }

    }
    private ArrayList<String> startView(){
        ArrayList <String> rezultStrings = new ArrayList<>();
        boolean flag = true;
        int numberOfPeople = 4;
        do{
            Character inputChar;
//            myPanel.playersView = new ArrayList<>();
            inputChar = getNewChar("Use the default settings? (Y/N)", "No-No-NO, %username% ! Char means char, not empty string!");
            switch (inputChar.toString().toUpperCase().charAt(0)){ /* использовать параметры по умолчанию?*/
                case 'Y':           /*да*/
                    flag = false;
                    defaultSettings(rezultStrings);
                    break;
                case 'N':               /*нет*/
                    flag = false;
                    notDefaultSettings(rezultStrings);
                    break;
                default:
                    System.out.println("What are you doing?? Try once more!");
            }
        } while ((flag));

        /*показываем то, что получаем в итоге*/
        System.out.println("So, we have:");
        System.out.printf("Number of people: %d\n", numberOfPeople);
//        for (PlayerView p : myPanel.playersView){
  //          System.out.printf("Player %s has '%c' as key to open last card and '%c' as key to catch totem\n",
    //                p.playerViewName, p.openCardKey, p.catchTotemKey);
      //  }
        return rezultStrings;
    }

    int chooseOneOfPlayers(ArrayList<Integer> playersIndex){
        int looser;
        Scanner scan = new Scanner(System.in);
        do {
            try{
                System.out.println("Please, choose a player from this list");
                System.out.printf("name: ");
                for (Integer i : playersIndex){
                    System.out.printf("%15s", myGame.getPlayer(i).getName());
                }
                System.out.printf("\n");
                System.out.printf("type: ");
                for (int i = 0; i < playersIndex.size(); i++){
                    System.out.printf("%15d", i);
                }
                System.out.printf("\n");
                looser = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException | StringIndexOutOfBoundsException e){
                System.out.println("Integer must be integer, you, clever user! try again\n");
                scan.nextLine();
                continue;
            }
            if ((looser < 0) || (looser >= playersIndex.size())){
                System.out.printf("%d isn't correct number of player! try again\n", looser);
            }else{
                break;
            }
        } while(true);
        return looser;

    }
    public GraphicsView(){
        FRAME_SIZE = (Toolkit.getDefaultToolkit().getScreenSize().getHeight() > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) ?
        (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() : (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
        myGame = new Game(startView(), CardView.getCardsNumbers());
        myPanel = new MyPanel();
        ArrayList<Character> kt = new ArrayList<>(4);
        kt.add('w');
        kt.add('r');
        kt.add('y');
        kt.add('i');
        ArrayList<Character> oc = new ArrayList<>(4);
        oc.add('q');
        oc.add('e');
        oc.add('t');
        oc.add('u');
        ArrayList<Double> an = new ArrayList<>(4);
        an.add(0.0);
        an.add(90.0);
        an.add(180.0);
        an.add(270.0);
        myPanel.initiation(myGame, kt, oc, an);
//        for (int i=0; i<myPanel.playersView.size(); i++){
  //          myPanel.playersView.get(i).connectWithInfo(myGame.getPlayer(i));
    //    }
//        myPanel.myGame = this.myGame;
      //  myPanel.initTotemV();
        add(myPanel);
        pack();
        addKeyListener(myPanel.initMyKeyListener());
        addMouseListener(myPanel.initMyMouseListener());
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}