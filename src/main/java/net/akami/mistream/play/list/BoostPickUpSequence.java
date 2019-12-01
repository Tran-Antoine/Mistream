package net.akami.mistream.play.list;

import net.akami.mistream.play.QueueHandler;
import net.akami.mistream.gamedata.BoostDataProvider;
import net.akami.mistream.gamedata.BoostPad;
import net.akami.mistream.gamedata.CarInfoProvider;

import java.util.function.Function;


public class BoostPickUpSequence extends EndToEndSequence {

    private BoostDataProvider boostData;
    private BoostPad destination;

    public BoostPickUpSequence(BoostPad destination, QueueHandler botController) {
        super(destination.getLocation(), botController.data(CarInfoProvider.class));
        this.boostData = botController.data(BoostDataProvider.class);
        this.destination = destination;
    }


    @Override
    protected Function<Long, Float> getBoostFunction() {
        return (f) -> -1f;
    }

    @Override
    public boolean isStopped() {
        return !boostData.isPadActive(this.destination);
    }

    @Override
    public String name() {
        return "Boost pick up";
    }
}
