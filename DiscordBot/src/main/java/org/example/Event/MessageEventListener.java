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

        if(message.equalsIgnoreCase("obama airstrike")){
            event.getChannel().sendMessage("Predator missiles incoming.").queue();
            try{TimeUnit.SECONDS.sleep(4);} //this is only in a try/catch bc sleep needs it
            catch (InterruptedException ex) {}
            event.getChannel().sendMessage("MIDDLE EASTERN CHILDREN RUN IN FEAR!").queue();
        }

        if(     message.equalsIgnoreCase("biden 2024") ||
                message.equalsIgnoreCase("trump 2024") ||
                message.equalsIgnoreCase("kanye 2024")){
            event.getChannel().sendMessage("Brent Peterson 2024").queue();
        }

        int nR = (int)(Math.random() * 10000);
        if(nR == 50)
            event.getMessage().reply("https://pbs.twimg.com/media/FdMVn_jakAEJca2?format=jpg&name=360x360").queue();

//        user = event.getMember().getEffectiveName();
//
//        System.out.println(user + "said: " + message);


    }
}
