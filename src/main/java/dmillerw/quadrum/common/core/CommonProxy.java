package dmillerw.quadrum.common.core;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dmillerw.quadrum.Quadrum;
import dmillerw.quadrum.common.block.ItemBlockQuadrum;
import dmillerw.quadrum.common.block.data.BlockData;
import dmillerw.quadrum.common.block.data.BlockLoader;
import dmillerw.quadrum.common.handler.EntityDropHandler;
import dmillerw.quadrum.common.handler.FuelHandler;
import dmillerw.quadrum.common.item.data.ItemData;
import dmillerw.quadrum.common.item.data.ItemLoader;
import dmillerw.quadrum.common.lib.LanguageHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;

/**
 * @author dmillerw
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Configuration configuration = new Configuration(new File(Quadrum.configDir, "Quadrum.cfg"));
        configuration.load();

        Quadrum.textureStackTrace = configuration.get("general", "textureStackTrace", false, "Dump full stack trace upon failing to load a texture").getBoolean(false);
        Quadrum.dumpBlockMap = configuration.get("general", "dumpBlockMap", false, "Dump the stitched together block texture map to the config folder").getBoolean(false);
        Quadrum.dumpItemMap = configuration.get("general", "dumpItemMap", false, "Dump the stitched together item texture map to the config folder").getBoolean(false);

        configuration.save();

        BlockLoader.initialize();
        ItemLoader.initialize();

        LanguageHelper.loadDirectory(Quadrum.blockLangDir);
        LanguageHelper.loadDirectory(Quadrum.itemLangDir);

        for (BlockData blockData : BlockLoader.blockDataList) {
            if (blockData != null) {
                Block block = blockData.getBlockType().createBlock(blockData);
                GameRegistry.registerBlock(block, ItemBlockQuadrum.class, blockData.name);

                for (String string : blockData.oreDictionary) {
                    OreDictionary.registerOre(string, block);
                }
            }
        }

        for (ItemData itemData : ItemLoader.itemDataList) {
            if (itemData != null) {
                Item item = itemData.getItemType().createItem(itemData);
                GameRegistry.registerItem(item, itemData.name);

                for (String string : itemData.oreDictionary) {
                    OreDictionary.registerOre(string, item);
                }
            }
        }

        GameRegistry.registerFuelHandler(new FuelHandler());
        MinecraftForge.EVENT_BUS.register(new EntityDropHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        BlockLoader.verifyDrops();
    }
}
