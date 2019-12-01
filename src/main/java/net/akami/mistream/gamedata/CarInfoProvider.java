package net.akami.mistream.gamedata;

import net.akami.mistream.vector.Vector3f;
import rlbot.flat.GameTickPacket;
import rlbot.flat.Physics;
import rlbot.flat.PlayerInfo;
import rlbot.flat.Rotator;

import java.util.function.BiConsumer;

public class CarInfoProvider implements DataProvider {

    private Vector3f botLocation;
    private Vector3f botDirection;
    private Vector3f playerLocation;
    private Vector3f playerDirection;

    @Override
    public void update(GameTickPacket packet) {
        update(this::updateBot, packet.players(0));
        if(packet.playersLength() == 2)
            update(this::updatePlayer, packet.players(1));
    }

    private void update(BiConsumer<Vector3f, Vector3f> setter, PlayerInfo info) {
        Physics physics = info.physics();
        Vector3f loc = new Vector3f(physics.location());
        Rotator rotation = physics.rotation();
        float yaw = rotation.yaw();
        float pitch = rotation.pitch();
        double noseX = -1 * Math.cos(pitch) * Math.cos(yaw);
        double noseY = Math.cos(pitch) * Math.sin(yaw);
        double noseZ = Math.sin(pitch);
        Vector3f dir = new Vector3f(noseX, noseY, noseZ);
        setter.accept(loc, dir);
    }

    private void updatePlayer(Vector3f loc, Vector3f dir) {
        this.playerLocation = loc;
        this.playerDirection = dir;
    }

    private void updateBot(Vector3f loc, Vector3f dir) {
        this.botLocation = loc;
        this.botDirection = dir;
    }

    public Vector3f getBotLocation() {
        return botLocation;
    }

    public Vector3f getBotDirection() {
        return botDirection;
    }

    public Vector3f getPlayerLocation() {
        return playerLocation;
    }

    public Vector3f getPlayerDirection() {
        return playerDirection;
    }
}
