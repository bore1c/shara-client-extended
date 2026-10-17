package dev.premium.shara.client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;

import java.util.function.Supplier;

public class KillAura {
    private final Supplier<Boolean> enabled;
    private final Supplier<Double> range;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public KillAura(Supplier<Boolean> enabled, Supplier<Double> range) {
        this.enabled = enabled;
        this.range = range;
    }

    public void tick() {
        if (!enabled.get()) return;
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;

        double r = range.get();
        Entity target = null;
        double bestSq = Double.MAX_VALUE;

        for (Entity e : mc.world.getEntities()) {
            if (!(e instanceof LivingEntity)) continue;
            if (e == mc.player) continue;
            if (e.isRemoved()) continue;

            double sq = e.squaredDistanceTo(mc.player.getX(), mc.player.getY(), mc.player.getZ());
            if (sq <= r * r && sq < bestSq) {
                bestSq = sq;
                target = e;
            }
        }

        if (target != null) {
            try {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
            } catch (Exception ignored) {
            }
        }
    }
}
