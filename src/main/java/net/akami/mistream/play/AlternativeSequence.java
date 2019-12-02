package net.akami.mistream.play;

import net.akami.mistream.gamedata.DataHandler;
import net.akami.mistream.util.ProbabilityLaw;
import rlbot.ControllerState;

import java.util.LinkedList;
import java.util.List;

public abstract class AlternativeSequence implements OutputSequence {

    private OutputSequence sequence;

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue, DataHandler gameData) {
        if(sequence == null) {
            setSequence(queue, gameData);
        }
        return sequence.apply(queue, gameData);
    }

    private void setSequence(LinkedList<OutputSequence> queue, DataHandler gameData) {
         this.sequence = ProbabilityLaw.of(loadAlternatives(), (seq) -> seq.weight(queue, gameData))
                 .draw()
                 .orElseThrow(() -> new IllegalStateException("Could not find a suitable sequence"));
    }

    protected abstract List<OutputSequence> loadAlternatives();

    @Override
    public boolean isStopped() {
        return sequence.isStopped();
    }
}
