package j4ui.dev.splittingSouls.data;

import com.google.gson.*;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Type;

public class Vec3dSerializer implements JsonSerializer<Vec3d> {
    @Override
    public JsonElement serialize(Vec3d src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("x", src.x);
        obj.addProperty("y", src.y);
        obj.addProperty("z", src.z);
        return obj;
    }
}
