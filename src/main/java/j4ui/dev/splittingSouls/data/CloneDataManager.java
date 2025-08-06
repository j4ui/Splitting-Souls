package j4ui.dev.splittingSouls.data;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.Vec3d;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CloneDataManager {
    private static final String FILE_NAME = "player_clones.json";
    private final Map<UUID, PlayerCloneData> cloneDataMap = new HashMap<>();
    private final Path dataFilePath;

    public CloneDataManager() {
        this.dataFilePath = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
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

        try (FileWriter writer = new FileWriter(dataFilePath.toFile())) {
            JsonObject root = new JsonObject();

            for (Map.Entry<UUID, PlayerCloneData> entry : cloneDataMap.entrySet()) {
                if (!entry.getValue().exists()) continue;

                JsonObject playerData = new JsonObject();
                playerData.addProperty("world", entry.getValue().getWorld().getRegistryKey().getValue().toString());
                playerData.add("position", gson.toJsonTree(entry.getValue().getPosition()));
                playerData.addProperty("yaw", entry.getValue().getYaw());
                playerData.addProperty("pitch", entry.getValue().getPitch());

                root.add(entry.getKey().toString(), playerData);
            }

            gson.toJson(root, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        if (!dataFilePath.toFile().exists()) return;

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
                // Note: World will need to be set when the player logs in
                cloneData.setExists(true);

                cloneDataMap.put(uuid, cloneData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}