import java.awt.*;
import java.util.ArrayList;

public class PlayerView{
    private static int scale;
    private CardView topCardView;
    private Player playerInfo;
    private int xCoordinate;
    private int yCoordinate;
    private double angle;
    private char openCardKey;
    private char catchTotemKey;
    public char getOpenCardKey(){
        return openCardKey;
    }
    public char getCatchTotemKey(){
        return catchTotemKey;
    }
    public static void setScale(int size){
            scale = size;

    }
    public void setTopCardView(ArrayList<CardView> cardsView){
        topCardView = cardsView.get(playerInfo.getTopOpenedCard().getCardNumber());
    }
    public void resize(int haracteristicScale){
        xCoordinate = (int)((haracteristicScale/3.5) * Math.sin(angle*Math.PI/180) + haracteristicScale / 2.2);
        yCoordinate = (int)((haracteristicScale/3.5) * Math.cos(angle*Math.PI/180) + haracteristicScale / 2.5);

    }
    public String getPlayerName(){
        return playerInfo.getName();
    }
    public PlayerView(char newOpenCardKey, char newCatchTotemKey, Player player, double a){
        openCardKey = newOpenCardKey;
        catchTotemKey = newCatchTotemKey;
        playerInfo = player;
        angle = a;
        xCoordinate = (int)((scale/3.5) * Math.sin(angle*Math.PI/180) + scale / 2.2);
        yCoordinate = (int)((scale/3.5) * Math.cos(angle*Math.PI/180) + scale / 2.5);
    }


    public boolean isIn(Point p){
        if ((p.getX()<xCoordinate+CardView.getCardSize())&&(p.getX()>xCoordinate-CardView.getCardSize())){
            if ((p.getY()<yCoordinate+CardView.getCardSize()+40)&&(p.getY()>yCoordinate-40)){
                return true;
            }
        }
        return false;
    }
    public void clear(Graphics g){
        g.clearRect(xCoordinate-(int)(CardView.getCardSize()*2.1), yCoordinate, (int)(3.1*CardView.getCardSize()), (int)(CardView.getCardSize()*2.5));
    }
    public void drawPlayer(Graphics g, MyPanel panel){
        clear(g);
        g.drawChars(playerInfo.getName().toCharArray(), 0, playerInfo.getName().length(), xCoordinate - CardView.getCardSize() / 3, yCoordinate - scale / 30);
        String openCardsNumber = String.valueOf(playerInfo.getOpenCardsCount());
        g.drawChars(openCardsNumber.toCharArray(), 0, openCardsNumber.length(),xCoordinate+scale/60, yCoordinate+CardView.getCardSize()+scale/30);
        if (playerInfo.getOpenCardsCount()!=0){
            Image image = topCardView.getCardImage();
            g.drawImage(image, xCoordinate, yCoordinate, CardView.getCardSize(), CardView.getCardSize(), panel);
        }
        String closeCardsNumber = String.valueOf(playerInfo.getCloseCardsCount());
        g.drawChars(closeCardsNumber.toCharArray(), 0, closeCardsNumber.length(),  xCoordinate-CardView.getCardSize()+scale/60, yCoordinate+CardView.getCardSize()+scale/30);
        if (playerInfo.getCloseCardsCount()!=0){
            g.fillRect(xCoordinate - (int)(1.1 * CardView.getCardSize()), yCoordinate, CardView.getCardSize(), CardView.getCardSize());
        }
        String catchKey=String.valueOf(openCardKey)+", "+String.valueOf(catchTotemKey);
        g.drawChars(catchKey.toCharArray(), 0, catchKey.length(), xCoordinate - (int)(CardView.getCardSize()*1.5), yCoordinate - scale/30);
    }
}
