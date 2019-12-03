package net.akami.mistream.play.list;

import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.play.TerminalSequence;
import rlbot.ControllerState;

public class ForwardMovement extends TerminalSequence {

    private float speed;

    public ForwardMovement(float time, float speed) {
        super(time, null);
        this.speed = speed;
    }

    @Override
    protected ControllerState loadController() {
        return new ControlsOutput()
                .withSteer(0)
                .withThrottle(speed == -1 ? 1 : speed)
                .withBoost(speed == -1);
    }

    @Override
    public String name() {
        return "forward movement";
    }
}
