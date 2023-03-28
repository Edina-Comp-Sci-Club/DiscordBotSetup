package org.example.Event;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class MessageEventListener  extends ListenerAdapter  {
    protected String token = System.getenv("OPENAI_TOKEN"); //TODO set this
    String message;
    String user;
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        super.onMessageReceived(event);

        message = event.getMessage().getContentRaw();

        OpenAiService service = new OpenAiService(token);

        if(message.startsWith("GPT image ")) {
            event.getMessage().reply("Generating image...").queue(
                    message1 -> {
                        CreateImageRequest request = CreateImageRequest.builder()
                                .prompt(message.substring(11))
                                .build();

                        String generatedImageUrl = service.createImage(request).getData().get(0).getUrl();

                        message1.editMessage(generatedImageUrl).queue();
                    }

            );
        }
//        user = event.getMember().getEffectiveName();
//
//        System.out.println(user + "said: " + message);


    }
}
