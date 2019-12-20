package net.akami.mistream.core;

import net.akami.mistream.gamedata.CarInfoProvider;
import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.play.OutputSequence;
import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.vector.Vector3f;
import rlbot.Bot;
import rlbot.ControllerState;
import rlbot.flat.GameTickPacket;
import rlbot.flat.PlayerInfo;

public class InputProcessor implements Bot {

    // for debugging purposes
    private String currentPlayName;
    private boolean debug = false;

    private final int playerIndex;
    private final QueueHandler manager;

    public InputProcessor(int playerIndex, QueueHandler manager) {
        this.playerIndex = playerIndex;
        this.manager = manager;
    }

    @Override
    public ControllerState processInput(GameTickPacket packet) {
        if(debug) {
            debug(packet);
        }

        if (error(packet)) {
            return ControlsOutput.EMPTY;
        }

        manager.update(packet);
        ControllerState state = manager.provideController().orElse(ControlsOutput.EMPTY);
        return state;
    }

    private void debug(GameTickPacket packet) {

        if(packet.playersLength() == 2) {
            PlayerInfo pInfo = packet.players(1);
            if(pInfo.jumped()) {
                System.out.println();
            }


            CarInfoProvider info = manager.data(CarInfoProvider.class);
            Vector3f carLoc = info.getPlayerLocation();
            if(carLoc != null) {
                Vector3f ballLoc = new Vector3f(packet.ball().physics().location());
                ballLoc = new Vector3f(ballLoc.x, ballLoc.y, ballLoc.z - 75);
                Vector3f carDir = info.getPlayerDirection();

                Vector3f idealDir = ballLoc.minus(carLoc);
                boolean lookAt = Math.abs(carDir.angle(idealDir)) < 0.1;
                if (lookAt) {
                    System.out.println("Looking at the ball");
                }
            }
        }
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
        System.out.println("Retiring bot " + playerIndex);
    }

    @Override
    public int getIndex() {
        return this.playerIndex;
    }
}
