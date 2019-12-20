package net.akami.mistream.play;

import net.akami.mistream.core.InputProcessor;
import net.akami.mistream.gamedata.DataHandler;
import net.akami.mistream.gamedata.DataProvider;
import net.akami.mistream.util.ProbabilityLaw;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Manager class for the queue system. The QueueHandler object helps the {@link InputProcessor} determine what {@link ControllerState}
 * must be returned in the {@link InputProcessor#processInput(GameTickPacket)} method for each frame throughout the entire game. <br>
 * It handles a queue, containing different 'plays'. A play is defined as an {@link OutputSequence}, and tells what the bot must do at what time.
 * Obviously, a single play cannot determine what the bot should do for the whole game. Therefore, whenever a play is finished, the handler
 * will chuck it, and ask the play at the top of the queue to take effect. The play at the second place in the queue (if existing) then moves
 * on to the top of the queue, and so on. <br>
 * The other main utility of the {@link QueueHandler} class is to actually fill this queue, by trying to add new plays to it every frame. <br>
 * In order to do that, the handler relies on the 'weight' system. Depending on the current situation of the game, each play has a different
 * amount of chances to be picked. For instance, there is very little chances that the bot chooses to rush for the ball if it is 5 seconds away from
 * it and has 0 boost. On the other hand, shooting directly towards the net if the opponent is out of position has a huge percentage of chances
 * to be chosen. What is also to be taken into account when searching for a new play is the current state of the queue. If a ceiling shot is
 * planned as the next play, it is also extremely unlikely that the bot will add a boost steal to the queue. A recovery play, however, might be appropriate.
 * Once every weight is calculated, the bot will randomly pick up one of the sequences, according to the law of probability created by these weights. <br>
 * It may happen that the queue is empty. In this case, if the current sequence gets terminated, the handler will just provide nothing, telling
 * the input processor to use a default {@link ControllerState}, asking the bot to do nothing, while it is preparing a new play.
 *
 */
public abstract class QueueHandler extends DataHandler implements DataProvider {

    private static final int MAX_PREDICTIONS = 5;

    // Main element of the program. The queue stores "plays", that are played one after another
    protected final LinkedList<OutputSequence> queue;
    // The current sequence being applied
    protected OutputSequence currentSequence;
    // The actual list of available "plays"
    private final List<Function<QueueHandler, OutputSequence>> generator;

    /**
     * Constructs a queue handler. The queue is initialized as an empty LinkedList, and the list of suppliers
     * is created from {@link #loadPlaySuppliers()}.
     */
    public QueueHandler() {
        this.queue = new LinkedList<>();
        this.generator = loadPlaySuppliers();
    }

    /**
     * Provides the {@link ControllerState} that the input processor must rely on.
     * @return the current sequence if existing, otherwise an empty optional
     */
    public Optional<ControllerState> provideController() {
        if(currentSequence == null || currentSequence.isStopped()) {
            this.currentSequence = queue.poll();
            if(currentSequence == null) {
                return Optional.empty();
            }
        }
        return Optional.of(currentSequence.apply(queue, this));
    }

    @Override
    public void update(GameTickPacket packet) {
        dataProviders.forEach((dataProvider -> dataProvider.update(packet)));
        updateQueue();
    }

    /**
     * Updates the queue by searching for new plays. <br>
     * New sequences are chosen using a {@link ProbabilityLaw}, created from the different weights of the available plays.
     */
    protected void updateQueue() {

        // If several trajectories are already planned, we avoid loading any other. Plays can not be predicted so far ahead
        if(queue.size() >= MAX_PREDICTIONS) {
            return;
        }

        List<OutputSequence> sequences = generator
                .stream()
                .map((f) -> f.apply(this))
                .collect(Collectors.toList());

        ProbabilityLaw.of(sequences, (seq) -> seq.weight(queue, this))
                .draw()
                .ifPresent(this::queue);
    }

    private void queue(OutputSequence target) {
        System.out.println("Queuing a new sequence : " + target.name());
        target.queue(queue);
    }

    public boolean isBotInactive() {
        return currentSequence == null && queue.size() == 0;
    }
    public OutputSequence getCurrentSequence() {
        return currentSequence;
    }
    protected abstract List<Function<QueueHandler, OutputSequence>> loadPlaySuppliers();
}
