package net.akami.mistream.gamedata;

import rlbot.flat.GameTickPacket;

@FunctionalInterface
public interface DataProvider {
    void update(GameTickPacket packet);
}
