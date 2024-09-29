package kpan.light_and_shadow.util.handlers;

import kpan.light_and_shadow.ModMain;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@EventBusSubscriber
public class RegistryHandler {

    public static void preInitRegistries(@SuppressWarnings("unused") FMLPreInitializationEvent event) {
        ModMain.proxy.registerOnlyClient();
    }

    public static void initRegistries() {
    }

    public static void postInitRegistries() {
    }

    public static void serverRegistries(@SuppressWarnings("unused") FMLServerStartingEvent event) {
    }

}
