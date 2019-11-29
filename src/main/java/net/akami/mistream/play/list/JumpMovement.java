package net.akami.mistream.play.list;

import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.core.BotController;
import net.akami.mistream.play.TerminalOutputSequence;
import rlbot.ControllerState;

public class JumpMovement extends TerminalOutputSequence {

    private float roll;
    private float yaw;
    private float speed;

    public JumpMovement(int frameExecutions, float roll, float yaw, float speed, BotController botController) {
        super(frameExecutions, botController);
        this.roll = roll;
        this.speed = speed;
        this.yaw = yaw;
    }

    @Override
    protected ControllerState loadController() {
        return new ControlsOutput()
                .withRoll(roll)
                .withYaw(yaw)
                .withThrottle(speed == -1 ? 1 : speed)
                .withBoost(speed == -1)
                .withJump();
    }
}
