package io.discloader.discloader.common.start;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.google.gson.Gson;

import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.client.render.WindowFrame;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.ShardManager;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.logger.DLErrorStream;
import io.discloader.discloader.common.logger.DLPrintStream;

/**
 * DiscLoader client entry point
 * 
 * @author Perry Berman
 * @see DiscLoader
 */
public class Main {
    public static final Gson gson = new Gson();
    public static WindowFrame window;
    public static DiscLoader loader;
    public static boolean usegui = false;
    public static String token;
    private static int shard = 0, shards = 1;
    private static Logger LOGGER;

    public static DiscLoader getLoader() {
        return loader;
    }

    /**
     * @return the lOG
     */
    public static Logger getLogger() {
        return LOGGER;
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String... args) throws IOException {
        LOGGER = DiscLoader.LOG;
        System.setOut(new DLPrintStream(System.out, LOGGER));
        System.setErr(new DLErrorStream(System.err, LOGGER));
        System.setProperty("http.agent", "DiscLoader");
        String content = "";
        Object[] lines = Files.readAllLines(Paths.get("./options.json")).toArray();
        for (Object line : lines)
            content += line;
        options options = gson.fromJson(content, options.class);
        DiscLoader.addEventHandler(new EventListenerAdapter() {

            @Override
            public void Ready(DiscLoader loader) {
//                LOGGER.fine(String.format("Ready as user %s#%s", loader.user.username, loader.user.discriminator));
                try {
                    loader.voiceChannels.get("245269174425747467").join().thenAcceptAsync(connection -> {
                        connection.player.setVolume(10);
                        connection.play(new File("Fumes.mp3"));
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        token = options.auth.token;
        parseArgs(args);
        if (shard != -1) {
            loader = new DiscLoader(shards, shard);
            loader.startup();
        } else {
            @SuppressWarnings("unused")
            ShardManager manager = new ShardManager(shards);
        }
    }

    public static void parseArgs(String... args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("usegui")) {
                usegui = true;
            } else if (args[i].equals("-t")) {
                if (i + 1 < args.length) {
                    token = args[i + 1];
                } else {
                    LOGGER.severe("Expected argument after -t");
                    System.exit(1);
                }
            } else if (args[i].equals("-p")) {
                if (i + 1 < args.length) {
                    CommandHandler.prefix = args[i + 1];
                } else {
                    LOGGER.severe("Expected argument after -p");
                    System.exit(1);
                }
            } else if (args[i].equals("-s")) {
                if (i + 1 < args.length) {
                    String[] g = args[i + 1].split("/");
                    shard = Integer.parseInt(g[0], 10);
                    shards = Integer.parseInt(g[1], 10);
                } else {
                    LOGGER.severe("Expected argument after -s");
                    System.exit(1);
                }
            } else if (args[i].equals("-S")) {
                if (i + 1 < args.length) {
                    shard = -1;
                    shards = Integer.parseInt(args[i + 1], 10);
                } else {
                    LOGGER.severe("Expected argument after -S");
                    System.exit(1);
                }
            }
        }
    }
}
