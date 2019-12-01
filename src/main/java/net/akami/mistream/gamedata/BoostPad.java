package net.akami.mistream.gamedata;

import net.akami.mistream.vector.Vector3f;

public class BoostPad {

    private final Vector3f location;
    private final boolean isFullBoost;

    public BoostPad(Vector3f location, boolean isFullBoost) {
        this.location = location;
        this.isFullBoost = isFullBoost;
    }

    public Vector3f getLocation() {
        return location;
    }

    public boolean isFullBoost() {
        return isFullBoost;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BoostPad) {
            return location.equals(((BoostPad) obj).location);
        }
        return false;
    }
}
