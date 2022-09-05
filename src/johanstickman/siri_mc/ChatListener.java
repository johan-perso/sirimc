package johanstickman.siri_mc;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class ChatListener implements Listener {
    // Détecter les nouveaux messages dans le chat
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        // Si le message ne commence pas par Siri, annuler
        if(!e.getMessage().toLowerCase(Locale.ROOT).startsWith("siri")) return;

        // Si, avant modification, le message est trop court, annuler
        if(e.getMessage().length() < 6){
            e.getPlayer().sendMessage("§7[Bot] §rSiri: Bonjour ! Je suis Siri, votre assistant personnel. Pour me parler, envoyer §c\"siri <votre message>\"§r dans le chat.");
            setTimeout(() -> e.getPlayer().sendMessage("§7[Bot] §rSiri: J'utilise l'API d'Anti Coupable (https://anticoupable.johanstick.me) pour te fournir des réponses, cependant je n'ai pas réponse à tout :("), 1500);
            setTimeout(() -> e.getPlayer().sendMessage("§7[Bot] §rSiri: mais tu peux tout de même rejoindre le serveur Discord d'Anti Coupable (https://discord.gg/Fg8Ruzxnzp) et utiliser la commande §c\"/chatset\"§r pour définir une réponse :)"), 3000);
            e.setCancelled(true);
            return;
        }

        // Modifier le message pour enlever les 5 premiers caractères ("siri ")
        e.setMessage(e.getMessage().substring(5));

        // Si, après modification, le message est trop court, annuler
        if(e.getMessage().length() < 2){
            e.getPlayer().sendMessage("§7[Bot] §rSiri: Bonjour ! Je suis Siri, votre assistant personnel. Pour me parler, envoyer §c\"siri <votre message>\"§r dans le chat.");
            setTimeout(() -> e.getPlayer().sendMessage("§7[Bot] §rSiri: J'utilise l'API d'Anti Coupable (https://anticoupable.johanstick.me) pour te fournir des réponses, cependant je n'ai pas réponse à tout :("), 1500);
            setTimeout(() -> e.getPlayer().sendMessage("§7[Bot] §rSiri: mais tu peux tout de même rejoindre le serveur Discord d'Anti Coupable (https://discord.gg/Fg8Ruzxnzp) et utiliser la commande §c\"/chatset\"§r pour définir une réponse :)"), 3000);
            e.setCancelled(true);
            return;
        }

        // Faire une requête vers l'API d'Anti Coupable
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://anticoupable.johanstick.me/api/ac-chat"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("message=" + e.getMessage()))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Obtenir le résultat de la requête
            String json = response.body();
            String[] lines = json.split(",\"");
            for (String line : lines) {
                // S'il y a un champ message (généralement une erreur)
                if (line.startsWith("message\":\"")) {
                    e.setMessage("Dis Siri, " + e.getMessage());
                    setTimeout(() -> Bukkit.broadcastMessage("§7[Bot] §rSiri: " + line.substring(10, line.length() - 1)), 200);
                }

                // S'il y a un champ response (généralement une réponse à la question posée)
                if (line.startsWith("response\":\"")) {
                    e.setMessage("Dis Siri, " + e.getMessage());
                    setTimeout(() -> Bukkit.broadcastMessage("§7[Bot] §rSiri: " + line.substring(11, line.length() - 2)), 200);
                }
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    // J'suis habitué au JS du coup j'rajoute une fonction quasi pareil qu'en JavaScript 😭😭
    // (https://stackoverflow.com/a/36842856)
    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }
}
