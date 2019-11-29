package net.akami.mistream.play;

import net.akami.mistream.core.BotController;
import net.akami.mistream.gamedata.DataHandler;
import rlbot.ControllerState;

import java.util.LinkedList;
import java.util.List;

public abstract class FragmentedOutputSequence implements OutputSequence {

    private List<OutputSequence> children;
    protected BotController botController;

    public FragmentedOutputSequence(BotController botController) {
        this.botController = botController;
    }

    protected abstract List<OutputSequence> loadChildren();

    @Override
    public boolean isStopped() {
        throw new IllegalStateException("FragmentedOutputSequences may be present, but should not be used in the queue");
    }

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue, DataHandler gameData) {
        if(children == null)
            children = loadChildren();

        OutputSequence first = children.remove(0);
        MutableInteger integer = new MutableInteger();
        children.forEach(child -> queue.add(integer.i++, child));
        return first.apply(queue, gameData);
    }

    // Only used for lambda incrementation
    private static class MutableInteger {
        private int i = 0;
    }
}
