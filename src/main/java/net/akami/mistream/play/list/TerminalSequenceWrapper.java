package net.akami.mistream.play.list;

import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.play.TerminalSequence;
import rlbot.ControllerState;

public class TerminalSequenceWrapper extends TerminalSequence {

    private ControllerState controller;

    public TerminalSequenceWrapper(ControllerState controller, int time, QueueHandler botController) {
        super(time, botController);
        this.controller = controller;
    }

    @Override
    protected ControllerState loadController() {
        return controller;
    }

    @Override
    public String name() {
        return "Basic sequence, with controller : " + controller;
    }
}
