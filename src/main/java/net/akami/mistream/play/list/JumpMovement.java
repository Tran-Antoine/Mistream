package net.akami.mistream.play.list;

import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.play.TerminalSequence;
import rlbot.ControllerState;

public class JumpMovement extends TerminalSequence {

    private float roll;
    private float yaw;
    private float pitch;
    private float speed;

    public JumpMovement(float time, float roll, float yaw, float speed, QueueHandler botController) {
        this(time, roll, yaw, 0, speed, botController);
    }

    public JumpMovement(float time, float roll, float yaw, float pitch, float speed, QueueHandler botController) {
        super(time, botController);
        this.roll = roll;
        this.speed = speed;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    protected ControllerState loadController() {
        return new ControlsOutput()
                .withRoll(roll)
                .withYaw(yaw)
                .withPitch(pitch)
                .withThrottle(speed == -1 ? 1 : speed)
                .withBoost(speed == -1)
                .withJump();
    }

    @Override
    public String name() {
        return "jump";
    }
}
