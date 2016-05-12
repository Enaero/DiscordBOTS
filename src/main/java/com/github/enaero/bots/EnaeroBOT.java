package com.github.enaero.bots;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import org.alicebot.ab.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Enaero's bot.
 */
public class EnaeroBOT extends ListenerAdapter implements Runnable {
    // Connector to Discord API
    private JDA mJDA = null;
    private Bot mBot = null;
    private Chat mChatSession = null;

    //private ChatterBotSession mChatterBotSession = null;

    // Meta info
    final String mName = "EnaeroBOT";
    final String mSourceBot = "alice2";

    //final ChatterBotType mChatterBotType = ChatterBotType.CLEVERBOT;
    Map<String, Long> mKnownUsers = null;

    // YouTube links to songs
    private String[] mHalloweenSongs = {
            "https://www.youtube.com/watch?v=4OFNVYcjR1c",
            "https://www.youtube.com/watch?v=WWum0VRc6MI",
            "https://www.youtube.com/watch?v=1f4_p8yqHiQ",
            "https://www.youtube.com/watch?v=q6-ZGAGcJrk"
    };
    private String[] mSecretSongs = {
            "https://www.youtube.com/watch?v=zwZISypgA9M",
            "https://www.youtube.com/watch?v=gMUEFZXkmDA",
            "https://www.youtube.com/watch?v=T37eZ55Hcmc"
    };
    
    public EnaeroBOT() {
        try {
            mJDA = new JDABuilder()
                    .setBotToken("MTczMzgxMjgwODE2MTY4OTYw.CgsLHQ.M9qI1VOQxMGULD-uKvDlcoMZ1PE")
                    .addListener(this)
                    .buildAsync();
            mKnownUsers = new HashMap<>();
            mBot = new Bot(mSourceBot, Config.programAbPath);
            mChatSession = new Chat(mBot);
        }
        catch (LoginException e) {
            e.printStackTrace(System.err);
            System.err.println("Error logging in: " + e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unknown exception: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            int keyPress = System.in.read();
            while (keyPress != 'q') {
                keyPress = System.in.read();
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            if (mJDA != null) {
                mJDA.shutdown(true);
            }
            System.out.println("Good bye Discord!");
        }
    }

    @Override
    public void onReady(ReadyEvent readyEvent) {
        System.out.println("I'M READY!" + readyEvent.toString());
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String messageLower = event.getMessage().getContent().toLowerCase();
        String author = event.getAuthor().getUsername();
        String authorId = event.getAuthor().getId();
        String gameName = event.getAuthor().getCurrentGame();

        System.out.println("I received a Guild Message!: " + messageLower);

        if (event.getAuthor().getId().equals(mJDA.getSelfInfo().getId())) {
            return;
        }
        
        if (messageLower.contains("happy halloween")) {
            int index = new Random().nextInt(mHalloweenSongs.length);
            event.getChannel().sendMessage(mHalloweenSongs[index]);
        } 
        else if (messageLower.contains("secret")){
            int index = new Random().nextInt(mSecretSongs.length);
            event.getChannel().sendMessage(mSecretSongs[index]);
        }
        else if (messageLower.contains(mName.toLowerCase())) {
            String request = messageLower.replace(mName.toLowerCase(), "alice");
            String response = mChatSession.multisentenceRespond(request).replace("ALICE", mName);
            event.getChannel().sendMessage(response);
        }
        else if (messageLower.equals("hi") || messageLower.equals("hello") || messageLower.equals("hey")) {
            if (!mKnownUsers.containsKey(authorId)) {
                event.getChannel().sendMessage(
                        String.format("Hi, %s. My name is %s. Nice to meet you!", author, mName));
            }
            else if (System.nanoTime() - mKnownUsers.get(authorId) > TimeUnit.HOURS.toNanos(1)) {
                event.getChannel().sendMessage("Hey! It's been a while. How are you?");
            }
            else if (gameName == null || gameName.length() < 1) {
                event.getChannel().sendMessage("What's up? Gonna play something soon?");
            }
            else {
                event.getChannel().sendMessage(
                        String.format("I see you're playing %s. How are you liking it?", gameName));
            }
            mKnownUsers.put(authorId, System.nanoTime());
        }
    }

}
