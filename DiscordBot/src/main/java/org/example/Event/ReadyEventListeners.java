package org.example.Event;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

public class ReadyEventListeners extends ListenerAdapter {
    String message;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        //The super.onMessageReceived(event) is basically saying when there is a message run this thing. It's important to remember that it's discord that calls this method and not our code.
        super.onMessageReceived(event);




    }
}
