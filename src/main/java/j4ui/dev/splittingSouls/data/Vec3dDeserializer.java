package j4ui.dev.splittingSouls.data;

import com.google.gson.*;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Type;



public class Vec3dDeserializer implements JsonDeserializer<Vec3d> {
    @Override
    public Vec3d deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        return new Vec3d(
                obj.get("x").getAsDouble(),
                obj.get("y").getAsDouble(),
                obj.get("z").getAsDouble()
        );
    }
}