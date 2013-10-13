import java.io.*;
import java.util.*;
class View{
    public static void main(String[] args){
        startView();
    }


	private static class PlayerView{
		public char openCardKey;
		public char catchTotemKey;
        public String playerViewName;
        public PlayerView(){

        }
        public PlayerView(char newOpenCardKey, char newCatchTotemKey, String name){
            openCardKey = newOpenCardKey;
            catchTotemKey = newCatchTotemKey;
            playerViewName = name;
        }
        public PlayerView(PlayerView pw){
            openCardKey = pw.openCardKey;
            catchTotemKey = pw.catchTotemKey;
        }
	}
	static ArrayList <PlayerView> playersView;
    Game myGame;
/**
 * ввести число игроков, а так же кнопки управления
 * по умолчанию:
 * 4 игрока, клавиши
 * q, w
 * e, r
 * t, y
 * u, i
*/
	public static int startView(){
        boolean flag = true;
        int numberOfPeople = 4;
        do{
            System.out.println("Use the default settings? (Y/N)");
            Scanner scan = new Scanner(System.in);
            String inputString; /* Считывается строка и воспринимается первый символ как ответ*/
            Character inputChar;
            char inputOpenCardKey;
            char inputCatchTotemKey;
            playersView = new ArrayList<PlayerView>();
            inputString = scan.nextLine();
            inputChar = inputString.charAt(0);
            switch (inputChar.toString().toUpperCase().charAt(0)){
                case 'Y':
                    numberOfPeople = 4;
                    flag = false;
                    inputOpenCardKey = 'q';
                    inputCatchTotemKey = 'w';
                    playersView.ensureCapacity(numberOfPeople);
                    playersView.add(new PlayerView(inputOpenCardKey, inputCatchTotemKey, "Vasya"));

                    inputOpenCardKey = 'e';
                    inputCatchTotemKey = 'r';
                    playersView.add(new PlayerView(inputOpenCardKey, inputCatchTotemKey, "Petya"));

                    inputOpenCardKey = 't';
                    inputCatchTotemKey = 'y';
                    playersView.add(new PlayerView(inputOpenCardKey, inputCatchTotemKey, "Gosha"));

                    inputOpenCardKey = 'u';
                    inputCatchTotemKey = 'i';
                    playersView.add(new PlayerView(inputOpenCardKey, inputCatchTotemKey,"Manya"));
                    break;
                case 'N':
                    flag = false;
                    do {
                        System.out.println("Please, insert number of players");
                        numberOfPeople = scan.nextInt();
                        System.out.printf("\n");
                        inputString = scan.nextLine();
                        if (numberOfPeople <= 1){
                            System.out.printf("%d isn't correct number of players! try again\n", numberOfPeople);
                        }else{
                            break;
                        }
                    } while(true);
                    playersView.ensureCapacity(numberOfPeople);
                    for (int i = 0; i < numberOfPeople; i++){
                        System.out.printf("player %d: insert your name\n", i+1);
                        String inputS = scan.nextLine();
                        label:
                        do {
                            System.out.printf("%s: insert key to open first card\n", inputS);
                            inputString = scan.nextLine();
                            inputOpenCardKey = inputString.charAt(0);
                            Character tmp = inputOpenCardKey;
                            inputOpenCardKey = tmp.toString().toLowerCase().charAt(0);
                            for (PlayerView p : playersView){
                                if ((inputOpenCardKey == p.catchTotemKey) || (inputOpenCardKey == p.openCardKey)){
                                    System.out.printf("player %s already use this key. Try another one\n", p.playerViewName);
                                    continue label;
                                }
                            }
                            break label;
                        }while(true);
                        label:
                        do{
                            System.out.printf("%s: insert key to catch totem\n", inputS);
                            inputString = scan.nextLine();
                            inputCatchTotemKey = inputString.charAt(0);
                            Character tmp = inputCatchTotemKey;
                            inputCatchTotemKey = tmp.toString().toLowerCase().charAt(0);
                            if (inputCatchTotemKey == inputOpenCardKey){
                                System.out.println("you already use this key for key that open last card! try another key");
                                continue label;
                            }
                            for (PlayerView p : playersView){
                                if ((inputCatchTotemKey == p.catchTotemKey) || (inputCatchTotemKey == p.openCardKey)){
                                    System.out.printf("player %s already use this key. Try another one\n", p.playerViewName);
                                    continue label;
                                }
                            }
                            break label;
                        }while (true);
                        playersView.add(new PlayerView(inputOpenCardKey, inputCatchTotemKey, inputS));
                    }
                    break;
                default:
                        System.out.println("What are you doing?? Try once more!");
                }
        } while ((flag));
        System.out.println("So, we have:");
        System.out.printf("Number of people: %d\n", numberOfPeople);
        for (PlayerView p : playersView){
            System.out.printf("Player %s has %c as key to open last card and %c as key to catch totem\n",
                    p.playerViewName, p.openCardKey, p.catchTotemKey);
        }
		return numberOfPeople;
	}
	
    public View(int numberOfPlayers)
    {
        
    }
    void run(){
/*		while (!(Game.isGameEnded())true){ //сюда, получается, надо ссылку на экземляр Game?
		String readL;
		char oc;
			do{
				System.out.printf("Round: %d\n", turnNumber);
				for (Player p : players){
					System.out.printf("player %s, %d open cards,  %d closed card \n the last card: %d\n\n",
						p.getName(), p.getOpenCardsCount(), p.getCloseCardsCount(), 
						p.getTopOpenedCard().getCardNumber());
				}
				System.out.printf("play!\n");
				readL=sc.nextLine();
				oc=readL.charAt(0);
				for (int i=0; i<players.length; i++){
					if (playersView.elementAt(i).openCardKey==oc){
						makeMove(i, false);
					}
					if (playersView.elementAt(i).catchTotemKey==oc){
						makeMove(i, true);
					}
				}
			}while(!(isRoundEnded()));
		}                           */
	}
}