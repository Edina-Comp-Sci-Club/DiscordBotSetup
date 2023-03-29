package org.example;

import net.dv8tion.jda.api.entities.Channel;

public class SessionConstants {
    private Channel GPTChatChannel;

    public void setGPTChatChannel(Channel targetChannel){
        GPTChatChannel = targetChannel;
    }

    public Channel getGPTChatChannel(){
        return GPTChatChannel;
    }

}
