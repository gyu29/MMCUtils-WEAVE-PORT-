package me.redth.mmcutils

import net.weavemc.loader.api.ModInitializer
import net.weavemc.loader.api.event.EventBus
import net.weavemc.loader.api.event.KeyboardEvent
import net.weavemc.loader.api.event.RenderGameOverlayEvent
import me.redth.mmcutils.core.Core
import me.redth.mmcutils.mixins.BlockColorsMixin
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import org.lwjgl.input.Keyboard

class MMCUtilsMod : ModInitializer {
    override fun preInit() {
        println("Initializing MMCUtilsMod!")

        EventBus.subscribe(KeyboardEvent::class.java) { e ->
            if (Minecraft.getMinecraft().currentScreen == null && e.keyState) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                    ChatComponentText("Key Pressed: " + Keyboard.getKeyName(e.keyCode))
                )
            }
        }

        EventBus.subscribe(RenderGameOverlayEvent.Post::class.java, Core::preventTablist)
        EventBus.subscribe(RenderGameOverlayEvent.Post::class.java, Core::renderHeightLimit)
    }
}
