package net.akami.mistream.play.list;

import net.akami.mistream.gamedata.CarInfoProvider;
import net.akami.mistream.gamedata.DataHandler;
import net.akami.mistream.gamedata.GameState;
import net.akami.mistream.play.FragmentedSequence;
import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.play.OutputSequence;
import net.akami.mistream.vector.Vector3f;
import rlbot.flat.GameTickPacket;

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
}
