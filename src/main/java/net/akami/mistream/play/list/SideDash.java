package net.akami.mistream.play.list;

import net.akami.mistream.core.BotController;
import net.akami.mistream.play.FragmentedOutputSequence;
import net.akami.mistream.play.OutputSequence;

import java.util.Arrays;
import java.util.List;

public class SideDash extends FragmentedOutputSequence {

    private boolean left;
    private float speed;

    public SideDash(BotController botController) {
        this(botController, false, 1);
    }

    public SideDash(BotController botController, boolean left, float speed) {
        super(botController);
        this.left = left;
        this.speed = speed;
    }

    @Override
    protected List<OutputSequence> loadChildren() {
        return Arrays.asList(
                new JumpMovement(1, 0f, left ? -0.4f : 0.4f, speed, botController),
                new ForwardMovement(1, speed),
                new JumpMovement(1, left ? -0.7f : 0.7f, left ? 0.4f : -0.4f, speed, botController)
        );
    }
}
