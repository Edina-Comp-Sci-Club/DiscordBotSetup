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
        //This is the Variable that we used to store the token.
//        final String  TOKEN = "Token Here";

        //The Code below is how we give birth to the bot, just like birth it's a little messy.
//        JDA bot = JDABuilder.createDefault(TOKEN)//this part connects our code to the discord bot

//                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES) // we tell discord with this code that we are trying to retrieve messages.
//
//                .addEventListeners(
//                        new ReadyEventListeners(),
//                        new MessageEventListener(),
//                        new InteractionEventReceiver()) // the Listerners here are the ones that wait for a message and let's us know what the message is  we can do what ever we want
//
//                .build(); // Then once we have all that done we build the bot, and give birth to something we will hate or die in the next 5 minuts
//
        //this is for slash commands
//        bot.upsertCommand("hi", "I will say hi in the most aggressive way").setGuildOnly(true).queue();

        //now try to follow along with code

        //CODE HERE




    }
}
