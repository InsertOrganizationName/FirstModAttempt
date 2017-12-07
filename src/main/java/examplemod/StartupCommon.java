package examplemod;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class StartupCommon {

    public static TestBlock testBlockBlock;
    public static ItemBlock testBlockItem;

    public static TestFurnace testFurnaceBlock;
    public static ItemBlock testFurnaceItem;

    public static void preInitCommon() {
        testBlockBlock = (TestBlock)(new TestBlock().setUnlocalizedName("example_mod_test_block"));
        testBlockBlock.setRegistryName("example_mod_test_block");
        ForgeRegistries.BLOCKS.register(testBlockBlock);

        testBlockItem = new ItemBlock(testBlockBlock);
        testBlockItem.setRegistryName(testBlockBlock.getRegistryName());
        ForgeRegistries.ITEMS.register(testBlockItem);



        testFurnaceBlock = (TestFurnace)(new TestFurnace().setUnlocalizedName("example_mod_test_furnace"));
        testFurnaceBlock.setRegistryName("example_mod_test_furnace");
        ForgeRegistries.BLOCKS.register(testFurnaceBlock);

        testFurnaceItem = new ItemBlock(testFurnaceBlock);
        testFurnaceItem.setRegistryName(testFurnaceBlock.getRegistryName());
        ForgeRegistries.ITEMS.register(testFurnaceItem);

        GameRegistry.registerTileEntity(TestFurnaceTileEntity.class, "example_mod_test_furnace_tile_entity");

        NetworkRegistry.INSTANCE.registerGuiHandler(ExampleMod.instance, GuiHandlerRegistry.getInstance());
        GuiHandlerRegistry.getInstance().registerGuiHandler(new ExampleModGuiHandler(), ExampleModGuiHandler.getGuiID());
    }

    public static void initCommon() {

    }

    public static void postInitCommon() { }
}

