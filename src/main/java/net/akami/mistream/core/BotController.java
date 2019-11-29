package net.akami.mistream.core;

import net.akami.mistream.gamedata.BoostDataProvider;
import net.akami.mistream.gamedata.CarInfoProvider;
import net.akami.mistream.gamedata.DataHandler;
import net.akami.mistream.gamedata.DataProvider;
import net.akami.mistream.play.OutputSequence;
import net.akami.mistream.play.list.DiagonalKickoff;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;

import java.util.*;
import java.util.function.Function;

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
        updateQueue(packet);
    }

    private void updateQueue(GameTickPacket packet) {
        // If several trajectories are already planned, we avoid loading any other. Plays can not be predicted so far ahead
        if(queue.size() >= MAX_PREDICTIONS) {
            return;
        }

        for(Function<BotController, OutputSequence> supplier : generator) {
            OutputSequence sequence = supplier.apply(this);
            if(sequence.isSuitable(packet, queue)) {
                sequence.queue(queue);
            }
        }
    }

    public Optional<ControllerState> provideController() {
        if(currentSequence == null || currentSequence.isStopped()) {
            this.currentSequence = queue.poll();
            if(currentSequence == null) {
                return Optional.empty();
            }
        }
        return Optional.of(currentSequence.apply(queue));
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
                new CarInfoProvider()
        );
    }
}
