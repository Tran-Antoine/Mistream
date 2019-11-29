package net.akami.mistream.play.list;

import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.play.TerminalOutputSequence;
import rlbot.ControllerState;

public class BasicMovement extends TerminalOutputSequence {

    private float speed;
    private float steer;
    private float yaw;

    public BasicMovement(int frameExecutions, float speed, float steer) {
        this(frameExecutions, speed, 0, steer);
    }

    public BasicMovement(int frameExecutions, float speed, float yaw, float steer) {
        super(frameExecutions, null);
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
}
