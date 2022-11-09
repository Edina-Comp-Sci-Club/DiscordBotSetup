package org.example.Event;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class MessageEventListener  extends ListenerAdapter  {
    String message;
    String user;
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        super.onMessageReceived(event);

        message = event.getMessage().getContentRaw();

//        user = event.getMember().getEffectiveName();
//
//        System.out.println(user + "said: " + message);


    }
}
