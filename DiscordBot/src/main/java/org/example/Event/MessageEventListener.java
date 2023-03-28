package org.example.Event;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class MessageEventListener  extends ListenerAdapter  {
    protected String token = System.getenv("OPENAI_TOKEN");
    String message;
    String user;
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event)
    {
        super.onMessageReceived(event);

        message = event.getMessage().getContentRaw();

        OpenAiService service = new OpenAiService(token);

        if (message.equalsIgnoreCase("Confirm")) {
            event.getMessage().reply("Confirmed!").queue();
        }

        if(message.equals("crash") && event.getMember().hasPermission(Permission.ADMINISTRATOR)){
            event.getChannel().sendMessage("Shutting down...").queue();
            System.exit(0);
        }

        if (message.startsWith("GPT image ")) {
            String prompt = message.substring(10);
            event.getMessage().reply("Generating image...").queue(
                    message1 -> {
                        System.out.println(prompt);
                        CreateImageRequest request = CreateImageRequest.builder()
                                .prompt(prompt)
                                .build();

                        String generatedImageUrl = service.createImage(request).getData().get(0).getUrl();

                        message1.editMessage(generatedImageUrl).queue();
                    }
            );
        }
        if(message.startsWith("GPT complete")) {
            String prompt = message.substring(12);

            CompletionRequest completionRequest = CompletionRequest.builder()
                    .model("text-davinci-003")
                    .prompt(prompt)
                    .echo(false)
                    .n(1)
                    .maxTokens(50)
                    .build();

            String response = service.createCompletion(completionRequest).getChoices().get(0).getText();

            event.getMessage().reply(response).queue();
        }
//        user = event.getMember().getEffectiveName();
//
//        System.out.println(user + "said: " + message);


    }
}
