package net.akami.mistream.play.list;

import net.akami.mistream.gamedata.CarInfoProvider;
import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.play.TerminalSequence;
import net.akami.mistream.vector.Vector3f;
import rlbot.ControllerState;

public class PostDashLanding extends TerminalSequence {

    public PostDashLanding(QueueHandler botController) {
        super(0, botController);
    }

    @Override
    protected ControllerState loadController() {
        return new ControlsOutput()
                .withBoost()
                .withRoll(-0.2f)
                .withThrottle(1)
                .withYaw(-0.6f);
    }

    @Override
    public String name() {
        return "post dash landing";
    }

    @Override
    public boolean isStopped() {
        Vector3f botDir = botController.data(CarInfoProvider.class).getBotDirection();
        System.out.println(botDir.x);
        return Math.abs(botDir.z) < 0.01 && Math.abs(botDir.x - 0.5) < 0.15;
    }
}
