/**
 * Created with IntelliJ IDEA.
 * User: lavton
 * Date: 08.12.13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public interface TotemGameController {
    public Game.ResultOfMakeMove makeMoving(int n, Game.WhatPlayerDid whatPlayerDid);
    public boolean isModified();
}
