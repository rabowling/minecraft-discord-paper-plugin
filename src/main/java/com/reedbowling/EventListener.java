package com.reedbowling;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.advancement.AdvancementDisplay;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent chatEvent) {
        String sender = "SERVER";
        String playerName = ((TextComponent) chatEvent.getPlayer().displayName()).content();
        String message = "<" + playerName + "> " + ((TextComponent)chatEvent.message()).content();

        try {
            DiscordWebhookHandler.sendMessageToDiscord(sender, message);
        } catch (Exception e) {
            JavaPlugin.getPlugin(MinecraftDiscordPlugin.class).getLogger().info(e.getLocalizedMessage());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent deathEvent) {
        String sender = "SERVER";
        String message = "**" + deathEvent.getDeathMessage() + ".**";

        try {
            DiscordWebhookHandler.sendMessageToDiscord(sender, message);
        } catch (Exception e) {
            JavaPlugin.getPlugin(MinecraftDiscordPlugin.class).getLogger().info(e.getLocalizedMessage());
        }
    }

    // @EventHandler
    // public void onAdvancementEarned(PlayerAdvancementDoneEvent advancementEvent) {
    //     String sender = "SERVER";
    //     String messageFormat = "%s has made the advancement **%s** - %s";

    //     String playerName = ((TextComponent)advancementEvent.getPlayer().displayName()).content();

    //     AdvancementDisplay advancement = advancementEvent.getAdvancement().getDisplay();
    //     String advancementName = ((TextComponent)advancement.title()).content();
    //     String advancementDescription = ((TextComponent)advancement.description()).content();

    //     String message = String.format(messageFormat, playerName, advancementName, advancementDescription);

    //     try {
    //         DiscordWebhookHandler.sendMessageToDiscord(sender, message);
    //     } catch (Exception e) {
    //         JavaPlugin.getPlugin(MinecraftDiscordPlugin.class).getLogger().info(e.getLocalizedMessage());
    //     }
    // }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent joinEvent) {
        String sender = "SERVER";
        String message = ((TextComponent)joinEvent.getPlayer().displayName()).content() + " has joined the server.";

        try {
            DiscordWebhookHandler.sendMessageToDiscord(sender, message);
        } catch (Exception e) {
            JavaPlugin.getPlugin(MinecraftDiscordPlugin.class).getLogger().info(e.getLocalizedMessage());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent quitEvent) {
        String sender = "SERVER";
        String message = ((TextComponent)quitEvent.getPlayer().displayName()).content() + " has left the server.";

        try {
            DiscordWebhookHandler.sendMessageToDiscord(sender, message);
        } catch (Exception e) {
            JavaPlugin.getPlugin(MinecraftDiscordPlugin.class).getLogger().info(e.getLocalizedMessage());
        }
    }
}
