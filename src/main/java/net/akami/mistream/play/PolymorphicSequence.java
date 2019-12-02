package net.akami.mistream.play;

import net.akami.mistream.gamedata.DataHandler;
import net.akami.mistream.util.ProbabilityLaw;
import rlbot.ControllerState;

import java.util.LinkedList;
import java.util.List;

public abstract class PolymorphicSequence implements OutputSequence {

    private List<OutputSequence> possibilities;

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue, DataHandler gameData) {
        if(possibilities == null) {
            possibilities = loadAlternatives();
        }
        return ProbabilityLaw.of(possibilities, (seq) -> seq.weight(queue, null))
                .draw()
                .orElseThrow(() -> new IllegalStateException("Could not find a suitable sequence"))
                .apply(queue, gameData);
    }

    protected abstract List<OutputSequence> loadAlternatives();

    @Override
    public boolean isStopped() {
        throw new IllegalStateException("AlternativeOutputSequences may be present, but should not be used in the queue");
    }
}
