package net.akami.mistream.play.list;

import net.akami.mistream.gamedata.CarInfoProvider;
import net.akami.mistream.output.ControlsOutput;
import net.akami.mistream.play.OutputSequence;
import net.akami.mistream.play.UnfragmentableOutputSequence;
import net.akami.mistream.vector.Vector2f;
import net.akami.mistream.vector.Vector3f;
import rlbot.ControllerState;

import java.util.LinkedList;
import java.util.function.Function;

// TODO : Some stuff can be moved to the upper class
public abstract class EndToEndSequence extends UnfragmentableOutputSequence {

    protected Vector3f end;
    private CarInfoProvider locProvider;
    private int frameExecuted;

    protected EndToEndSequence(Vector3f end, CarInfoProvider locProvider) {
        this.end = end;
        this.locProvider = locProvider;
    }

    protected abstract Function<Integer, Float> getBoostFunction();

    @Override
    public ControllerState apply(LinkedList<OutputSequence> queue) {

        Vector2f carDir = locProvider.getBotDirection().flatten();
        Vector2f carLoc = locProvider.getBotLocation().flatten();
        Vector2f carIdealDir = end.minus(locProvider.getBotLocation()).flatten();

        float steer;
        boolean drift = false;
        float angle = (float) carDir.correctionAngle(carIdealDir);
        float speed = getBoostFunction().apply(++frameExecuted);
        double distance = carLoc.distance(end.flatten());

        if(Math.abs(angle) / distance > 0.001) {
            drift = true;
        }

        if(angle > 0.2) {
            steer = -1;
        } else if(angle < -0.2) {
            steer = 1;
        } else {
            steer = 0;//- (float) Math.sin(angle) / 2;
        }


        return new ControlsOutput()
                .withSteer(steer)
                .withThrottle(speed == -1 ? 1 : speed)
                .withBoost(speed == -1 && !drift)
                .withSlide(drift);
    }

    @Override
    public boolean isStopped() {
        return locProvider.getBotLocation().approximatelyEquals(end, 50);
    }
}
