package gmod;

import gmod.particle.FlameParticle;
import gmod.particle.LineParticle;
import insertorgname.anvil.utils.Position;
import insertorgname.anvil.utils.Velocity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.stream.Collectors;

public class KeyBindingHandler {

    //TODO TL 2018-02-09 : Use the param
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInputEvent(KeyInputEvent keyInputEvent) {
        ClientOnlyProxy.keyBindings.forEach(keyBinding -> {
            if (keyBinding.isPressed()) {
                System.out.println(String.format("Key Binding Pressed: %s", keyBinding.getKeyDescription()));

                EntityPlayerSP player = Minecraft.getMinecraft().player;

                Minecraft.getMinecraft().effectRenderer.addEffect(
                        FlameParticle.of(player.world,
                                Position.of(player.getPositionEyes(0)),
                                Velocity.of(player.getLookVec()))
                );

                List<EntityItem> entitiesWithinAABB = player.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(
                        player.getPosition().getX() - 5, player.getPosition().getY() - 5, player.getPosition().getZ() - 5,
                        player.getPosition().getX() + 5, player.getPosition().getY() + 5, player.getPosition().getZ() + 5
                ));


                System.out.println(entitiesWithinAABB.size());


                List<EntityItem> ironIngotItems = entitiesWithinAABB.stream()
                        .filter(entity -> entity.getItem().getItem().getRegistryName() == Items.IRON_INGOT.getRegistryName())
                        .collect(Collectors.toList());

                ironIngotItems.forEach(entityItem -> {
                    Minecraft.getMinecraft().effectRenderer.addEffect(
                            LineParticle.of(player.world,
                                    player,
                                    entityItem)
                    );
                });
            }
        });
    }
}
