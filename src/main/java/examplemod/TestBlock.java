package examplemod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TestBlock extends Block {

    public TestBlock() {
        super(Material.ROCK);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        System.out.println("~~~~~~~~~~~~~~~~  Hit me!");
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        System.out.println("Clicked!");
        Vec3d playerLookVector = playerIn.getLookVec();

        BlockPos blockToReplace = new BlockPos(pos.getX() + Math.round(playerLookVector.x), pos.getY() + Math.round(playerLookVector.y), pos.getZ() + Math.round(playerLookVector.z));

        boolean isBlockToReplaceAir = worldIn.getBlockState(blockToReplace).getBlock().getUnlocalizedName().equals("tile.air");

        if (isBlockToReplaceAir) {
            worldIn.setBlockState(blockToReplace, state);
            return true;
        } else {
            return false;
        }
    }
}
