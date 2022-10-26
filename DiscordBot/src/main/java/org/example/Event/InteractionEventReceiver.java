package org.example.Event;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;

public class InteractionEventReceiver extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        super.onSlashCommandInteraction(event);

        System.out.println("Slash Commands");
        if(event.getName().equals("hi")) {
            event.reply("HI!!!").setEphemeral(true).queue();
        }
    }
}
