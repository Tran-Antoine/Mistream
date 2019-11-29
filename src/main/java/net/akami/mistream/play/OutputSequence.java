package net.akami.mistream.play;

import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;

import java.util.LinkedList;
import java.util.Queue;

public interface OutputSequence {

    ControllerState apply(LinkedList<OutputSequence> queue);
    boolean isStopped();
    default void queue(Queue<OutputSequence> target) { target.add(this); }
    default boolean isSuitable(GameTickPacket packet, Queue<OutputSequence> queue) { return false; }
}
