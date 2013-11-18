/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Internet
 */
public class GameTest {
    
    public GameTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of generateAllCards method, of class Game.
     */
    @Test
    
    public void test1() {
        System.out.println("test1");
        ArrayList<String> pl = new ArrayList<String>();
        pl.add("1");
        pl.add("2");
       
         LinkedList allCards = new LinkedList<Card>();  
        
         for (int i = 0; i < 80; i++){
            allCards.add(new Card(11));
        }
         Game instance = new Game(pl, allCards);
         instance.testGenerateAllCards(allCards);
         instance.generatePlayers(pl);
         instance.generateCardsUnderTotem(pl.size());
         instance.makeMove(0, Game.WhatPlayerDid.OPEN_NEW_CARD);
         instance.makeMove(1, Game.WhatPlayerDid.OPEN_NEW_CARD);
         instance.makeMove(0, Game.WhatPlayerDid.TOOK_TOTEM);
         ArrayList<Player> players = new ArrayList<Player>();
         players = instance.getPlayers();
         assertTrue("it is not correct" , players.get(0).getCardsCount() < players.get(1).getCardsCount());
 
    }
    
    @Test
     public void test2() {
        System.out.println("test2");
        ArrayList<String> pl = new ArrayList<String>();
        pl.add("1");
        pl.add("2");
         LinkedList allCards = new LinkedList<Card>();
         for (int i = 0; i < 80; i++){
            allCards.add(new Card(1 + i*2));
        }
         Game instance = new Game(pl, allCards);
            //instance.generateAllCards();
         instance.testGenerateAllCards(allCards);
         instance.generatePlayers(pl);
         instance.generateCardsUnderTotem(pl.size());
         instance.makeMove(0, Game.WhatPlayerDid.OPEN_NEW_CARD);
         instance.makeMove(1, Game.WhatPlayerDid.OPEN_NEW_CARD);
         instance.makeMove(0, Game.WhatPlayerDid.TOOK_TOTEM);
       
         ArrayList<Player> players = new ArrayList<Player>();
         players = instance.getPlayers();
         assertTrue("it is not correct" , players.get(0).getCardsCount() > players.get(1).getCardsCount());
    }
    @Test
      public void test3() {
        System.out.println("test3");
        ArrayList<String> playersN = new ArrayList<String>();
        playersN.add("1");
        playersN.add("2");
        playersN.add("3");
         LinkedList allCards = new LinkedList<Card>();
         for (int i = 0; i < 80; i++){
            allCards.add(new Card(1 + i*10));
        }
         Game instance = new Game(playersN, allCards);
         for (int i = 0; i < 1; i++){
            instance.makeMove(0, Game.WhatPlayerDid.OPEN_NEW_CARD);
            instance.makeMove(1, Game.WhatPlayerDid.OPEN_NEW_CARD);
            instance.makeMove(2, Game.WhatPlayerDid.OPEN_NEW_CARD);
         }
         instance.makeMove(0, Game.WhatPlayerDid.TOOK_TOTEM);
       
         ArrayList<Player> players = new ArrayList<Player>();
         players = instance.getPlayers();
      //   System.out.println(players.get(0).getCloseCardsCount());
      //   System.out.println(players.get(1).getCloseCardsCount());
         assertTrue("it is not correct" , players.get(0).getCloseCardsCount() > players.get(1).getCloseCardsCount());
    }

    @Test
      public void test4() {
        System.out.println("test4");
        ArrayList<String> playersN = new ArrayList<String>();
        playersN.add("1");
        playersN.add("2");
        playersN.add("3");
        playersN.add("4");
         LinkedList allCards = new LinkedList<Card>();
         for (int i = 0; i < 80; i++){
            allCards.add(new Card(1 + 1*10));
        }
         Game instance = new Game(playersN, allCards);
            instance.makeMove(0, Game.WhatPlayerDid.OPEN_NEW_CARD);
            instance.makeMove(1, Game.WhatPlayerDid.OPEN_NEW_CARD);

         
         instance.makeMove(1, Game.WhatPlayerDid.TOOK_TOTEM);
       
         ArrayList<Player> players = new ArrayList<Player>();
         players = instance.getPlayers();
         assertTrue("it is not correct" , players.get(0).getCloseCardsCount() > players.get(1).getCloseCardsCount());
         //assertTrue("it is not correct" , players.get(2).getCloseCardsCount() == players.get(1).getCloseCardsCount());
    }
    
    @Test
      public void test5() {
        System.out.println("test5");
        ArrayList<String> playersN = new ArrayList<String>();
        playersN.add("1");
        playersN.add("2");
        //playersN.add("3");
        //playersN.add("4");
         LinkedList allCards = new LinkedList<Card>();
         for (int i = 0; i < 80; i++){
            allCards.add(new Card(1 + (i%2)*10));
        }
         Game instance = new Game(playersN, allCards);
            instance.makeMove(0, Game.WhatPlayerDid.OPEN_NEW_CARD);
            instance.makeMove(1, Game.WhatPlayerDid.OPEN_NEW_CARD);
            instance.makeMove(0, Game.WhatPlayerDid.OPEN_NEW_CARD);
            instance.makeMove(1, Game.WhatPlayerDid.OPEN_NEW_CARD);

         
         instance.makeMove(0, Game.WhatPlayerDid.TOOK_TOTEM);
       
         ArrayList<Player> players = new ArrayList<Player>();
         players = instance.getPlayers();
        // System.out.println(players.get(0).getCloseCardsCount());
         assertTrue("it is not correct" , players.get(0).getCloseCardsCount() < players.get(1).getCloseCardsCount());
         //assertTrue("it is not correct" , players.get(2).getCloseCardsCount() == players.get(1).getCloseCardsCount());
    }

    /**
     * Test of generatePlayers method, of class Game.
     */

   @Test
    public void test6() {
        System.out.println("test6");
        ArrayList<String> pl = new ArrayList<String>();
        pl.add("1");
        pl.add("2");
        Game instance = new Game(pl);
        instance.generateCardsUnderTotem(2);
        int count = 0;
        ArrayList<Player> players = new ArrayList<Player>();
        players = instance.getPlayers();
        for(int i = 0; i < 2; i++){
            count += players.get(i).getCardsCount();
        }
        count += instance.getCardsUnderTotemCount();
        
        assertTrue("it is not correct" , count == 80);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    @Test
      public void test7() {
        System.out.println("test7");
        ArrayList<String> playersN = new ArrayList<String>();
        playersN.add("1");
        playersN.add("2");
         LinkedList allCards = new LinkedList<Card>();
         for (int i = 0; i < 80; i++){
            allCards.add(new Card(1 + i*10));
        }
         int rand = (int)(Math.random()*7)%20 + 1;
         Game instance = new Game(playersN, allCards);
         for(int i = 0; i < rand; i++){
            instance.makeMove(0, Game.WhatPlayerDid.OPEN_NEW_CARD);
            instance.makeMove(1, Game.WhatPlayerDid.OPEN_NEW_CARD);
         }
         instance.makeMove(rand%2, Game.WhatPlayerDid.TOOK_TOTEM);
         ArrayList<Player> players = new ArrayList<Player>();
         players = instance.getPlayers();
         assertTrue("it is not correct" , players.get(rand%2).getCloseCardsCount() > players.get((rand+1)%2).getCloseCardsCount());
         
    }
    @Test
      public void test8() {
        System.out.println("test8");
        ArrayList<String> playersN = new ArrayList<String>();
        int n = (int)(Math.random()*7+1)%10;
        String a = "0";
        for (int i = 0; i < n; i++){
            a += 1;
            playersN.add(a);
        }
         LinkedList allCards = new LinkedList<Card>();
         for (int i = 0; i < 80; i++){
            allCards.add(new Card(1 + i*10));
        }
         int rand = (int)(Math.random()*7)%10 + 1;
         Game instance = new Game(playersN, allCards);
         for(int i = 0; i < rand; i++){
            for(int j = 0; j < n; j++)
                instance.makeMove(j, Game.WhatPlayerDid.OPEN_NEW_CARD);
         }
         instance.makeMove(rand%n, Game.WhatPlayerDid.TOOK_TOTEM);
         ArrayList<Player> players = new ArrayList<Player>();
         players = instance.getPlayers();
         assertTrue("it is not correct" , players.get(rand%n).getCloseCardsCount() > players.get((rand+1)%n).getCloseCardsCount());
         
    }
    
    @Test
      public void test9() {
        System.out.println("test9");
        ArrayList<String> playersN = new ArrayList<String>();
        playersN.add("1");
        playersN.add("2");
         LinkedList allCards = new LinkedList<Card>();
         for (int i = 0; i < 80; i++){
            allCards.add(new Card(1 + i*10));
        }
         Game instance = new Game(playersN, allCards);
         instance.makeMove(0, Game.WhatPlayerDid.TOOK_TOTEM);
         ArrayList<Player> players = new ArrayList<Player>();
         players = instance.getPlayers();
         assertTrue("it is not correct" , players.get(0).getCloseCardsCount() == players.get(1).getCloseCardsCount());
         
    }
    @Test
      public void test10() {
        System.out.println("test10");
        ArrayList<String> playersN = new ArrayList<String>();
        int n = 80;
        for (int i = 0; i < n; i++)
            playersN.add("1");
        
         LinkedList allCards = new LinkedList<Card>();
         for (int i = 0; i < 80; i++){
            allCards.add(new Card(i + 1*10));
        }
         Game instance = new Game(playersN, allCards);
         for(int i = 0; i < n; i++)
             instance.makeMove(i, Game.WhatPlayerDid.OPEN_NEW_CARD);
         
         instance.makeMove(0, Game.WhatPlayerDid.TOOK_TOTEM);
         ArrayList<Player> players = new ArrayList<Player>();
         players = instance.getPlayers();
         //System.out.println(players.get(0).getCloseCardsCount());
         assertTrue("it is not correct" , players.get(0).getCloseCardsCount() == 0);
         
    }
    
}
   