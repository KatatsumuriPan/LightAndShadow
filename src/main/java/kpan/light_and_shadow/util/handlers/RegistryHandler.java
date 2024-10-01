package kpan.light_and_shadow.util.handlers;

import kpan.light_and_shadow.block.BlockBase;
import kpan.light_and_shadow.block.BlockInit;
import kpan.light_and_shadow.item.ItemInit;
import kpan.light_and_shadow.ModMain;
import kpan.light_and_shadow.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

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


	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		BlockBase.prepareRegistering();
		event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
	}

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Item item : ItemInit.ITEMS) {
			if (item instanceof IHasModel)
				((IHasModel) item).registerItemModels();
		}

		for (Block block : BlockInit.BLOCKS) {
			if (block instanceof IHasModel)
				((IHasModel) block).registerItemModels();
		}
	}
}
