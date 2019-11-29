package net.akami.mistream.gamedata;

import net.akami.mistream.vector.Vector3f;
import rlbot.flat.GameTickPacket;
import rlbot.flat.Physics;
import rlbot.flat.Rotator;
import rlbot.flat.Vector3;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CarInfoProvider implements DataProvider {

    private Vector3f botLocation;
    private Vector3f botDirection;
    private Vector3f playerLocation;

    // TODO : not update this all the time, but rather on demand
    @Override
    public void update(GameTickPacket packet) {
        BiFunction<Integer, Function<Physics, Vector3>, Vector3f> gen =
                (i, func) -> new Vector3f(func.apply(packet.players(i).physics()));
        this.botLocation = gen.apply(0, Physics::location);
        this.playerLocation = gen.apply(1, Physics::location);
        Rotator rotation = packet.players(0).physics().rotation();
        float yaw = rotation.yaw();
        float pitch = rotation.pitch();
        double noseX = -1 * Math.cos(pitch) * Math.cos(yaw);
        double noseY = Math.cos(pitch) * Math.sin(yaw);
        double noseZ = Math.sin(pitch);

        this.botDirection = new Vector3f(noseX, noseY, noseZ);
    }

    public Vector3f getBotLocation() {
        return botLocation;
    }

    public Vector3f getPlayerLocation() {
        return playerLocation;
    }

    public Vector3f getBotDirection() {
        return botDirection;
    }
}
