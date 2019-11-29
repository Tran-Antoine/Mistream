package net.akami.mistream.play;

import net.akami.mistream.core.BotController;
import net.akami.mistream.gamedata.DataHandler;
import rlbot.ControllerState;

import java.util.LinkedList;

public abstract class TerminalSequence implements OutputSequence {

    private int frameExecutions;
    private int currentFrameExecutions;
    private ControllerState controllerState;
    protected BotController botController;

    public TerminalSequence(int frameExecutions, BotController botController) {
        this.frameExecutions = frameExecutions;
        this.botController = botController;
    }

    protected abstract ControllerState loadController();

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue, DataHandler gameData) {
        this.currentFrameExecutions++;
        if(controllerState == null) {
            controllerState = loadController();
        }
        return controllerState;
    }

    @Override
    public boolean isStopped() {
        return currentFrameExecutions > frameExecutions;
    }

}
