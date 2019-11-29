package net.akami.mistream.play;

import rlbot.ControllerState;

import java.util.LinkedList;
import java.util.List;

public abstract class AlternativeOutputSequence implements OutputSequence {

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue) {
        for(OutputSequence current : loadAlternatives()) {
            if(current.isSuitable(null, queue)) {
                return current.apply(queue);
            }
        }
        throw new IllegalStateException("Unreachable statement. No alternative found");
    }

    protected abstract List<OutputSequence> loadAlternatives();

    @Override
    public boolean isStopped() {
        throw new IllegalStateException("AlternativeOutputSequences may be present, but should not be used in the queue");
    }
}
