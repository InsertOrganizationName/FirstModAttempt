package examplemod;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TestFurnace extends BlockContainer {

    public static final PropertyInteger BURNING_SIDES_COUNT = PropertyInteger.create("burning_sides_count", 0, 4);

    // change the furnace emitted light ("block light") depending on how many slots are burning
    private static final int FOUR_SIDE_LIGHT_VALUE = 15; // light value for four sides burning
    private static final int ONE_SIDE_LIGHT_VALUE = 8;  // light value for a single side burning

    public TestFurnace() {
        super(Material.ROCK);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TestFurnaceTileEntity();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        System.out.println("~~~~~>  Furnace Activated!");
        if (worldIn.isRemote) {
            return true;
        }

        playerIn.openGui(ExampleMod.instance, ExampleModGuiHandler.getGuiID(), worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {

        TileEntity tileEntity = worldIn.getTileEntity(pos);


        if (tileEntity instanceof TestFurnaceTileEntity) {
            TestFurnaceTileEntity tileInventoryFurnace = (TestFurnaceTileEntity) tileEntity;
            int burningSlots = tileInventoryFurnace.numberOfBurningFuelSlots();
            burningSlots = MathHelper.clamp(burningSlots, 0, 4);
            return getDefaultState().withProperty(BURNING_SIDES_COUNT, burningSlots);
        }
        return state;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
        // return this.getDefaultState().withProperty(BURNING_SIDES_COUNT, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
        // return ((Integer)state.getValue(BURNING_SIDES_COUNT)).intValue();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BURNING_SIDES_COUNT);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState blockState = getActualState(getDefaultState(), world, pos);
        int burningSides = blockState.getValue(BURNING_SIDES_COUNT);

        int lightValue;
        if (burningSides == 0) {
            lightValue = 0;
        } else {
            // linearly interpolate the light value depending on how many slots are burning
            lightValue = ONE_SIDE_LIGHT_VALUE + (int)((FOUR_SIDE_LIGHT_VALUE - ONE_SIDE_LIGHT_VALUE) / (4.0 - 1.0) * burningSides);
        }
        return MathHelper.clamp(lightValue, 0, FOUR_SIDE_LIGHT_VALUE);
    }

    // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    // used by the renderer to control lighting and visibility of other blocks.
    // set to false because this block doesn't fill the entire 1x1x1 space
    @Override
    public boolean isOpaqueCube(IBlockState iBlockState) {
        return false;
    }

    // used by the renderer to control lighting and visibility of other blocks, also by
    // (eg) wall or fence to control whether the fence joins itself to this block
    // set to false because this block doesn't fill the entire 1x1x1 space
    @Override
    public boolean isFullCube(IBlockState iBlockState) {
        return false;
    }

    // render using a BakedModel
    // required because the default (super method) is INVISIBLE for BlockContainers.
    @Override
    public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
        return EnumBlockRenderType.MODEL;
    }

}
