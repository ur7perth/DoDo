package com.easykillmob;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeys {

    public static KeyBinding TOGGLE_KEY;
    public static KeyBinding MENU_KEY;

    public static void register() {

        TOGGLE_KEY = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.easykillmob.toggle",
                        InputUtil.Type.KEYSYM,
                        ModConfig.toggleKey,
                        "category.easykillmob"
                )
        );

        MENU_KEY = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.easykillmob.menu",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_V,
                        "category.easykillmob"
                )
        );
    }

    public static void setToggleKey(int keyCode) {

        ModConfig.toggleKey = keyCode;

        TOGGLE_KEY.setBoundKey(
                InputUtil.Type.KEYSYM.createFromCode(keyCode)
        );

        KeyBinding.updateKeysByCode();

        ModConfig.save();
    }
}
