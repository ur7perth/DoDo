package com.easykillmob;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class EasyKillScreen extends Screen {

    private ButtonWidget keyButton;
    private boolean waitingForKey = false;

    public EasyKillScreen() {
        super(Text.literal("EasyKillMob"));
    }

    @Override
    protected void init() {

        int center = this.width / 2;
        int y = this.height / 2 - 65;

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal(
                                "Status: " +
                                (AutoAttack.enabled ? "ON" : "OFF")
                        ),
                        button -> {
                            AutoAttack.toggle();
                            button.setMessage(Text.literal(
                                    "Status: " +
                                    (AutoAttack.enabled ? "ON" : "OFF")
                            ));
                        }
                ).dimensions(center - 100, y, 200, 20).build()
        );

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Speed: " + ModConfig.cps + " CPS"),
                        button -> {
                            ModConfig.cps = Math.min(
                                    20, ModConfig.cps + 1
                            );

                            ModConfig.save();

                            button.setMessage(Text.literal(
                                    "Speed: " + ModConfig.cps + " CPS"
                            ));
                        }
                ).dimensions(center - 100, y + 27, 95, 20).build()
        );

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Slower"),
                        button -> {
                            ModConfig.cps = Math.max(
                                    1, ModConfig.cps - 1
                            );

                            ModConfig.save();

                            button.setMessage(Text.literal(
                                    "Speed: " + ModConfig.cps + " CPS"
                            ));
                        }
                ).dimensions(center + 5, y + 27, 95, 20).build()
        );

        keyButton = addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Toggle Key: " +
                                GLFW.glfwGetKeyName(
                                        ModConfig.toggleKey, 0
                                )
                        ),
                        button -> {
                            waitingForKey = true;
                            button.setMessage(
                                    Text.literal("Press any key...")
                            );
                        }
                ).dimensions(center - 100, y + 54, 200, 20).build()
        );

        addDrawableChild(
                ButtonWidget.builder(
                        Text.literal("Done"),
                        button -> close()
                ).dimensions(center - 100, y + 81, 200, 20).build()
        );
    }

    @Override
    public boolean keyPressed(
            int keyCode,
            int scanCode,
            int modifiers
    ) {

        if (waitingForKey) {

            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                waitingForKey = false;

                keyButton.setMessage(
                        Text.literal("Toggle Key: " +
                                ModConfig.toggleKey)
                );

                return true;
            }

            ModKeys.setToggleKey(keyCode);

            waitingForKey = false;

            keyButton.setMessage(
                    Text.literal("Toggle Key: " + keyCode)
            );

            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta
    ) {

        renderBackground(context);

        context.drawCenteredTextWithShadow(
                textRenderer,
                Text.literal("EasyKillMob"),
                width / 2,
                height / 2 - 95,
                0xFFFFFF
        );

        context.drawCenteredTextWithShadow(
                textRenderer,
                Text.literal("Automatic Mob Attack"),
                width / 2,
                height / 2 - 82,
                0xAAAAAA
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
