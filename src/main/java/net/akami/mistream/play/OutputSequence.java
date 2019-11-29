package net.akami.mistream.play;

import net.akami.mistream.gamedata.DataHandler;
import net.akami.mistream.play.list.EndToEndSequence;
import net.akami.mistream.util.ProbabilityLaw;
import rlbot.ControllerState;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Root class of the queuing system. Output sequences determine which {@link ControllerState} must be used at what time as output
 * value for the bot. Sequences are initially stored by the manager in the queue, and eventually perform a defined action
 * when required. Several implementations are already provided :
 * <ul>
 *     <li> {@link TerminalSequence}s are simple wrappers for {@link ControllerState}s. Once created, the define a fixed
 *          ControllerState that will always be returned, as long as the sequence is active. Terminal sequenced are usually
 *          stopped after a provided execution time.
 *     </li>
 *     <li>
 *         {@link AlternativeSequence}s hold a list of {@link OutputSequence}s, and chose one element
 *         from their list (according to the weight of each element) when applied for the first time. The chosen sequence
 *         does NOT need to be determined before the alternative sequenced takes effect (that is, when it reaches the top of the queue and the current
 *         sequence is stopped or null). Since alternative sequences are not able to provide controllers, they deliver the task
 *         to their chosen child
 *     </li>
 *     <li>
 *         {@link PolymorphicSequence}s are similar to {@link AlternativeSequence}s, but instead of defining which {@link OutputSequence}
 *         is suitable once for all (when the {@code apply} method is called for the first time), they constantly calculate which
 *         one is <b>currently</b> the most suitable, according to the respective weights of the handled children. <br>
 *         Beware that polymorphic sequences are not recommended for long sequences, because they are rather heavy, since they
 *         perform probability calculations every frame.
 *     </li>
 *     <li>
 *         {@link FragmentedSequence}s are probably the most used sequences. They hold a list of {@link OutputSequence}s, which
 *         will be <b>entirely</b> added to the queue when applied for the first time. All fragmented sequences should therefore
 *         be stopped immediately after they are applied, since they are not it themselves able to provide a controller. Their
 *         only purpose is to put all their children to the top of the queue. They are extremely useful because manually adding
 *         several sequences (instead of just one fragmented sequence) to the queue cause several issues :
 *         <ul>
 *             <li>
 *                 It becomes very unclear what plays are currently present in the queue. Fragmented sequences are a very practical
 *                 way of grouping sequences, so that a name can be given.
 *             </li>
 *             <li>
 *                 When the queue is too large, it is not updated anymore, since it is assumed that plays cannot be too much
 *                 anticipated. A group of sequences counts as one "place" in the queue, therefore the next play can be predicted even
 *                 if a 10 moves kickoff is prepared, because after all, that's just a kickoff.
 *             </li>
 *             <li>
 *                 It is easier to remove one sequence than several "linked" sequences. Groups of sequences are thus a way of
 *                 easily managing plays throughout the game, without having a hard time determining which sequences must be deleted
 *                 or modified together
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         {@link UnfragmentableSequence}s are a mix between {@link TerminalSequence}s and {@link PolymorphicSequence}s. They
 *         describe bot actions that can not be fully determined beforehand. {@link EndToEndSequence} would be a great example.
 *         Given a situation where the bot has to reach a certain destination, its steer and throttle can only be determined on
 *         the fly, because the trajectory might be purposelessly tricky to compute in one go.
 *     </li>
 *
 * </ul>
 */
public interface OutputSequence {

    /**
     * Defines what actions are to be performed when the sequences is the current used one. Sequences become the "current used one"
     * whenever they meet the two following conditions : they are the head of the queue, and the previous current sequence is null
     * or stopped. <br>
     * Additionally, the sequence must return what controller has to take effect while it is active.
     * @param queue the current queue
     * @param gameData the data of the current game
     * @return what controller must be applied
     */
    ControllerState apply(LinkedList<OutputSequence> queue, DataHandler gameData);
    boolean isStopped();

    /**
     * Defines how exactly the sequence should be added in the given queue. In 99% of the cases, this method
     * does not need to be overridden : The object will be added through {@link Queue#add(Object)}. <br>
     * However, there might be some cases where the play is to be added differently : at the top of the queue for instance
     * @param target the queue that the sequence has to be added to
     */
    default void queue(Queue<OutputSequence> target) { target.add(this); }

    /**
     * The probability that the given sequence will be chosen. <br>
     * Probabilities are NOT given in percent, thus returning {@code 0.1} does not necessarily mean that the given
     * sequence has 10% chances to be picked. <br>
     * Note that {@link ProbabilityLaw#GUARANTEED} sets that the sequence is to be chosen regardless of the others.
     * Several guaranteed plays should not occur, they will lead to arbitrary choosing.
     *
     * @param queue the queue that might contain the sequence if picked
     * @param gameData the data of the current game
     * @return a positive float (except for guaranteed plays) determining the probability that the sequence is chosen.
     */
    default float weight(Queue<OutputSequence> queue, DataHandler gameData) { return 0; }

    /**
     * Used for sequences that either are guaranteed to be picked, or guaranteed not to be picked. <br>
     * @param suitable whether the play is guaranteed to be chosen, or guaranteed not to be chosen
     * @return {@link ProbabilityLaw#GUARANTEED} is {@code suitable} is {@code true}, {@code 0} otherwise
     */
    default float binaryWeight(boolean suitable) { return suitable ? ProbabilityLaw.GUARANTEED : 0; }
}
