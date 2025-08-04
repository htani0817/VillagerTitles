package com.example.villagertitles

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.VillagerCareerChangeEvent
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.plugin.java.JavaPlugin

class VillagerTitlesPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        // イベントリスナー登録
        server.pluginManager.registerEvents(this, this)

        // /vtreload を登録（plugin.yml にも要記述）
        getCommand("vtreload")?.setExecutor { sender, _, _, _ ->
            sender.sendMessage(Component.text("VillagerTitles ➤ 全村人の職業名を再反映中…", NamedTextColor.YELLOW))
            rescanAllVillagers()
            sender.sendMessage(Component.text("VillagerTitles ➤ 全村人を再走査し完了しました。", NamedTextColor.GREEN))
            true
        }

        // 起動時に既存の村人に反映
        rescanAllVillagers()
    }

    @EventHandler
    fun onChunkLoad(e: ChunkLoadEvent) {
        // newly作成されてない既存チャンクにいる村人にタイトルを付ける
        if (!e.isNewChunk) {
            e.chunk.entities
                .filterIsInstance<Villager>()
                .forEach { it.updateTitle() }
        }
    }

    @EventHandler
    fun onVillagerSpawn(e: CreatureSpawnEvent) {
        (e.entity as? Villager)?.updateTitle()
    }

    @EventHandler
    fun onCareerChange(e: VillagerCareerChangeEvent) {
        // 職業変更後にリロードが確定するまで 1 tick 遅延
        server.scheduler.runTask(this, Runnable { e.entity.updateTitle() })
    }

    private fun rescanAllVillagers() {
        server.worlds
            .flatMap { it.loadedChunks.asSequence() }
            .flatMap { it.entities.asSequence() }
            .filterIsInstance<Villager>()
            .forEach { it.updateTitle() }
    }

    private fun Villager.updateTitle() {
        val text = PROFESSION_NAMES[profession]
            ?: Component.text("未知", NamedTextColor.GRAY)
        customName(text)
        isCustomNameVisible = true
    }

    companion object {
        val PROFESSION_NAMES = mapOf(
            Villager.Profession.NONE      to Component.text("ニート", NamedTextColor.GRAY),
            Villager.Profession.NITWIT    to Component.text("ニート", NamedTextColor.GRAY),
            Villager.Profession.LIBRARIAN to Component.text("司書", NamedTextColor.AQUA),
            Villager.Profession.FARMER    to Component.text("農民", NamedTextColor.GREEN),
            Villager.Profession.CLERIC    to Component.text("聖職者", NamedTextColor.LIGHT_PURPLE),
            Villager.Profession.ARMORER   to Component.text("防具鍛冶", NamedTextColor.DARK_GRAY),
            Villager.Profession.BUTCHER   to Component.text("肉屋", NamedTextColor.RED),
            Villager.Profession.CARTOGRAPHER to Component.text("製図師", NamedTextColor.GOLD),
            Villager.Profession.TOOLSMITH to Component.text("道具鍛冶", NamedTextColor.BLUE),
            Villager.Profession.WEAPONSMITH to Component.text("武器鍛冶", NamedTextColor.DARK_RED),
            Villager.Profession.SHEPHERD  to Component.text("羊飼い", NamedTextColor.WHITE),
            Villager.Profession.FISHERMAN to Component.text("漁師", NamedTextColor.AQUA)
        )
    }
}
