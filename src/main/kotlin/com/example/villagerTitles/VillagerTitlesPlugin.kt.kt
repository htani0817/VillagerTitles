package com.example.villagertitles

import org.bukkit.event.entity.VillagerCareerChangeEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class VillagerTitlesPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        // 既存の村人も起動時に更新
        server.pluginManager.registerEvents(this, this)
        server.worlds.forEach { w ->
            w.entities.filterIsInstance<Villager>().forEach { it.updateTitle() }
        }
    }

    /** 自然・繁殖・スポーンエッグすべてここに来る */
    @EventHandler
    fun onVillagerSpawn(e: CreatureSpawnEvent) {
        (e.entity as? Villager)?.updateTitle()
    }

    /** 職業ブロックが変わった直後 */
    @EventHandler
    fun onCareerChange(e: VillagerCareerChangeEvent) {
        // 1 tick 後に確定するので遅延
        server.scheduler.runTask(this, Runnable { e.entity.updateTitle() })
    }

    /** 村人の頭上に職業名を出す */
    private fun Villager.updateTitle() {
        val comp = PROFESSION_NAMES[profession]
            ?: Component.text("未知", NamedTextColor.GRAY)
        customName(comp)
        isCustomNameVisible = true            // 常時ネームタグ表示:contentReference[oaicite:4]{index=4}
    }

    companion object {
        private val PROFESSION_NAMES = mapOf(
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
        )                                   // 職業一覧は Spigot Javadoc を参照:contentReference[oaicite:5]{index=5}
    }
}
