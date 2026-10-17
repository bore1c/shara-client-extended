package dev.premium.shara.client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

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
        if (mc.player == null || mc.currentScreen != null || mc.interactionManager == null) return;

        boolean attackHeld = mc.options.attackKey.isPressed();
        if (!attackHeld) return;

        long now = System.currentTimeMillis();
        if (now - lastClick < delayMs.get()) return;

        lastClick = now;
        try {
            if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
                var entityHit = ((EntityHitResult) mc.crosshairTarget).getEntity();
                if (entityHit != null) {
                    mc.interactionManager.attackEntity(mc.player, entityHit);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    return;
                }
            }

            mc.player.swingHand(Hand.MAIN_HAND);
        } catch (Exception ignored) {
        }
    }
}
