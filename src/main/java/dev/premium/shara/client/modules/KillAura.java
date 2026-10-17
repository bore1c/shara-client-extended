package dev.premium.shara.client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import java.util.Comparator;
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
        if (mc.player == null || mc.world == null) return;

        double r = range.get();

        Entity target = mc.world.getEntities().stream()
                .filter(e -> e instanceof LivingEntity)
                .filter(e -> e != mc.player)
                .filter(e -> !e.isRemoved())
                .filter(e -> e.squaredDistanceTo(mc.player.getX(), mc.player.getY(), mc.player.getZ()) <= r * r)
                .min(Comparator.comparingDouble(e -> e.squaredDistanceTo(mc.player.getX(), mc.player.getY(), mc.player.getZ())))
                .orElse(null);

        if (target != null) {
            try {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
            } catch (Exception ignored) {
            }
        }
    }
}
