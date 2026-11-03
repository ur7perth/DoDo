package com.easykillmob;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.Hand;

public class AutoAttack {

    public static boolean enabled = false;

    private static float accumulator = 0f;
    private static MobEntity lastTarget = null;

    public static void toggle() {
        enabled = !enabled;
        accumulator = 0f;
        lastTarget = null;
    }

    public static void disable() {
        enabled = false;
        accumulator = 0f;
        lastTarget = null;
    }

    public static void tick(MinecraftClient client) {

        if (!enabled) return;

        if (client.player == null || client.world == null) {
            disable();
            return;
        }

        if (!client.player.isAlive()) {
            disable();
            return;
        }

        if (client.player.isUsingItem()) {
            return;
        }

        if (lastTarget != null && !lastTarget.isAlive()) {
            disable();
            return;
        }

        if (!(client.crosshairTarget instanceof EntityHitResult hit)) {
            lastTarget = null;
            accumulator = 0f;
            return;
        }

        Entity entity = hit.getEntity();

        if (!(entity instanceof MobEntity mob)) {
            lastTarget = null;
            accumulator = 0f;
            return;
        }

        if (!mob.isAlive() || mob.isRemoved()) {
            return;
        }

        if (lastTarget != mob) {
            lastTarget = mob;
            accumulator = 0f;
        }

        accumulator = Math.min(
                1f,
                accumulator + ModConfig.cps / 20f
        );

        if (accumulator >= 1f &&
                client.player.getAttackCooldownProgress(0f) >= 1f) {

            accumulator = 0f;

            client.interactionManager.attackEntity(
                    client.player,
                    mob
            );

            client.player.swingHand(Hand.MAIN_HAND);
        }
    }
}
