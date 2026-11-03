package com.easykillmob;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;

public class AutoAttack {

    public static boolean enabled = false;

    private static int timer = 0;

    private static MobEntity lastTarget = null;

    public static void toggle() {

        enabled = !enabled;

        timer = 0;
        lastTarget = null;
    }

    public static void disable() {

        enabled = false;

        timer = 0;
        lastTarget = null;
    }

    public static void tick(MinecraftClient client) {

        if (!enabled) {
            return;
        }

        if (client.player == null || client.world == null) {
            disable();
            return;
        }

        // Stop after player death
        if (client.player.isDead()) {
            disable();
            return;
        }

        // Do not attack while using an item
        if (client.player.isUsingItem()) {
            return;
        }

        // Stop when previous target dies
        if (lastTarget != null && !lastTarget.isAlive()) {
            disable();
            return;
        }

        // Check crosshair target
        if (!(client.crosshairTarget instanceof EntityHitResult hit)) {
            lastTarget = null;
            timer = 0;
            return;
        }

        Entity entity = hit.getEntity();

        if (!(entity instanceof MobEntity mob)) {
            lastTarget = null;
            timer = 0;
            return;
        }

        if (!mob.isAlive() || mob.isRemoved()) {
            return;
        }

        if (lastTarget != mob) {
            lastTarget = mob;
            timer = 0;
        }

        timer++;

        int delay = Math.max(1, 20 / ModConfig.cps);

        if (timer >= delay) {

            timer = 0;

            // Respect Minecraft's normal attack cooldown
            if (client.player.getAttackCooldownProgress(0.0F) >= 1.0F) {

                // Perform the normal client attack action
                client.doAttack();
            }
        }
    }
}
