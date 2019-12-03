package net.akami.mistream.play;

import net.akami.mistream.gamedata.DataHandler;
import net.akami.mistream.gamedata.GameState;
import rlbot.ControllerState;

import java.util.LinkedList;

public abstract class TerminalSequence implements OutputSequence {

    private float endTime;
    private float currentTime;
    private final float seconds;
    private ControllerState controllerState;
    protected QueueHandler botController;

    public TerminalSequence(float seconds, QueueHandler botController) {
        System.out.println("Initialized " + name() + " with duration of " + seconds);
        this.seconds = seconds;
        this.botController = botController;
    }

    protected abstract ControllerState loadController();

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue, DataHandler gameData) {

        float currentGameTime = gameData.data(GameState.class).getTime();
        if(endTime == 0) {
            endTime = currentGameTime + seconds;
        }
        this.currentTime = currentGameTime;

        if(controllerState == null) {
            controllerState = loadController();
        }
        return controllerState;
    }

    @Override
    public boolean isStopped() {
        //System.out.println(currentTime + " vs " + endTime + " ("+seconds+")s");
        return currentTime > endTime;
    }

}
