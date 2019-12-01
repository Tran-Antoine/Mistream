package net.akami.mistream.play;

import net.akami.mistream.core.MistreamDisplay;
import net.akami.mistream.gamedata.*;
import net.akami.mistream.play.list.DiagonalKickoff;
import net.akami.mistream.util.ProbabilityLaw;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QueueHandler extends DataHandler implements DataProvider {

    private static final int MAX_PREDICTIONS = 5;

    private MistreamDisplay display;
    // Main element of the program. The queue stores "plays", that are played one after another
    private final LinkedList<OutputSequence> queue;
    // The current sequence being applied
    private OutputSequence currentSequence;
    // The actual list of available "plays"
    private final List<Function<QueueHandler, OutputSequence>> generator;

    public QueueHandler(MistreamDisplay display) {
        this.display = display;
        this.queue = new LinkedList<>();
        this.generator = loadSuppliers();
    }

    @Override
    public void update(GameTickPacket packet) {
        dataProviders.forEach((dataProvider -> dataProvider.update(packet)));
        updateQueue();
        updateDisplay();
    }

    private void updateQueue() {

        // If several trajectories are already planned, we avoid loading any other. Plays can not be predicted so far ahead
        if(queue.size() >= MAX_PREDICTIONS) {
            return;
        }

        List<OutputSequence> sequences = generator
                .stream()
                .map((f) -> f.apply(this))
                .collect(Collectors.toList());
        OutputSequence suitableSequence = ProbabilityLaw.of(sequences, (seq) -> seq.weight(queue, this))
                .draw();
        if(suitableSequence != null) {
            System.out.println("Queuing a new sequence : " + suitableSequence.name());
            suitableSequence.queue(queue);
        }
    }

    private void updateDisplay() {
        display.setText(currentSequence);
    }

    public Optional<ControllerState> provideController() {
        if(currentSequence == null || currentSequence.isStopped()) {
            this.currentSequence = queue.poll();
            if(currentSequence == null) {
                return Optional.empty();
            }
        }
        return Optional.of(currentSequence.apply(queue, this));
    }

    public boolean isBotInactive() {
        return currentSequence == null && queue.size() == 0;
    }

    private List<Function<QueueHandler, OutputSequence>> loadSuppliers() {
        return Arrays.asList(
                DiagonalKickoff::new
        );
    }

    @Override
    protected List<DataProvider> loadProviders() {
        return Arrays.asList(
                new BoostDataProvider(),
                new CarInfoProvider(),
                new GameState()
        );
    }

    public OutputSequence getCurrentSequence() {
        return currentSequence;
    }
}
