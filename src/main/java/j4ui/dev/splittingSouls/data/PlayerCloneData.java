package j4ui.dev.splittingSouls.data;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlayerCloneData {
    private final String playerUuid;
    private Vec3d position;
    private float yaw;
    private float pitch;
    private World world;
    private boolean exists;

    public PlayerCloneData(String playerUuid) {
        this.playerUuid = playerUuid;
        this.exists = false;
    }

    // Getters
    public String getPlayerUuid() {
        return playerUuid;
    }

    public Vec3d getPosition() {
        return position;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public World getWorld() {
        return world;
    }

    public boolean exists() {
        return exists;
    }

    // Setters
    public void setPosition(Vec3d position) {
        this.position = position;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setWorld(World world) {
        this.world = world;
    }
    public void setExists(boolean exists) {
        this.exists = exists;
    }

}