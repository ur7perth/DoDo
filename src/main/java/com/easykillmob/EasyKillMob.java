package com.easykillmob;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class EasyKillMob implements ClientModInitializer {

    public static final String MOD_ID = "easykillmob";

    @Override
    public void onInitializeClient() {

        ModConfig.load();
        ModKeys.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (ModKeys.TOGGLE_KEY.wasPressed()) {
                AutoAttack.toggle();
            }

            while (ModKeys.MENU_KEY.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new EasyKillScreen());
                }
            }

            AutoAttack.tick(client);
        });
    }
}
