package net.akami.mistream.play.list;

import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.play.OutputSequence;
import net.akami.mistream.vector.Vector3f;

import java.util.Arrays;
import java.util.List;

public class DiagonalKickoff extends KickoffSequence {

    public DiagonalKickoff(QueueHandler botController) {
        super(new Vector3f(2048, 2560, 17.01), botController);
    }

    // we invert the boolean "isOnLeft" because if the car is on the left, we want it to go on the right to get centered
    @Override
    protected List<OutputSequence> loadChildren() {

        int factor = !isOnLeft() ? -1 : 1;
        return Arrays.asList(
                new BasicMovement(300, -1, 0),
                new BasicMovement(100, -1, factor),
                new SideDash(botController, isOnLeft(), -1),
                new PostDashLanding(botController),
                new BasicMovement(50, -1, 0),
                new JumpMovement(280, 0, 0, 1, botController),
                new ResetMovement(0, botController),
                new JumpMovement(50, factor/2.0f, 0, -0.3f,1, botController)
        );
    }

    @Override
    public String name() {
        return "Diagonal kickoff, Lionblaze form";
    }
}
