package net.akami.mistream.core;

import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.play.OutputSequence;
import net.akami.mistream.play.QueueHandler;
import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;

public class InputProcessor implements Bot {

    // for debugging purposes
    private String currentPlayName;
    private boolean debug = true;

    private final int playerIndex;
    private final QueueHandler manager;

    public InputProcessor(int playerIndex, QueueHandler manager) {
        this.playerIndex = playerIndex;
        this.manager = manager;
    }

    @Override
    public ControllerState processInput(GameTickPacket packet) {
        if(debug) {
            debug();
        }

        if (error(packet)) {
            return ControlsOutput.EMPTY;
        }

        manager.update(packet);
        ControllerState state = manager.provideController().orElse(ControlsOutput.EMPTY);
        return state;
    }

    private void debug() {
        OutputSequence current = manager.getCurrentSequence();
        if(current != null) {
            String name = manager.getCurrentSequence().name();
            if(!name.equals(currentPlayName)) {
                this.currentPlayName = name;
                System.out.println("Current play : " + currentPlayName);
            }
        }
    }

    private boolean error(GameTickPacket packet) {
        return packet.ball() == null || !packet.gameInfo().isRoundActive();
    }

    @Override
    public void retire() {
        System.out.println("Retiring sample bot " + playerIndex);
    }

    @Override
    public int getIndex() {
        return this.playerIndex;
    }
}
