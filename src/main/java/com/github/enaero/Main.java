/**
 * Created by enaero
 */
package com.github.enaero;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class Main extends ListenerAdapter {
    public static void main(String[] args) {
        JDA jda = null;
        try {
            jda = new JDABuilder()
                    .setBotToken("MTczMzgxMjgwODE2MTY4OTYw.CgsLHQ.M9qI1VOQxMGULD-uKvDlcoMZ1PE")
                    .buildAsync();

            System.out.println("Building JDA object, type 'q' then ENTER to quit");
            int keyPress = System.in.read();
            while (keyPress != 'q') {
                keyPress = System.in.read();
            }
        }
        catch (LoginException e) {
            e.printStackTrace(System.err);
            System.err.println("Error logging in: " + e.getMessage());
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            if (jda != null) {
                jda.shutdown(true);
            }
        }
        System.out.println("Good bye Discord!");
    }

    @Override
    public void onReady(ReadyEvent readyEvent) {
        System.out.println("I'M READY!" + readyEvent.toString());
    }
}
