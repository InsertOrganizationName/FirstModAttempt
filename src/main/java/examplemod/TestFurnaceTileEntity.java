package examplemod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumSkyBlock;

import javax.annotation.Nullable;
import java.util.Arrays;

public class TestFurnaceTileEntity extends TileEntity implements IInventory, ITickable {
    // Create and initialize the itemStacks variable that will store store the itemStacks
    public static final int FUEL_SLOTS_COUNT = 4;
    public static final int INPUT_SLOTS_COUNT = 5;
    public static final int OUTPUT_SLOTS_COUNT = 5;
    public static final int TOTAL_SLOTS_COUNT = FUEL_SLOTS_COUNT + INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

    public static final int FIRST_FUEL_SLOT = 0;
    public static final int FIRST_INPUT_SLOT = FIRST_FUEL_SLOT + FUEL_SLOTS_COUNT;
    public static final int FIRST_OUTPUT_SLOT = FIRST_INPUT_SLOT + INPUT_SLOTS_COUNT;
    private static final int NUM_TICKS_PER_SECOND = 20;

    /** The number of burn ticks remaining on the current piece of fuel */
    private int [] burnTimeRemaining = new int[FUEL_SLOTS_COUNT];
    /** The initial fuel value of the currently burning fuel (in ticks of burn duration) */
    private int [] burnTimeInitialValue = new int[FUEL_SLOTS_COUNT];

    /**The number of ticks the current item has been cooking*/
    private short cookTime;

    /**The number of ticks required to cook an item*/
    private static final short COOK_TIME_FOR_COMPLETION = 200;  // vanilla value is 200 = 10 seconds

    private int cachedNumberOfBurningSlots = -1;

    private ItemStack[] itemStacks;


    private static final byte COOK_FIELD_ID = 0;
    private static final byte FIRST_BURN_TIME_REMAINING_FIELD_ID = 1;
    private static final byte FIRST_BURN_TIME_INITIAL_FIELD_ID = FIRST_BURN_TIME_REMAINING_FIELD_ID + (byte)FUEL_SLOTS_COUNT;
    private static final byte NUMBER_OF_FIELDS = FIRST_BURN_TIME_INITIAL_FIELD_ID + (byte)FUEL_SLOTS_COUNT;


    public TestFurnaceTileEntity() {
        itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];
        clear();
    }

    @Override
    public void update() {
        if (canSmelt()) {
            int numberOfFuelBurning = burnFuel();

            // If fuel is available, keep cooking the item, otherwise start "uncooking" it at double speed
            if (numberOfFuelBurning > 0) {
                cookTime += numberOfFuelBurning;
            } else {
                cookTime -= 2;
            }

            if (cookTime < 0) cookTime = 0;

            // If cookTime has reached maxCookTime smelt the item and reset cookTime
            if (cookTime >= COOK_TIME_FOR_COMPLETION) {
                smeltItem();
                cookTime = 0;
            }
        } else {
            cookTime = 0;
        }

        // when the number of burning slots changes, we need to force the block to re-render, otherwise the change in
        //   state will not be visible.  Likewise, we need to force a lighting recalculation.
        // The block update (for renderer) is only required on client side, but the lighting is required on both, since
        //    the client needs it for rendering and the server needs it for crop growth etc
        int numberBurning = numberOfBurningFuelSlots();
        if (cachedNumberOfBurningSlots != numberBurning) {
            cachedNumberOfBurningSlots = numberBurning;
            if (world.isRemote) {
                IBlockState iblockstate = this.world.getBlockState(pos);
                final int FLAGS = 3;  // I'm not sure what these flags do, exactly.
                world.notifyBlockUpdate(pos, iblockstate, iblockstate, FLAGS);
            }
            world.checkLightFor(EnumSkyBlock.BLOCK, pos);
        }
    }

    private void smeltItem() {
        smeltItem(true);
    }

    private boolean canSmelt() {return smeltItem(false);}

    private boolean smeltItem(boolean performSmelt)
    {

        boolean havePerformedSmelting = false;

        // finds the first input slot which is smeltable and whose result fits into an output slot (stacking if possible)
        for (int inputSlot = FIRST_INPUT_SLOT; inputSlot < FIRST_INPUT_SLOT + INPUT_SLOTS_COUNT; inputSlot++)	{
            if (!itemStacks[inputSlot].isEmpty()) {  //isEmpty()

                ItemStack result = getSmeltingResultForItem(itemStacks[inputSlot]);  //EMPTY_ITEM

                Integer outputSlot = getBestOutputSlot(result);

                if (outputSlot == null) {
                    continue;
                }

                if (!performSmelt) {
                    return true;
                }
                itemStacks[inputSlot].shrink(1);  // decreaseStackSize()
                if (itemStacks[inputSlot].getCount() <= 0) {
                    itemStacks[inputSlot] = ItemStack.EMPTY;  //getStackSize(), EmptyItem
                }
                if (itemStacks[outputSlot].isEmpty()) {  // isEmpty()
                    itemStacks[outputSlot] = result.copy(); // Use deep .copy() to avoid altering the recipe
                } else {
                    int newStackSize = itemStacks[outputSlot].getCount() + result.getCount();
                    itemStacks[outputSlot].setCount(newStackSize) ;  //setStackSize(), getStackSize()
                }
                havePerformedSmelting = true;
            }
        }
        if (havePerformedSmelting) {
            markDirty();
        }
        return havePerformedSmelting;
    }

    private Integer getBestOutputSlot(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return null;
        }

        for (int fuelSlot = FIRST_FUEL_SLOT; fuelSlot < FIRST_FUEL_SLOT + FUEL_SLOTS_COUNT; fuelSlot++) {
            ItemStack fuelStack = itemStacks[fuelSlot];

            if (fuelStack.getItem() == itemStack.getItem() && (!fuelStack.getHasSubtypes() || fuelStack.getMetadata() == fuelStack.getMetadata())
                    && ItemStack.areItemStackTagsEqual(fuelStack, itemStack)) {
                int combinedSize = itemStacks[fuelSlot].getCount() + itemStack.getCount();  //getStackSize()
                if (combinedSize <= getInventoryStackLimit() && combinedSize <= itemStacks[fuelSlot].getMaxStackSize()) {
                    return fuelSlot;
                }
            }
        }

        Integer firstEmptySlotOrNull = null;

        for (int outputSlot = FIRST_OUTPUT_SLOT; outputSlot < FIRST_OUTPUT_SLOT + OUTPUT_SLOTS_COUNT; outputSlot++) {
            ItemStack outputStack = itemStacks[outputSlot];

            if (outputStack.isEmpty() && firstEmptySlotOrNull == null) {
                firstEmptySlotOrNull = outputSlot;
            }

            if (outputStack.getItem() == itemStack.getItem() && (!outputStack.getHasSubtypes() || outputStack.getMetadata() == outputStack.getMetadata())
                    && ItemStack.areItemStackTagsEqual(outputStack, itemStack)) {
                int combinedSize = itemStacks[outputSlot].getCount() + itemStack.getCount();  //getStackSize()
                if (combinedSize <= getInventoryStackLimit() && combinedSize <= itemStacks[outputSlot].getMaxStackSize()) {
                    return outputSlot;
                }
            }
        }
        return firstEmptySlotOrNull;
    }

    public static ItemStack getSmeltingResultForItem(ItemStack sourceStack) {
        return FurnaceRecipes.instance().getSmeltingResult(sourceStack);
    }

    public static short getItemBurnTime(ItemStack sourceStack) {
        int burntime = TileEntityFurnace.getItemBurnTime(sourceStack);  // just use the vanilla values
        return (short)MathHelper.clamp(burntime, 0, Short.MAX_VALUE);
    }

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : itemStacks) {
            if (!itemstack.isEmpty()) {  // isEmpty()
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        return itemStacks[slotIndex];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int count) {
        ItemStack itemStackInSlot = getStackInSlot(slotIndex);
        if (itemStackInSlot.isEmpty()) return ItemStack.EMPTY;  //isEmpty(), EMPTY_ITEM

        ItemStack itemStackRemoved;
        if (itemStackInSlot.getCount() <= count) { //getStackSize
            itemStackRemoved = itemStackInSlot;
            setInventorySlotContents(slotIndex, ItemStack.EMPTY); // EMPTY_ITEM
        } else {
            itemStackRemoved = itemStackInSlot.splitStack(count);
            if (itemStackInSlot.getCount() == 0) { //getStackSize
                setInventorySlotContents(slotIndex, ItemStack.EMPTY); //EMPTY_ITEM
            }
        }
        markDirty();
        return itemStackRemoved;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemStack) {

        itemStacks[slotIndex] = itemStack;
        if (!itemStack.isEmpty() && itemStack.getCount() > getInventoryStackLimit()) {  // isEmpty();  getStackSize()
            itemStack.setCount(getInventoryStackLimit());  //setStackSize()
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        }
        final double X_CENTRE_OFFSET = 0.5;
        final double Y_CENTRE_OFFSET = 0.5;
        final double Z_CENTRE_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
        return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    public static boolean isItemValidForFuelSlot(ItemStack stack) {
        return true;
    }

    public static boolean isItemValidForInputSlot(ItemStack stack) {
        return true;
    }

    public static boolean isItemValidForOutputSlot(ItemStack stack) {
        return false;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound) {
        super.writeToNBT(parentNBTTagCompound); // The super call is required to save and load the tiles location

        // Save the stored item stacks

        // to use an analogy with Java, this code generates an array of hashmaps
        // The itemStack in each slot is converted to an NBTTagCompound, which is effectively a hashmap of key->value pairs such
        //   as slot=1, id=2353, count=1, etc
        // Each of these NBTTagCompound are then inserted into NBTTagList, which is similar to an array.
        NBTTagList dataForAllSlots = new NBTTagList();
        for (int i = 0; i < this.itemStacks.length; ++i) {
            if (!this.itemStacks[i].isEmpty()) {  //isEmpty()
                NBTTagCompound dataForThisSlot = new NBTTagCompound();
                dataForThisSlot.setByte("Slot", (byte) i);
                this.itemStacks[i].writeToNBT(dataForThisSlot);
                dataForAllSlots.appendTag(dataForThisSlot);
            }
        }
        // the array of hashmaps is then inserted into the parent hashmap for the container
        parentNBTTagCompound.setTag("Items", dataForAllSlots);

        // Save everything else
        parentNBTTagCompound.setShort("CookTime", cookTime);
        parentNBTTagCompound.setTag("burnTimeRemaining", new NBTTagIntArray(burnTimeRemaining));
        parentNBTTagCompound.setTag("burnTimeInitial", new NBTTagIntArray(burnTimeInitialValue));
        return parentNBTTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound); // The super call is required to save and load the tiles location

        final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing

        NBTTagList dataForAllSlots = nbtTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

        Arrays.fill(itemStacks, ItemStack.EMPTY);           // set all slots to empty EMPTY_ITEM
        for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
            NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
            byte slotNumber = dataForOneSlot.getByte("Slot");
            if (slotNumber >= 0 && slotNumber < this.itemStacks.length) {
                this.itemStacks[slotNumber] = new ItemStack(dataForOneSlot);
            }
        }

        // Load everything else.  Trim the arrays (or pad with 0) to make sure they have the correct number of elements
        cookTime = nbtTagCompound.getShort("CookTime");
        burnTimeRemaining = Arrays.copyOf(nbtTagCompound.getIntArray("burnTimeRemaining"), FUEL_SLOTS_COUNT);
        burnTimeInitialValue = Arrays.copyOf(nbtTagCompound.getIntArray("burnTimeInitial"), FUEL_SLOTS_COUNT);
        cachedNumberOfBurningSlots = -1;
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound updateTagDescribingTileEntityState = getUpdateTag();
        final int METADATA = 0;
        return new SPacketUpdateTileEntity(this.pos, METADATA, updateTagDescribingTileEntityState);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound updateTagDescribingTileEntityState = pkt.getNbtCompound();
        handleUpdateTag(updateTagDescribingTileEntityState);
    }
    /* Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
       Warning - although our getUpdatePacket() uses this method, vanilla also calls it directly, so don't remove it.
     */

    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }
    /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
     Warning - although our onDataPacket() uses this method, vanilla also calls it directly, so don't remove it.
   */

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }

    @Override
    public void clear() {
        Arrays.fill(itemStacks, ItemStack.EMPTY);
    }

    @Override
    public String getName() {
        return "container.example_mod_test_furnace.name";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
    // standard code to look up what the human-readable name is

    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    @Override
    public int getField(int id) {
        if (id == COOK_FIELD_ID) return cookTime;
        if (id >= FIRST_BURN_TIME_REMAINING_FIELD_ID && id < FIRST_BURN_TIME_REMAINING_FIELD_ID + FUEL_SLOTS_COUNT) {
            return burnTimeRemaining[id - FIRST_BURN_TIME_REMAINING_FIELD_ID];
        }
        if (id >= FIRST_BURN_TIME_INITIAL_FIELD_ID && id < FIRST_BURN_TIME_INITIAL_FIELD_ID + FUEL_SLOTS_COUNT) {
            return burnTimeInitialValue[id - FIRST_BURN_TIME_INITIAL_FIELD_ID];
        }
        System.err.println("Invalid field ID in TileInventorySmelting.getField:" + id);
        return 0;
    }

    @Override
    public void setField(int id, int value) {

        if (id == COOK_FIELD_ID) {
            cookTime = (short)value;
        } else if (id >= FIRST_BURN_TIME_REMAINING_FIELD_ID && id < FIRST_BURN_TIME_REMAINING_FIELD_ID + FUEL_SLOTS_COUNT) {
            burnTimeRemaining[id - FIRST_BURN_TIME_REMAINING_FIELD_ID] = value;
        } else if (id >= FIRST_BURN_TIME_INITIAL_FIELD_ID && id < FIRST_BURN_TIME_INITIAL_FIELD_ID + FUEL_SLOTS_COUNT) {
            burnTimeInitialValue[id - FIRST_BURN_TIME_INITIAL_FIELD_ID] = value;
        } else {
            System.err.println("Invalid field ID in TileInventorySmelting.setField:" + id);
        }
    }

    @Override
    public int getFieldCount() {
        return NUMBER_OF_FIELDS;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack removeStackFromSlot(int slotIndex) {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (!itemStack.isEmpty()) setInventorySlotContents(slotIndex, ItemStack.EMPTY);  //isEmpty();  EMPTY_ITEM
        return itemStack;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    public double getFractionOfCookTimeComplete() {
        double fraction = cookTime / (double)COOK_TIME_FOR_COMPLETION;
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

    public int secondsOfFuelRemaining(int fuelSlotIndex) {
        if (burnTimeRemaining[fuelSlotIndex] <= 0) {
            return 0;
        }
        return burnTimeRemaining[fuelSlotIndex] / NUM_TICKS_PER_SECOND;
    }

    public double getFractionOfFuelRemaining(int fuelSlotIndex) {
        if (burnTimeInitialValue[fuelSlotIndex] <= 0 ) {
            return 0;
        }
        double fraction = burnTimeRemaining[fuelSlotIndex] / (double)burnTimeInitialValue[fuelSlotIndex];
        return MathHelper.clamp(fraction, 0.0, 1.0);
    }

    public int numberOfBurningFuelSlots() {
        int burningCount = 0;
        for (int burnTime : burnTimeRemaining) {
            if (burnTime > 0) ++burningCount;
        }
        return burningCount;
    }

    private int burnFuel() {
        int burningCount = 0;
        boolean inventoryChanged = false;
        // Iterate over all the fuel slots
        for (int i = 0; i < FUEL_SLOTS_COUNT; i++) {
            int fuelSlotNumber = i + FIRST_FUEL_SLOT;
            if (burnTimeRemaining[i] > 0) {
                --burnTimeRemaining[i];
                ++burningCount;
            }
            if (burnTimeRemaining[i] == 0) {
                if (!itemStacks[fuelSlotNumber].isEmpty() && getItemBurnTime(itemStacks[fuelSlotNumber]) > 0) {  // isEmpty()
                    // If the stack in this slot is not null and is fuel, set burnTimeRemaining & burnTimeInitialValue to the
                    // item's burn time and decrease the stack size
                    burnTimeRemaining[i] = burnTimeInitialValue[i] = getItemBurnTime(itemStacks[fuelSlotNumber]);
                    itemStacks[fuelSlotNumber].shrink(1);  // decreaseStackSize()
                    ++burningCount;
                    inventoryChanged = true;
                    // If the stack size now equals 0 set the slot contents to the items container item. This is for fuel
                    // items such as lava buckets so that the bucket is not consumed. If the item dose not have
                    // a container item getContainerItem returns null which sets the slot contents to null
                    if (itemStacks[fuelSlotNumber].getCount() == 0) {  //getStackSize()
                        itemStacks[fuelSlotNumber] = itemStacks[fuelSlotNumber].getItem().getContainerItem(itemStacks[fuelSlotNumber]);
                    }
                }
            }
        }
        if (inventoryChanged) markDirty();
        return burningCount;
    }
}
