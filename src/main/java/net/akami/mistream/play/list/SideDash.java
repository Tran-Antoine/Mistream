package net.akami.mistream.play.list;

import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.play.FragmentedSequence;
import net.akami.mistream.play.OutputSequence;

import java.util.Arrays;
import java.util.List;

public class SideDash extends FragmentedSequence {

    private boolean left;
    private float speed;

    public SideDash(QueueHandler botController) {
        this(botController, false, 1);
    }

    public SideDash(QueueHandler botController, boolean left, float speed) {
        super(botController);
        this.left = left;
        this.speed = speed;
    }

    @Override
    protected List<OutputSequence> loadChildren() {
        return Arrays.asList(
                new JumpMovement(0.01f, 0f, left ? -0.4f : 0.4f, speed, botController),
                new ForwardMovement(0.1f, speed),
                new JumpMovement(0.1f, left ? -0.7f : 0.7f, left ? 0.4f : -0.4f, speed, botController)
        );
    }

    @Override
    public String name() {
        return "side dash";
    }
}
