package examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class TestFurnaceGui extends GuiContainer {

    private static final ResourceLocation texture = new ResourceLocation("examplemod", "textures/gui/example_mod_test_furnace_background.png");
    // some [x,y] coordinates of graphical elements
    private final int COOK_BAR_XPOS = 49;
    private final int COOK_BAR_YPOS = 60;
    private final int COOK_BAR_ICON_U = 0;   // texture position of white arrow icon
    private final int COOK_BAR_ICON_V = 207;
    private final int COOK_BAR_WIDTH = 80;
    private final int COOK_BAR_HEIGHT = 17;
    private final int FLAME_XPOS = 54;
    private final int FLAME_YPOS = 80;
    private final int FLAME_ICON_U = 176;   // texture position of flame icon
    private final int FLAME_ICON_V = 0;
    private final int FLAME_WIDTH = 14;
    private final int FLAME_HEIGHT = 14;
    private final int FLAME_X_SPACING = 18;
    private final TestFurnaceTileEntity testFurnaceTileEntity;


    TestFurnaceGui(InventoryPlayer inventoryPlayer, TestFurnaceTileEntity testFurnaceTileEntity) {
        super(new TestFurnaceContainer(inventoryPlayer, testFurnaceTileEntity));

        this.xSize = 176;
        this.ySize = 207;

        this.testFurnaceTileEntity = testFurnaceTileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        double cookProgress = testFurnaceTileEntity.getFractionOfCookTimeComplete();

        drawTexturedModalRect(guiLeft + COOK_BAR_XPOS, guiTop + COOK_BAR_YPOS, COOK_BAR_ICON_U, COOK_BAR_ICON_V,
                (int)(cookProgress * COOK_BAR_WIDTH), COOK_BAR_HEIGHT);

        for (int fuelSlotIndex = 0; fuelSlotIndex < TestFurnaceTileEntity.FUEL_SLOTS_COUNT; ++fuelSlotIndex) {
            double burnRemaining = testFurnaceTileEntity.getFractionOfFuelRemaining(fuelSlotIndex);
            int yOffset = (int)((1.0 - burnRemaining) * FLAME_HEIGHT);
            drawTexturedModalRect(guiLeft + FLAME_XPOS + FLAME_X_SPACING * fuelSlotIndex, guiTop + FLAME_YPOS + yOffset,
                    FLAME_ICON_U, FLAME_ICON_V + yOffset, FLAME_WIDTH, FLAME_HEIGHT - yOffset);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        final int LABEL_X_OFFSET = 5;
        final int LABEL_Y_OFFSET = 5;

        ITextComponent displayName = testFurnaceTileEntity.getDisplayName();
        if (null == displayName) {
            System.err.println("Failed to draw TestFurnaceGui string! The displayName was null.");
            return;
        }
        fontRenderer.drawString(displayName.getUnformattedText(), LABEL_X_OFFSET, LABEL_Y_OFFSET, Color.darkGray.getRGB());

        List<String> hoveringText = new ArrayList<>();

        if (isInRect(guiLeft + COOK_BAR_XPOS, guiTop + COOK_BAR_YPOS, COOK_BAR_WIDTH, COOK_BAR_HEIGHT, mouseX, mouseY)) {
            hoveringText.add("Progress:");
            int cookPercentage = (int) (testFurnaceTileEntity.getFractionOfCookTimeComplete() * 100);
            hoveringText.add(cookPercentage + "%");
        }

        for (int fuelSlotIndex = 0; fuelSlotIndex < TestFurnaceTileEntity.FUEL_SLOTS_COUNT; ++fuelSlotIndex) {
            if (isInRect(guiLeft + FLAME_XPOS + FLAME_X_SPACING * fuelSlotIndex, guiTop + FLAME_YPOS, FLAME_WIDTH, FLAME_HEIGHT, mouseX, mouseY)) {
                hoveringText.add("Fuel Time:");
                hoveringText.add(testFurnaceTileEntity.secondsOfFuelRemaining(fuelSlotIndex) + "s");
            }
        }

        if (!hoveringText.isEmpty()) {
            drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRenderer);
        }
    }

    private boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY) {
        return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
    }
}
