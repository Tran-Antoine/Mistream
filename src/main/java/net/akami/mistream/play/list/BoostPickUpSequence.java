package net.akami.mistream.play.list;

import net.akami.mistream.core.BotController;
import net.akami.mistream.gamedata.BoostDataProvider;
import net.akami.mistream.gamedata.BoostPad;
import net.akami.mistream.gamedata.CarInfoProvider;

import java.util.function.Function;


public class BoostPickUpSequence extends EndToEndSequence {

    private BoostDataProvider boostData;
    private BoostPad destination;

    public BoostPickUpSequence(BoostPad destination, BotController botController) {
        super(destination.getLocation(), botController.data(CarInfoProvider.class));
        this.boostData = botController.data(BoostDataProvider.class);
        this.destination = destination;
    }


    @Override
    protected Function<Integer, Float> getBoostFunction() {
        return (f) -> -1f;
    }

    @Override
    public boolean isStopped() {
        return !boostData.isPadActive(this.destination);
    }
}
