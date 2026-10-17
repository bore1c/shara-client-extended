package dev.premium.shara.client;

import com.google.gson.Gson;
import dev.premium.shara.client.config.Config;
import dev.premium.shara.client.modules.AutoClicker;
import dev.premium.shara.client.modules.KillAura;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class SharaClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("shara-client-extended");
    public static final MinecraftClient MC = MinecraftClient.getInstance();

    private Config config;

    private KeyBinding keyToggleESP;
    private KeyBinding keyToggleKillaura;
    private KeyBinding keyToggleXray;
    private KeyBinding keyToggleAutoArmor;
    private KeyBinding keyToggleAutoClicker;

    private KillAura killaura;
    private AutoClicker autoClicker;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Shara Client Extended...");

        config = Config.load();

        keyToggleESP = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("key.shara.esp", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "key.category.shara-client"));
        keyToggleKillaura = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("key.shara.killaura", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "key.category.shara-client"));
        keyToggleXray = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("key.shara.xray", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "key.category.shara-client"));
        keyToggleAutoArmor = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("key.shara.autoarmor", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "key.category.shara-client"));
        keyToggleAutoClicker = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("key.shara.autoclicker", InputUtil.Type.KEYSYM, InputUtil.UNKNOWN_KEY.getCode(), "key.category.shara-client"));

        killaura = new KillAura(() -> config.killaura, () -> config.killauraRange);
        autoClicker = new AutoClicker(() -> config.autoClicker, () -> config.autoClickDelayMs);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyToggleESP.wasPressed()) {
                config.esp = !config.esp;
                config.save();
            }
            if (keyToggleKillaura.wasPressed()) {
                config.killaura = !config.killaura;
                config.save();
            }
            if (keyToggleXray.wasPressed()) {
                config.xray = !config.xray;
                config.save();
            }
            if (keyToggleAutoArmor.wasPressed()) {
                config.autoArmor = !config.autoArmor;
                config.save();
            }
            if (keyToggleAutoClicker.wasPressed()) {
                config.autoClicker = !config.autoClicker;
                config.save();
            }

            killaura.tick();
            autoClicker.tick();
        });

        HudRenderCallback.EVENT.register((drawContext, tick) -> {
            if (MC.player == null || MC.currentScreen != null) return;

            int y = 10;
            drawContext.drawTextWithShadow(MC.textRenderer, "Shara Client Extended", 10, y, 0x00FF00);
            y += 12;

            if (config.esp) {
                drawContext.drawTextWithShadow(MC.textRenderer, "ESP (nearby):", 10, y, 0xFFFFFF);
                y += 10;
                if (MC.world != null) {
                    MC.world.getEntities().forEach(entity -> {
                        if (entity == MC.player) return;
                        String name = entity.getEntityName();
                        double dist = entity.squaredDistanceTo(MC.player.getX(), MC.player.getY(), MC.player.getZ());
                        String line = String.format("%s - %.1fm", name, Math.sqrt(dist));
                        drawContext.drawTextWithShadow(MC.textRenderer, line, 12, y, 0xFFFF00);
                        y += 10;
                    });
                }
            }

            y += 6;
            drawContext.drawTextWithShadow(MC.textRenderer, "Modules:", 10, y, 0x00FFFF);
            y += 10;
            if (config.killaura) drawContext.drawTextWithShadow(MC.textRenderer, "Killaura: ON", 12, y, 0x00FF00); else drawContext.drawTextWithShadow(MC.textRenderer, "Killaura: OFF", 12, y, 0xFF0000);
            y += 10;
            if (config.autoClicker) drawContext.drawTextWithShadow(MC.textRenderer, "AutoClicker: ON", 12, y, 0x00FF00); else drawContext.drawTextWithShadow(MC.textRenderer, "AutoClicker: OFF", 12, y, 0xFF0000);
            y += 10;
            if (config.xray) drawContext.drawTextWithShadow(MC.textRenderer, "X-Ray: ON", 12, y, 0x00FF00); else drawContext.drawTextWithShadow(MC.textRenderer, "X-Ray: OFF", 12, y, 0xFF0000);
            y += 10;
            if (config.autoArmor) drawContext.drawTextWithShadow(MC.textRenderer, "AutoArmor: ON", 12, y, 0x00FF00); else drawContext.drawTextWithShadow(MC.textRenderer, "AutoArmor: OFF", 12, y, 0xFF0000);

        });

        LOGGER.info("Initialized. Config path: {}", Config.getConfigFile().getAbsolutePath());
    }
}
