package net.akami.mistream.core;

import net.akami.mistream.output.ControlsOutput;
import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;

public class InputProcessor implements Bot {

    private final int playerIndex;
    private final BotController manager;

    public InputProcessor(int playerIndex, BotController manager) {
        this.playerIndex = playerIndex;
        this.manager = manager;
    }

    @Override
    public ControllerState processInput(GameTickPacket packet) {
        if (error(packet)) {
            return ControlsOutput.EMPTY;
        }

        manager.update(packet);
        ControllerState state = manager.provideController().orElse(ControlsOutput.EMPTY);
        return state;
    }

    private boolean error(GameTickPacket packet) {
        return packet.playersLength() < 2 || packet.ball() == null
                || !packet.gameInfo().isRoundActive();
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
