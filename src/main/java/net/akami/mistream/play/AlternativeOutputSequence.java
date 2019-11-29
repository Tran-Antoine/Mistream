package net.akami.mistream.play;

import net.akami.mistream.gamedata.DataHandler;
import net.akami.mistream.util.ProbabilityLaw;
import rlbot.ControllerState;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AlternativeOutputSequence implements OutputSequence {

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue, DataHandler gameData) {
        List<OutputSequence> sequences = loadAlternatives()
                .stream()
                .filter((seq) -> seq.weight(gameData, queue) != 0)
                .collect(Collectors.toList());
        return ProbabilityLaw.of(sequences, (seq) -> seq.weight(null, queue))
                .draw()
                .apply(queue, gameData);
    }

    protected abstract List<OutputSequence> loadAlternatives();

    @Override
    public boolean isStopped() {
        throw new IllegalStateException("AlternativeOutputSequences may be present, but should not be used in the queue");
    }
}
