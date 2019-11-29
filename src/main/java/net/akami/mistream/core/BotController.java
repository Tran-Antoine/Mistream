package net.akami.mistream.core;

import net.akami.mistream.gamedata.*;
import net.akami.mistream.play.OutputSequence;
import net.akami.mistream.play.list.DiagonalKickoff;
import net.akami.mistream.util.ProbabilityLaw;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BotController extends DataHandler implements DataProvider {

    private static final int MAX_PREDICTIONS = 5;

    // Main element of the program. The queue stores "plays", that are played one after another
    private final LinkedList<OutputSequence> queue;
    // The current sequence being applied
    private OutputSequence currentSequence;
    // The actual list of available "plays"
    private final List<Function<BotController, OutputSequence>> generator;

    public BotController() {
        this.queue = new LinkedList<>();
        this.generator = loadSuppliers();
    }

    @Override
    public void update(GameTickPacket packet) {
        dataProviders.forEach((dataProvider -> dataProvider.update(packet)));
        updateQueue();
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

        OutputSequence suitableSequence = ProbabilityLaw.of(sequences, (seq) -> seq.weight(this, queue))
                .draw();

        suitableSequence.queue(queue);
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

    private List<Function<BotController, OutputSequence>> loadSuppliers() {
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
}
