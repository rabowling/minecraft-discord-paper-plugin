package com.reedbowling;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.TextComponent;

public class DiscordWebhookHandler {
    
    private static final String WEBHOOK_URL = "redacted";

    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();

    public static void sendMessageToDiscord(Player sender, String message) throws Exception {
        sendMessageToDiscord(((TextComponent)sender.displayName()).content(), message);
    }

    public static void sendMessageToDiscord(String sender, String message) throws Exception {
        Map<Object, Object> data = new HashMap<>();
        data.put("username", sender);
        data.put("content", message);
        
        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .timeout(Duration.ofSeconds(30))
                .uri(URI.create(WEBHOOK_URL))
                .setHeader("User-Agent", "Minecraft Discord Plugin")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();
        
        client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(JavaPlugin.getPlugin(MinecraftDiscordPlugin.class).getLogger()::info);
    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
