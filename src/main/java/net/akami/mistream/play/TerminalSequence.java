package net.akami.mistream.play;

import net.akami.mistream.gamedata.DataHandler;
import rlbot.ControllerState;

import java.util.LinkedList;

public abstract class TerminalSequence implements OutputSequence {

    private long endTime;
    private int time;
    private ControllerState controllerState;
    protected QueueHandler botController;

    public TerminalSequence(int time, QueueHandler botController) {
        this.time = time;
        this.botController = botController;
    }

    protected abstract ControllerState loadController();

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue, DataHandler gameData) {
        if(endTime == 0) {
            endTime = System.currentTimeMillis() + time;
        }
        if(controllerState == null) {
            controllerState = loadController();
        }
        return controllerState;
    }

    @Override
    public boolean isStopped() {
        return System.currentTimeMillis() > endTime;
    }

}
