public class BasicController implements TotemGameController {

    Game myGame;
    ServerView myView;
    BasicController(Game game, ServerView view){
        myGame = game;
        myView = view;
    }
    boolean modified;
    synchronized private void setModified(boolean newC){
        modified = newC;
    }
    synchronized public boolean isModified(){
        boolean wasModified = modified;
        modified = false;
        return wasModified;
    }

    public Game.ResultOfMakeMove makeMoving(int n, Game.WhatPlayerDid whatPlayerDid){
        Game.ResultOfMakeMove resultOfMakeMove = myGame.makeMove(n, whatPlayerDid);
        setModified(true);
        return resultOfMakeMove;

    }
}
