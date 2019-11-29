package net.akami.mistream.gamedata;

import rlbot.flat.GameTickPacket;

public class GameState implements DataProvider {

    private GameTickPacket currentPacket;
    // TODO : actually update these values
    private int timeLeft;
    // -1 means one goal down, +1 means one goal up
    private int scoreDifference;

    @Override
    public void update(GameTickPacket packet) {
        this.currentPacket = packet;
    }

    public GameTickPacket getCurrentPacket() {
        return currentPacket;
    }

    public int getScoreDifference() {
        return scoreDifference;
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
