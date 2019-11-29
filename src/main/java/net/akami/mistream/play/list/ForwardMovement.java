package net.akami.mistream.play.list;

import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.play.TerminalSequence;
import rlbot.ControllerState;

public class ForwardMovement extends TerminalSequence {

    private float speed;

    public ForwardMovement(int frameExecutions, float speed) {
        super(frameExecutions, null);
        this.speed = speed;
    }

    @Override
    protected ControllerState loadController() {
        return new ControlsOutput()
                .withSteer(0)
                .withThrottle(speed == -1 ? 1 : speed)
                .withBoost(speed == -1);
    }
}
