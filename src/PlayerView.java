import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

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
  //      System.err.println("we are in setTopCV!!!");
//        System.err.println(playerInfo.getTopOpenedCard().getCardNumber());
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
        angle = a;
        xCoordinate = (int)((scale/3.5) * Math.sin(angle*Math.PI/180) + scale / 2.2);
        yCoordinate = (int)((scale/3.5) * Math.cos(angle*Math.PI/180) + scale / 2.5);
       // player.setCoordinate(xCoordinate, yCoordinate);
        playerInfo = player;
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
    public void drawPlayer(Graphics g, MyPanel panel) throws IOException{
        clear(g);
        g.drawImage(ImageIO.read(new File("data/tboy.jpg")), xCoordinate-110, yCoordinate, panel);
        g.drawChars(playerInfo.getName().toCharArray(), 0, playerInfo.getName().length(), xCoordinate - CardView.getCardSize() / 3, yCoordinate - scale / 30);
        String openCardsNumber = String.valueOf(playerInfo.getOpenCardsCount());
        g.drawChars(openCardsNumber.toCharArray(), 0, openCardsNumber.length(),xCoordinate+scale/60 + 30, yCoordinate+CardView.getCardSize()+scale/30);
        if (playerInfo.getOpenCardsCount()!=0){
            Image image = topCardView.getCardImage();
            g.drawImage(image, xCoordinate, yCoordinate, CardView.getCardSize(), CardView.getCardSize(), panel);
        }
        else
            if (playerInfo.getCloseCardsCount() != 0 )
            g.drawImage(ImageIO.read(new File("data/tback.jpg")), xCoordinate, yCoordinate, 70, 70, panel);
        String closeCardsNumber = String.valueOf(playerInfo.getCloseCardsCount());
        g.drawChars(closeCardsNumber.toCharArray(), 0, closeCardsNumber.length(),  xCoordinate + scale/60, yCoordinate+CardView.getCardSize()+scale/30);
        
        playerInfo.setCoordinate(xCoordinate, yCoordinate);
        
        String catchKey=String.valueOf(openCardKey)+", "+String.valueOf(catchTotemKey);
        g.drawChars(catchKey.toCharArray(), 0, catchKey.length(), xCoordinate - (int)(CardView.getCardSize()*1.5), yCoordinate - scale/30);
    }
}
