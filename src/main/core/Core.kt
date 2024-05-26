package me.redth.mmcutils.core

import cc.polyfrost.oneconfig.utils.dsl.mc
import me.redth.mmcutils.config.ModConfig
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.weavemc.loader.api.event.ClientChatReceivedEvent
import net.weavemc.loader.api.event.RenderGameOverlayEvent
import net.weavemc.loader.api.event.SubscribeEvent

object Core {
    private val ALL_PROXY = listOf("AS Practice", "EU Practice", "NA Practice", "SA Practice")
    private val BRIDGING_GAMES = listOf("Bed Fight", "Fireball Fight", "Bridges", "Battle Rush")
    private var checkedFirstMessage = false
    private var checkedScoreboard = false
    private var inPractice = false
    private var inPartyChat = false
    var inBridgingGame = false

    @SubscribeEvent
    fun onQuit(e: ClientDisconnectionFromServerEvent) {
        checkedFirstMessage = false
        inBridgingGame = false
        inPartyChat = false
        inPractice = false
        checkedScoreboard = false
    }

    @SubscribeEvent
    fun onChat(e: ClientChatReceivedEvent) {
        val cleanText = e.message.unformattedText.trim()
        if (cleanText.isEmpty()) return

        if (!checkedFirstMessage) {
            checkedFirstMessage = true
            inPractice = cleanText in ALL_PROXY
            return
        }

        if (!inPractice) return

        tryPartyChat()
        checkBridgingGame(cleanText)
        checkGameEnd(cleanText)
    }

    fun preventTablist(e: RenderGameOverlayEvent.Post) {
        if (!ModConfig.disablePlayerList) return
        if (e.type != RenderGameOverlayEvent.ElementType.PLAYER_LIST) return
        if (!inPractice) return
        e.isCanceled = true
    }

    private fun tryPartyChat() {
        if (!ModConfig.autoPartyChat) return
        if (inPartyChat) return

        mc.thePlayer.sendChatMessage("/p chat")
        inPartyChat = true
    }

    private fun checkBridgingGame(chat: String) {
        if (inBridgingGame) return
        if (chat !in BRIDGING_GAMES) return

        inBridgingGame = true
    }

    private fun checkGameEnd(chat: String) {
        if (!chat.startsWith("Match Results")) return

        if (inBridgingGame) inBridgingGame = false

        if (ModConfig.autoGG) mc.thePlayer.sendChatMessage(ModConfig.autoGGText)
    }
}
