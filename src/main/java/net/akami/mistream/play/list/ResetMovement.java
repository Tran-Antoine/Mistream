package net.akami.mistream.play.list;

import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.core.BotController;
import net.akami.mistream.play.TerminalSequence;
import rlbot.ControllerState;

public class ResetMovement extends TerminalSequence {

    public ResetMovement(int frameExecutions, BotController botController) {
        super(frameExecutions, botController);
    }

    @Override
    protected ControllerState loadController() {
        return ControlsOutput.EMPTY;
    }
}
