package examplemod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class GuiHandlerRegistry implements IGuiHandler {

    private Map<Integer, IGuiHandler> registeredHandlers;
    private static GuiHandlerRegistry guiHandlerRegistry = new GuiHandlerRegistry();

    public GuiHandlerRegistry() {
        registeredHandlers = new HashMap<>();
    }

    public void registerGuiHandler(IGuiHandler handler, int guiId) {
        registeredHandlers.put(guiId, handler);
    }

    public static GuiHandlerRegistry getInstance() {
        return guiHandlerRegistry;
    }


    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        IGuiHandler iGuiHandler = registeredHandlers.get(ID);
        return (iGuiHandler != null) ? iGuiHandler.getServerGuiElement(ID, player, world, x, y, z) : null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        IGuiHandler iGuiHandler = registeredHandlers.get(ID);
        return (iGuiHandler != null) ? iGuiHandler.getClientGuiElement(ID, player, world, x, y, z) : null;
    }
}
