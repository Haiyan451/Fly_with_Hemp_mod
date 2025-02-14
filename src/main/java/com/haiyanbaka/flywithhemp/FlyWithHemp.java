package com.haiyanbaka.flywithhemp;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(FlyWithHemp.MODID)
public class FlyWithHemp {
    public static final String MODID = "flywithhemp";
    private static final Logger LOGGER = LogManager.getLogger();

    public FlyWithHemp() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("FlyWithHemp is loading!");
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);

        // Check if main hand is hemp and off hand is flint and steel
        if (mainHand.getItem().getRegistryName().toString().equals("immersiveengineering:hemp") &&
            offHand.getItem() == Items.FLINT_AND_STEEL) {

            // Consume one hemp
            mainHand.shrink(1);

            // Apply levitation effect
            player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 600, 0)); // 30 seconds

            // 20% chance to reduce health and apply slowness
            if (Math.random() < 0.2) {
                player.getAttributes().getInstance(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(player.getMaxHealth() - 1);
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 0)); // 30 seconds
            }

            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }
}
