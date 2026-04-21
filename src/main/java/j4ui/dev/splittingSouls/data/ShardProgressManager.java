package j4ui.dev.splittingSouls.data;

import com.google.gson.*;
import j4ui.dev.splittingSouls.SplittingSouls;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShardProgressManager {
    private static final String FILE_NAME = "shard_progress.json";
    private final Map<UUID, Float> progressMap = new HashMap<>();
    private final Path dataFilePath;

    public ShardProgressManager(MinecraftServer server) {
        this.dataFilePath = server.getSavePath(WorldSavePath.ROOT)
                .resolve("splitting_souls")
                .resolve(FILE_NAME);
        try {
            Files.createDirectories(dataFilePath.getParent());
        } catch (IOException e) {
            SplittingSouls.LOGGER.error("Failed to create directories for shard progress data", e);
        }
        loadData();
    }

    public float getProgress(UUID uuid) {
        return progressMap.getOrDefault(uuid, 0f);
    }

    public void addProgress(UUID uuid, float amount) {
        setProgress(uuid, getProgress(uuid) + amount);
    }

    public boolean canSplit(UUID uuid) {
        return getProgress(uuid) >= 1.0f;
    }

    public void setProgress(UUID uuid, float progress) {
        progressMap.put(uuid, progress);
        saveData();
    }

    public void saveData() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject root = new JsonObject();
        for (Map.Entry<UUID, Float> entry : progressMap.entrySet()) {
            root.addProperty(entry.getKey().toString(), entry.getValue());
        }
        try (FileWriter writer = new FileWriter(dataFilePath.toFile())) {
            gson.toJson(root, writer);
        } catch (IOException e) {
            SplittingSouls.LOGGER.error("Failed to save shard progress data", e);
        }
    }

    private void loadData() {
        if (!dataFilePath.toFile().exists() || dataFilePath.toFile().length() == 0) {
            return;
        }
        try (FileReader reader = new FileReader(dataFilePath.toFile())) {
            JsonObject root = new Gson().fromJson(reader, JsonObject.class);
            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                progressMap.put(UUID.fromString(entry.getKey()), entry.getValue().getAsFloat());
            }
        } catch (IOException e) {
            SplittingSouls.LOGGER.error("Failed to load shard progress data", e);
        }
    }
}
