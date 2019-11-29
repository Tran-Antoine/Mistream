package net.akami.mistream.core;

import rlbot.Bot;
import rlbot.manager.BotManager;
import rlbot.pyinterop.SocketServer;

public class BotLauncher extends SocketServer {

    public BotLauncher(int port, BotManager botManager) {
        super(port, botManager);
    }

    @Override
    protected Bot initBot(int index, String botType, int team) {
        System.out.println("Bot instance created");
        return new InputProcessor(index, new BotController());
    }
}
