package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.example.Event.InteractionEventReceiver;
import org.example.Event.MessageEventListener;
import org.example.Event.ReadyEventListeners;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;


public class Main {
    public static void main(String[] args) throws InterruptedException, LoginException {
        //This is the Variable that we used to store the token.
        botTokenHolder archive = new botTokenHolder();
        final String TOKEN = archive.getToken();

        //The Code below is how we give birth to the bot, just like birth it's a little messy.
        JDA obama = JDABuilder.createDefault(TOKEN) //this part connects our code to the discord bot

               .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES) // we tell discord with this code that we are trying to retrieve messages.

                .addEventListeners(
                        new ReadyEventListeners(),
                        new MessageEventListener(),
                        new InteractionEventReceiver()) // the Listeners here are the ones that wait for a message and lets us know what the message is  we can do what ever we want

                .setActivity(Activity.playing("Syrian airstrike sim 2023"))

                .build(); // Then once we have all that done we build the bot, and give birth to something we will hate or die in the next 5 minuts

        //this is for slash commands
        obama.upsertCommand("hi", "I will say hi in the most aggressive way").setGuildOnly(true).queue();

        CommandListUpdateAction commands = obama.updateCommands();


        obama.upsertCommand("airstrike", "The kunduz specialty").setGuildOnly(true).queue();

        //now try to follow along with code

        //CODE HERE





    }
}
