package org.example.Event;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;
import org.example.SessionConstants;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessageEventListener  extends ListenerAdapter {
    protected String token = System.getenv("OPENAI_TOKEN");
    String message;
    String user;
    SessionConstants sessionConstants = new SessionConstants();

    final List<ChatMessage> messages = new ArrayList<>();


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        message = event.getMessage().getContentRaw();

        OpenAiService service = new OpenAiService(token);

        //Admin
        if (message.equals("crash") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage("Shutting down...").queue();
            System.exit(0);
        }

        if (event.getChannel().equals(sessionConstants.getGPTChatChannel())) {

        } else {

            //Utility
            if (message.equalsIgnoreCase("Confirm")) {
                event.getMessage().reply("Confirmed!").queue();
            }

            if (message.equalsIgnoreCase("help")) {
                event.getMessage().reply("""
                        Commands:\s
                          **Confirm**: Confirms the bot is online
                          **GPT image**: Generates an image based on text entered after *image*
                          **GPT complete: Completes text entered after *complete*
                          
                        Admin Commands:
                          **crash**: Shuts down the bot
                          """
                ).queue();
            }

            //GPT
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

            if (message.startsWith("GPT complete")) {
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

            //Set Channel for GPT chatting
            if (message.equalsIgnoreCase("GPT channel set") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                sessionConstants.setGPTChatChannel(event.getChannel());
            }
        }
    }
}
