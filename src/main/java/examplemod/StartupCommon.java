package examplemod;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class StartupCommon {

    public static TestBlock testBlock;
    public static ItemBlock itemTestBlock;

    public static void preInitCommon() {
        testBlock = (TestBlock)(new TestBlock().setUnlocalizedName("example_mod_test_block"));
        testBlock.setRegistryName("example_mod_test_block");
        ForgeRegistries.BLOCKS.register(testBlock);

        itemTestBlock = new ItemBlock(testBlock);
        itemTestBlock.setRegistryName(testBlock.getRegistryName());
        ForgeRegistries.ITEMS.register(itemTestBlock);
    }

    public static void initCommon() {

    }

    public static void postInitCommon() { }
}
