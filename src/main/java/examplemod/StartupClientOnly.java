package examplemod;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class StartupClientOnly {


    public static void preInitClientOnly()
    {
        final int DEFAULT_ITEM_SUBTYPE = 0;


        // This step is necessary in order to make your block render properly when it is an item (i.e. in the inventory
        //   or in your hand or thrown on the ground).
        // It must be done on client only, and must be done after the block has been created in Common.preinit().
        ModelResourceLocation testBlockItemModelResourceLocation = new ModelResourceLocation("examplemod:example_mod_test_block", "inventory");
        ModelLoader.setCustomModelResourceLocation(StartupCommon.testBlockItem, DEFAULT_ITEM_SUBTYPE, testBlockItemModelResourceLocation);

        ModelResourceLocation testFurnaceItemModelResourceLocation = new ModelResourceLocation("examplemod:example_mod_test_furnace", "inventory");
        ModelLoader.setCustomModelResourceLocation(StartupCommon.testFurnaceItem, DEFAULT_ITEM_SUBTYPE, testFurnaceItemModelResourceLocation);
    }

    public static void initClientOnly()
    {
    }

    public static void postInitClientOnly()
    {
    }
}
