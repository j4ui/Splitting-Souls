package j4ui.dev.splittingSouls;
import net.fabricmc.api.ModInitializer;
import java.io.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplittingSouls implements ModInitializer {
    public static final String MOD_ID = "splittingsouls";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.initialize();
        LOGGER.info("Splitting Souls Items Initialized!");

    }
}
