package net.akami.mistream.play;

import net.akami.mistream.gamedata.DataHandler;
import rlbot.ControllerState;

import java.util.LinkedList;
import java.util.List;

public abstract class FragmentedSequence implements OutputSequence {

    private List<OutputSequence> children;
    protected QueueHandler botController;

    public FragmentedSequence(QueueHandler botController) {
        this.botController = botController;
    }

    protected abstract List<OutputSequence> loadChildren();

    @Override
    public boolean isStopped() {
        // As soon as it is picked, it must be replaced by its children
        return true;
    }

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue, DataHandler gameData) {
        if(children == null)
            children = loadChildren();

        MutableInteger integer = new MutableInteger();
        children.forEach(child -> queue.add(integer.i++, child));
        // So that we don't lose a game tick of activity because the fragmented sequence does not have its own controller
        return children.get(0).apply(queue, gameData);
    }

    // Only used for lambda incrementation
    private static class MutableInteger {
        private int i = 0;
    }
}
