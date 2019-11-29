package net.akami.mistream.gamedata;

import net.akami.mistream.vector.Vector3f;

public class BoostPad {

    private final Vector3f location;
    private final boolean isFullBoost;
    private boolean isActive;

    public BoostPad(Vector3f location, boolean isFullBoost) {
        this.location = location;
        this.isFullBoost = isFullBoost;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Vector3f getLocation() {
        return location;
    }

    public boolean isFullBoost() {
        return isFullBoost;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BoostPad) {
            return location.equals(((BoostPad) obj).location);
        }
        return false;
    }
}
