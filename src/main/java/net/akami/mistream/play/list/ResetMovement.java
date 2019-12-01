package net.akami.mistream.play.list;

import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.play.TerminalSequence;
import rlbot.ControllerState;

public class ResetMovement extends TerminalSequence {

    public ResetMovement(int time, QueueHandler botController) {
        super(time, botController);
    }

    @Override
    protected ControllerState loadController() {
        return ControlsOutput.EMPTY;
    }

    @Override
    public String name() {
        return "empty movement";
    }
}
