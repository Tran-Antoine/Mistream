package net.akami.mistream.core;

import rlbot.Bot;
import rlbot.manager.BotManager;
import rlbot.pyinterop.SocketServer;

public class BotLauncher extends SocketServer {

    private MistreamDisplay display;

    public BotLauncher(int port, BotManager botManager, MistreamDisplay display) {
        super(port, botManager);
        this.display = display;
    }

    @Override
    protected Bot initBot(int index, String botType, int team) {
        System.out.println("Bot successfully initialized");
        return new InputProcessor(index, new MistreamQueueHandler(display));
    }
}
