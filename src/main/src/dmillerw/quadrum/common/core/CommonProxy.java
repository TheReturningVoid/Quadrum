package dmillerw.quadrum.common.core;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dmillerw.quadrum.Quadrum;
import dmillerw.quadrum.common.block.BlockQuadrum;
import dmillerw.quadrum.common.block.ItemBlockQuadrum;
import dmillerw.quadrum.common.block.data.BlockData;
import dmillerw.quadrum.common.block.data.BlockLoader;
import dmillerw.quadrum.common.item.ItemQuadrum;
import dmillerw.quadrum.common.item.data.ItemData;
import dmillerw.quadrum.common.item.data.ItemLoader;
import dmillerw.quadrum.common.lib.LanguageHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

/**
 * @author dmillerw
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        BlockLoader.initialize();
        ItemLoader.initialize();

        LanguageHelper.loadDirectory(Quadrum.blockLangDir);
        LanguageHelper.loadDirectory(Quadrum.itemLangDir);

        for (BlockData blockData : BlockLoader.blocks) {
            if (blockData != null) {
                Block block = new BlockQuadrum(blockData);
                GameRegistry.registerBlock(block, ItemBlockQuadrum.class, blockData.name);
            }
        }

        for (ItemData itemData : ItemLoader.items) {
            if (itemData != null) {
                Item item = new ItemQuadrum(itemData);
                GameRegistry.registerItem(item, itemData.name);
            }
        }

        GameRegistry.registerFuelHandler(new FuelHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        BlockLoader.verifyDrops();
    }
}
