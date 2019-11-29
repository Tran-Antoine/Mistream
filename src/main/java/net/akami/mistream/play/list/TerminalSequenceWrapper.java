package net.akami.mistream.play.list;

import net.akami.mistream.core.BotController;
import net.akami.mistream.play.TerminalOutputSequence;
import rlbot.ControllerState;

public class TerminalSequenceWrapper extends TerminalOutputSequence {

    private ControllerState controller;

    public TerminalSequenceWrapper(ControllerState controller, int frameExecutions, BotController botController) {
        super(frameExecutions, botController);
        this.controller = controller;
    }

    @Override
    protected ControllerState loadController() {
        return controller;
    }
}
