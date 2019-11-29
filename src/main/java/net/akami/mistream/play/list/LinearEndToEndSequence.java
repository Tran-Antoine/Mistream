package net.akami.mistream.play.list;

import net.akami.mistream.gamedata.CarInfoProvider;
import net.akami.mistream.vector.Vector3f;

import java.util.function.Function;

public class LinearEndToEndSequence extends EndToEndSequence {

    private float speed;

    protected LinearEndToEndSequence(Vector3f end, CarInfoProvider locProvider, float speed) {
        super(end, locProvider);
        this.speed = speed;
    }

    @Override
    protected Function<Integer, Float> getBoostFunction() {
        return (i) -> speed;
    }
}
