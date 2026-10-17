package dev.premium.shara.client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Hand;

import java.util.function.Supplier;

public class AutoClicker {
    private final Supplier<Boolean> enabled;
    private final Supplier<Integer> delayMs;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    private long lastClick = 0;

    public AutoClicker(Supplier<Boolean> enabled, Supplier<Integer> delayMs) {
        this.enabled = enabled;
        this.delayMs = delayMs;
    }

    public void tick() {
        if (!enabled.get()) return;
        if (mc.player == null || mc.currentScreen != null) return;

        boolean attackHeld = mc.options.attackKey.isPressed();
        if (!attackHeld) return;

        long now = System.currentTimeMillis();
        if (now - lastClick < delayMs.get()) return;

        lastClick = now;
        try {
            // swing and perform attack on the entity under crosshair if any
            if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == net.minecraft.client.render.Camera.PositionedEntityType.ENTITY) {
                // try to attack the targeted entity
                var entityHit = mc.crosshairTarget.entity;
                if (entityHit != null) {
                    mc.interactionManager.attackEntity(mc.player, entityHit);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    return;
                }
            }
            // otherwise, just swing hand (left-click in air)
            mc.player.swingHand(Hand.MAIN_HAND);
        } catch (Exception ignored) {
        }
    }
}
