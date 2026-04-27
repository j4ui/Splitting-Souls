package j4ui.dev.splittingSouls.data;

import com.google.gson.*;
import j4ui.dev.splittingSouls.SplittingSouls;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CloneDataManager {
    private static final String FILE_NAME = "player_clones.json";
    private final Map<UUID, PlayerCloneData> cloneDataMap = new HashMap<>();
    private final Path dataFilePath;
    private final MinecraftServer server;

    public CloneDataManager(MinecraftServer server) {
        this.server = server;
        this.dataFilePath = server.getSavePath(WorldSavePath.ROOT)
                .resolve("splitting_souls")
                .resolve(FILE_NAME);
        try {
            Files.createDirectories(dataFilePath.getParent());
        } catch (IOException e) {
            SplittingSouls.LOGGER.error("Failed to create directories for clone data", e);
        }
        loadData();
    }

    public PlayerCloneData getOrCreate(UUID playerUuid) {
        return cloneDataMap.computeIfAbsent(playerUuid, uuid -> new PlayerCloneData(uuid.toString()));
    }

    public void saveData() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Vec3d.class, new Vec3dSerializer())
                .setPrettyPrinting()
                .create();

        try {
            // First, load any existing data to preserve it
            JsonObject root = new JsonObject();
            if (dataFilePath.toFile().exists() && dataFilePath.toFile().length() > 0) {
                try (FileReader reader = new FileReader(dataFilePath.toFile())) {
                    JsonElement element = JsonParser.parseReader(reader);
                    if (element != null && element.isJsonObject()) {
                        root = element.getAsJsonObject();
                    }
                } catch (Exception e) {
                    SplittingSouls.LOGGER.error("Error reading existing clone data", e);
                }
            }

            // Update with current data
            boolean hasData = false;
            for (Map.Entry<UUID, PlayerCloneData> entry : cloneDataMap.entrySet()) {
                PlayerCloneData data = entry.getValue();
                if (data.exists() && data.getWorld() != null && data.getPosition() != null) {
                    JsonObject playerData = new JsonObject();
                    playerData.addProperty("world", data.getWorld().getRegistryKey().getValue().toString());
                    playerData.add("position", gson.toJsonTree(data.getPosition()));
                    playerData.addProperty("yaw", data.getYaw());
                    playerData.addProperty("pitch", data.getPitch());
                    if (data.getEntityUUID() != null) {
                        playerData.addProperty("entityUUID", data.getEntityUUID().toString());
                    }
                    root.add(entry.getKey().toString(), playerData);
                    hasData = true;
                }
            }

            // Only write if we have data
            if (hasData || !root.isEmpty()) {
                try (FileWriter writer = new FileWriter(dataFilePath.toFile())) {
                    gson.toJson(root, writer);
                }
            }
        } catch (IOException e) {
            SplittingSouls.LOGGER.error("Failed to save clone data", e);
        }
    }

    private void loadData() {
        if (!dataFilePath.toFile().exists() || dataFilePath.toFile().length() == 0) {
            return;
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Vec3d.class, new Vec3dDeserializer())
                .create();

        try (FileReader reader = new FileReader(dataFilePath.toFile())) {
            JsonObject root = gson.fromJson(reader, JsonObject.class);

            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                UUID uuid = UUID.fromString(entry.getKey());
                JsonObject playerData = entry.getValue().getAsJsonObject();

                PlayerCloneData cloneData = new PlayerCloneData(uuid.toString());
                cloneData.setPosition(gson.fromJson(playerData.get("position"), Vec3d.class));
                cloneData.setYaw(playerData.get("yaw").getAsFloat());
                cloneData.setPitch(playerData.get("pitch").getAsFloat());

                String worldStr = playerData.get("world").getAsString();
                RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(worldStr));
                ServerWorld world = server.getWorld(worldKey);
                cloneData.setWorld(world);

                if (playerData.has("entityUUID")) {
                    cloneData.setEntityUUID(UUID.fromString(playerData.get("entityUUID").getAsString()));
                }
                cloneData.setExists(true);

                cloneDataMap.put(uuid, cloneData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateCloneData(UUID playerUuid, PlayerCloneData newData) {
        cloneDataMap.put(playerUuid, newData);
        saveData();
    }

    public void clearClone(UUID playerUuid) {
        cloneDataMap.remove(playerUuid);
        saveData();
    }
}
