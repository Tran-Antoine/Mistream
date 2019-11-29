package net.akami.mistream.play;

import net.akami.mistream.core.BotController;
import rlbot.ControllerState;

import java.util.LinkedList;

public abstract class TerminalOutputSequence implements OutputSequence {

    private int frameExecutions;
    private int currentFrameExecutions;
    private ControllerState controllerState;
    protected BotController botController;

    public TerminalOutputSequence(int frameExecutions, BotController botController) {
        this.frameExecutions = frameExecutions;
        this.botController = botController;
    }

    protected abstract ControllerState loadController();

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue) {
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
