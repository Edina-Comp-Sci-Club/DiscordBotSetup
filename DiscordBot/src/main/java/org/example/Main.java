package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.Event.InteractionEventReceiver;
import org.example.Event.MessageEventListener;
import org.example.Event.ReadyEventListeners;

import javax.security.auth.login.LoginException;


public class Main {
    public static void main(String[] args) throws InterruptedException, LoginException {
        final String  TOKEN = "Token Here";
        JDA bot = JDABuilder.createDefault(TOKEN)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new ReadyEventListeners(), new MessageEventListener(),new InteractionEventReceiver())
                .build();

        bot.upsertCommand("hi", "I will say hi in the most agressive way").setGuildOnly(true).queue();

    }
}
