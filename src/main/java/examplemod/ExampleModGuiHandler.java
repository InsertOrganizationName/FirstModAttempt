package examplemod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class ExampleModGuiHandler implements IGuiHandler {

    private static final int GUIDID_EXAMPLE_MOD = 1;

    public static int getGuiID() {
        return GUIDID_EXAMPLE_MOD;
    }


    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID != getGuiID()) {
            System.err.println(String.format("Invalid ID: expected '%s', received '%s'", getGuiID(), ID));
        }

        BlockPos blockPos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TestFurnaceTileEntity) {
            TestFurnaceTileEntity testFurnaceTileEntity = (TestFurnaceTileEntity) tileEntity;
            return new TestFurnaceContainer(player.inventory, testFurnaceTileEntity);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID != getGuiID()) {
            System.err.println(String.format("Invalid ID: expected '%s', received '%s'", getGuiID(), ID));
        }

        BlockPos blockPos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity instanceof TestFurnaceTileEntity) {
            TestFurnaceTileEntity testFurnaceTileEntity = (TestFurnaceTileEntity) tileEntity;
            return new TestFurnaceGui(player.inventory, testFurnaceTileEntity);
        }
        return null;
    }
}
