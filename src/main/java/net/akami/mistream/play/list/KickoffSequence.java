package net.akami.mistream.play.list;

import net.akami.mistream.gamedata.CarInfoProvider;
import net.akami.mistream.gamedata.DataHandler;
import net.akami.mistream.gamedata.GameState;
import net.akami.mistream.play.FragmentedSequence;
import net.akami.mistream.play.OutputSequence;
import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.vector.Vector3f;
import rlbot.ControllerState;
import rlbot.cppinterop.RLBotDll;
import rlbot.flat.GameTickPacket;
import rlbot.gamestate.CarState;

import java.util.LinkedList;
import java.util.Queue;

public abstract class KickoffSequence extends FragmentedSequence {

    protected Vector3f absoluteLocation;

    public KickoffSequence(Vector3f absoluteLocation, QueueHandler botController) {
        super(botController);
        this.absoluteLocation = absoluteLocation;
    }

    @Override
    public float weight(Queue<OutputSequence> queue, DataHandler gameData) {
        GameTickPacket packet = gameData.data(GameState.class).getCurrentPacket();
        Vector3f convertedLocation = new Vector3f(packet.players(0).physics().location()).abs();
        return binaryWeight(convertedLocation.approximatelyEquals(this.absoluteLocation, 1)
                && queue.size() == 0
        );//&& packet.gameInfo().isKickoffPause());
    }

    public boolean isOnLeft() {
        return botController.data(CarInfoProvider.class).getBotLocation().x < 0;
    }

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue, DataHandler gameData) {
        rlbot.gamestate.GameState state = new rlbot.gamestate.GameState()
                .withCarState(0, new CarState()
                        .withBoostAmount(33f));
        RLBotDll.setGameState(state.buildPacket());
        return super.apply(queue, gameData);
    }
}
