package com.reedbowling;
import java.util.ArrayList;
import java.awt.Color;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;


public class MinecraftDiscordPlugin extends JavaPlugin {

    private final String BOT_TOKEN = "redacted";
    private long[] read_channel_ids = {927285275435089921l};
    private ArrayList<TextChannel> channels = new ArrayList<>();

    DiscordApi api = null;

    @Override
    public void onEnable() {
        getLogger().info("Discord Integration Plugin enabled.");

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        // Discord stuff
        api = new DiscordApiBuilder().setToken(BOT_TOKEN).login().join();

        for (long channel_id : read_channel_ids) {
            api.getTextChannelById(channel_id).ifPresent(channel -> {
                channels.add(channel);
            });
        }

        api.addMessageCreateListener(event -> {
            if (channels.contains(event.getChannel()) && event.getMessageAuthor().isWebhook() == false && event.getMessageContent().length() > 0) {
                broadcastMessage(event.getMessageAuthor(), event.getMessageContent());
            }
            // TODO: maybe do image to ascii for nick
        });

        try {
            DiscordWebhookHandler.sendMessageToDiscord("SERVER", "***SERVER IS UP AND RUNNING***");
        } catch (Exception e) {
            getLogger().info(e.getLocalizedMessage());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling Discord Integration Plugin.");

        try {
            DiscordWebhookHandler.sendMessageToDiscord("SERVER", "***SERVER IS SHUTTING DOWN***");
        } catch (Exception e) {
            getLogger().info(e.getLocalizedMessage());
        }

        if (api != null) {
            api.disconnect();
        }
    }

    private void broadcastMessage(MessageAuthor sender, String message) {
        Color roleColor = sender.getRoleColor().orElse(Color.WHITE);
        TextComponent text = Component.text("<")
                .append(Component.text(sender.getDisplayName(), TextColor.color(roleColor.getRed(), roleColor.getGreen(), roleColor.getBlue())))
                .append(Component.text("> "))
                .append(Component.text(message));
        getServer().broadcast(text);
    }
}
