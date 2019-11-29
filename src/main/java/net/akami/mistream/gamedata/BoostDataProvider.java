package net.akami.mistream.gamedata;

import net.akami.mistream.core.BotController;
import net.akami.mistream.vector.Vector2f;
import net.akami.mistream.vector.Vector3f;
import rlbot.cppinterop.RLBotDll;
import rlbot.cppinterop.RLBotInterfaceException;
import rlbot.flat.FieldInfo;
import rlbot.flat.GameTickPacket;

import java.util.ArrayList;
import java.util.List;

public class BoostDataProvider implements DataProvider {

    private final List<BoostPad> orderedBoosts = new ArrayList<>();
    private final List<BoostPad> fullBoosts = new ArrayList<>();
    private final List<BoostPad> smallBoosts = new ArrayList<>();

    @Override
    public void update(GameTickPacket packet) {
        FieldInfo fieldInfo;
        try {
            fieldInfo = RLBotDll.getFieldInfo();
        } catch (RLBotInterfaceException e) {
            e.printStackTrace();
            return;
        }
        synchronized (orderedBoosts) {
            orderedBoosts.clear();
            fullBoosts.clear();
            smallBoosts.clear();

            for (int i = 0; i < fieldInfo.boostPadsLength(); i++) {
                if(!packet.boostPadStates(i).isActive()) {
                    continue;
                }
                rlbot.flat.BoostPad flatPad = fieldInfo.boostPads(i);
                BoostPad ourPad = new BoostPad(new Vector3f(flatPad.location()), flatPad.isFullBoost());
                orderedBoosts.add(ourPad);
                if (ourPad.isFullBoost()) {
                    fullBoosts.add(ourPad);
                } else {
                    smallBoosts.add(ourPad);
                }
            }
        }
    }

    public BoostPad getNearestPad(BotController botController) {
        return getNearestPad(botController.data(CarInfoProvider.class));
    }

    public BoostPad getNearestPad(CarInfoProvider infoProvider) {
        return getNearestPad(infoProvider.getBotLocation());
    }

    public BoostPad getNearestPad(Vector3f carLocation) {
        double distance = 1000000;
        BoostPad currentNearest = null;

        for(BoostPad pad : orderedBoosts) {
            double localDistance = pad.getLocation().distance(carLocation);
            if(localDistance < distance) {
                distance = localDistance;
                currentNearest = pad;
            }
        }
        return currentNearest;
    }

    // This method is approximate. It does not 100% guarantee that the pad will be the quickest to be reached of all
    public BoostPad getFastestPad(Vector3f carLocation, Vector2f carDirection, float speed, boolean boost) {

        double factor = 100000;
        BoostPad currentBest = null;

        for(BoostPad pad : orderedBoosts) {
            Vector3f padLoc = pad.getLocation();
            double distance = padLoc.distance(carLocation);
            Vector2f idealDir = padLoc.minus(carLocation).flatten();
            double angle = carDirection.correctionAngle(idealDir);
            double angleFactor = 1 + 6*Math.abs(angle);
            double localFactor = distance * angleFactor;
            if(localFactor < factor) {
                factor = localFactor;
                currentBest = pad;
            }
        }
        return currentBest;
    }

    public boolean isPadActive(BoostPad destination) {
        return orderedBoosts.contains(destination);
    }
}
