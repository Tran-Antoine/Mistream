package net.akami.mistream.gamedata;

import rlbot.flat.GameTickPacket;

public class GameState implements DataProvider {

    private GameTickPacket currentPacket;
    private float time;
    // -1 means one goal down, +1 means one goal up
    private int scoreDifference;

    @Override
    public void update(GameTickPacket packet) {
        this.currentPacket = packet;
        this.time = packet.gameInfo().secondsElapsed();
    }

    public GameTickPacket getCurrentPacket() {
        return currentPacket;
    }

    public int getScoreDifference() {
        return scoreDifference;
    }

    public float getTime() {
        return time;
    }
}
