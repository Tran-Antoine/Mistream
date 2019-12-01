package net.akami.mistream.core;

import rlbot.manager.BotManager;
import rlbot.pyinterop.SocketServer;

public class MistreamMain {

    static final int DEFAULT_PORT = 17357;

    public static void main(String[] args) {
        BotManager botManager = new BotManager();
        MistreamDisplay display = new MistreamDisplay();
        SocketServer pythonInterface = new BotLauncher(DEFAULT_PORT, botManager, display);
        new Thread(pythonInterface::start).start();
        display.init();
    }
}
