package net.akami.mistream.gamedata;

import rlbot.flat.GameTickPacket;

public interface DataProvider {

    void update(GameTickPacket packet);
}
