package net.akami.mistream.play.list;

import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.play.TerminalSequence;
import rlbot.ControllerState;

public class BasicMovement extends TerminalSequence {

    private float speed;
    private float steer;
    private float yaw;

    public BasicMovement(int time, float speed, float steer) {
        this(time, speed, 0, steer);
    }

    public BasicMovement(int time, float speed, float yaw, float steer) {
        super(time, null);
        this.speed = speed;
        this.steer = steer;
        this.yaw = yaw;
    }

    @Override
    protected ControllerState loadController() {
        return new ControlsOutput()
                .withSteer(steer)
                .withYaw(yaw)
                .withThrottle(speed == -1 ? 1 : speed)
                .withBoost(speed == -1);
    }

    @Override
    public String name() {
        return String.format("Basic movement with speed %s and steer %s", speed, steer);
    }
}
