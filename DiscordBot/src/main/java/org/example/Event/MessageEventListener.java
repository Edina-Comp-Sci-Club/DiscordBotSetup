package org.example.Event;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.CreateImageVariationRequest;
import com.theokanning.openai.service.OpenAiService;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.SessionConstants;
import org.imgscalr.Scalr;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;


public class MessageEventListener  extends ListenerAdapter {
    protected String token = System.getenv("OPENAI_TOKEN");
    String message;
    String user;
    SessionConstants sessionConstants = new SessionConstants();
    OpenAiService service = new OpenAiService(token);
    Message messageToEdit;


    final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "You will respond to all messages by saying that the default prompt must still be set.");
    final List<ChatMessage> chatMessageHistory = new ArrayList<>() {{
        add(systemMessage);
    }};


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);

        message = event.getMessage().getContentRaw();

        if (!event.getAuthor().isBot()) {
            //Commands
            if (!event.getChannel().equals(sessionConstants.getGPTChatChannel())) {
                //Admin
                if (message.equals("crash") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    event.getChannel().sendMessage("Shutting down...").queue();
                    System.exit(0);
                }
                //Utility
                if (message.equalsIgnoreCase("Confirm")) {
                    event.getMessage().reply("Confirmed!").queue();
                }

                if (message.equalsIgnoreCase("help")) {
                    event.getMessage().reply("""
                            Commands:\s
                              **Confirm**: Confirms the bot is online
                              **GPT image**: Generates an image based on text entered after *image*
                              **GPT complete**: Completes text entered after *complete*
                              **GPT channel set**: Sets the current channel to be used for interactive chatting. The only command that works in this channel is **GPT prompt**
                              **GPT prompt**: Sets the chatbot's "personality"
                              **GPT edit**: Provides a variation on an inputted image; Transparent areas become black
                              
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

                if (message.startsWith("GPT edit")) {
                    URL imageUrl; //The URL of the image
                    String tempDir = System.getProperty("java.io.tmpdir"); //Your computer's temp directory
                    String path = tempDir + "botImages/" + LocalTime.now() + ".png"; //The filepath as a poorly converted png
                    File imageFile = new File(path); //The file object
                    BufferedImage image; //Instantiate bufferedimage
                    Graphics2D rescaler; //Graphics2D object to resize image
                    System.out.println(path);
                    try {
                        imageUrl = new URL(event.getMessage().getAttachments().get(0).getUrl()); //Assign image url
                        try {
                            FileUtils.copyURLToFile(imageUrl, imageFile); //Download URL to file in memory
                            int imageSideLength = Math.max(ImageIO.read(imageFile).getHeight(), ImageIO.read(imageFile).getWidth()); //Get Highest image dimension
                            int scaledSideLength = (imageSideLength > 768) ? 1024 : ((imageSideLength > 256) ? 512 : 256); //Get what will be the scaled length
                            image = new BufferedImage( //Image that will be written to
                                    scaledSideLength,
                                    scaledSideLength,
                                    BufferedImage.TYPE_4BYTE_ABGR
                            );
                            rescaler = image.createGraphics(); //Declare rescaler
                            rescaler.drawImage(ImageIO.read(imageFile),0,0, scaledSideLength, scaledSideLength, null); //Draw to output image
                            rescaler.dispose(); //TODO this doesnt work lol
                            ImageIO.write(image, "png", imageFile); //Write & properly convert image -> file & PNG
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        if (Files.size(imageFile.toPath()) > 4000000) {
                            event.getMessage().reply("Inputted image cannot be more than 4MB when processed, please provide a smaller file.").queue();
                        } else {
                            event.getMessage().reply("Editing image...").queue(message1 -> {messageToEdit = message1;});
                            CreateImageVariationRequest request = CreateImageVariationRequest.builder()
                                    .responseFormat("url")
                                    .size("512x512")
                                    .build();
                            String generatedImageUrl = service.createImageVariation(request, imageFile).getData().get(0).getUrl();
                            messageToEdit.editMessage(generatedImageUrl).queue();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (message.startsWith("GPT complete")) {
                    String prompt = message.substring(12);

                    CompletionRequest completionRequest = CompletionRequest.builder()
                            .model("text-davinci-003")
                            .prompt(prompt)
                            .echo(false)
                            .maxTokens(50)
                            .build();

                    String response = service.createCompletion(completionRequest).getChoices().get(0).getText();

                    event.getMessage().reply(response).queue();
                }

                //Set Channel for GPT chatting
                if (message.equalsIgnoreCase("GPT channel set") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    sessionConstants.setGPTChatChannel(event.getChannel());
                    event.getMessage().reply("GPT channel set to: **" + event.getChannel().getName() + "**").queue();
                }
            } else { //GPT chat history
                if (message.startsWith("GPT prompt")) {
                    if(message.substring(10).isEmpty()){
                        event.getMessage().reply("Please provide a valid prompt.").queue();
                    }
                    else {
                        chatMessageHistory.set(0, new ChatMessage(ChatMessageRole.SYSTEM.value(), message.substring(10)));
                        event.getMessage().reply("Prompt changed to: **" + chatMessageHistory.get(0).getContent() +"**").queue();
                    }
                }
                else if (event.getMessage().getMentions().getUsers().contains(event.getJDA().getSelfUser())) {
                    chatMessageHistory.add(new ChatMessage(ChatMessageRole.USER.value(), event.getMessage().getContentRaw()));

                    ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                            .builder()
                            .model("gpt-3.5-turbo")
                            .messages(chatMessageHistory)
                            .temperature(.4)
                            .maxTokens(50)
                            .build();
                    String completedMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
                    event.getMessage().reply(completedMessage).queue();
                } else {
                    chatMessageHistory.add(new ChatMessage(ChatMessageRole.USER.value(), event.getMessage().getContentRaw()));
                }
            }

        }
    }
}
