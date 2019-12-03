package net.akami.mistream.play.list;

import net.akami.mistream.gamedata.CarInfoProvider;
import net.akami.mistream.gamedata.GameState;
import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.play.TerminalSequence;
import net.akami.mistream.vector.Vector3f;
import rlbot.ControllerState;

public class PostDashLanding extends TerminalSequence {

    private boolean isOnLeft;

    public PostDashLanding(QueueHandler botController, boolean isOnLeft) {
        super(0, botController);
        this.isOnLeft = isOnLeft;
    }

    @Override
    protected ControllerState loadController() {
        float factor = isOnLeft ? -1 : 1;
        return new ControlsOutput()
                .withBoost()
                .withRoll(0.2f * factor)
                .withThrottle(1)
                .withYaw(0.5f * factor);
    }

    @Override
    public String name() {
        return "post dash landing";
    }

    @Override
    public boolean isStopped() {
        CarInfoProvider info = botController.data(CarInfoProvider.class);
        GameState state = botController.data(GameState.class);

        Vector3f carLoc = info.getBotLocation();
        if(carLoc == null) {
            return false;
        }

        Vector3f ballLoc = new Vector3f(state.getCurrentPacket().ball().physics().location());
        ballLoc = new Vector3f(ballLoc.x, ballLoc.y, ballLoc.z - 75);
        Vector3f carDir = info.getBotDirection();

        Vector3f idealDir = ballLoc.minus(carLoc);
        boolean lookAt = Math.abs(carDir.angle(idealDir)) < 0.2;
        return lookAt;
    }
}
