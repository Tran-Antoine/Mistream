package net.akami.mistream.play;

import net.akami.mistream.gamedata.DataHandler;
import rlbot.ControllerState;

import java.util.LinkedList;
import java.util.Queue;

public interface OutputSequence {

    ControllerState apply(LinkedList<OutputSequence> queue, DataHandler gameData);
    boolean isStopped();

    default void queue(Queue<OutputSequence> target) { target.add(this); }
    default float weight(DataHandler gameData, Queue<OutputSequence> queue) { return 0; }
    default float binaryWeight(boolean suitable) { return suitable ? 1 : 0; }
}
