package com.icrazyblaze.twitchmod;

import com.icrazyblaze.twitchmod.config.ConfigHelper;
import com.icrazyblaze.twitchmod.irc.BotConfig;
import com.icrazyblaze.twitchmod.irc.BotConnection;
import com.icrazyblaze.twitchmod.util.Reference;
import com.icrazyblaze.twitchmod.util.TickHandler;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Commoble, iCrazyBlaze
 */
@Mod(Reference.MOD_ID)
public final class Main {

    public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);
    public static ConfigImplementation config;

    public Main() {
        config = ConfigHelper.register(ModConfig.Type.SERVER, ConfigImplementation::new); // instantiate and subscribe our config instance

        MinecraftForge.EVENT_BUS.register(ForgeEventSubscriber.class);
        MinecraftForge.EVENT_BUS.register(TickHandler.class);
    }


    public static void updateConfig() {

        // Set config values from server config file
        BotConfig.TWITCH_KEY = config.keyProp.get();
        BotConfig.CHANNEL_NAME = config.channelProp.get();
        BotConfig.showChatMessages = config.showMessagesProp.get();
        BotConfig.showCommands = config.showCommandsProp.get();
        BotConfig.prefix = config.prefixProp.get();
        BotCommands.username = config.usernameProp.get();

    }


    public static class ConfigImplementation {

        public final ConfigHelper.ConfigValueListener<String> keyProp;
        public final ConfigHelper.ConfigValueListener<String> channelProp;
        public final ConfigHelper.ConfigValueListener<Boolean> showMessagesProp;
        public final ConfigHelper.ConfigValueListener<Boolean> showCommandsProp;
        public final ConfigHelper.ConfigValueListener<Integer> chatSecondsProp;
        public final ConfigHelper.ConfigValueListener<Integer> messageSecondsProp;

        public final ConfigHelper.ConfigValueListener<String> usernameProp;
        public final ConfigHelper.ConfigValueListener<String> prefixProp;

        public final ConfigHelper.ConfigValueListener<Boolean> cooldownProp;

        ConfigImplementation(final ForgeConfigSpec.Builder builder, ConfigHelper.Subscriber subscriber) {
            builder.push("general");
            this.keyProp = subscriber.subscribe(builder
                    .comment("Oauth key from twitchapps.com")
                    .translation(Reference.MOD_ID + ".config.keyProp")
                    .define("keyProp", ""));
            this.channelProp = subscriber.subscribe(builder
                    .comment("Name of Twitch channel")
                    .translation(Reference.MOD_ID + ".config.channelProp")
                    .define("channelProp", ""));
            this.showMessagesProp = subscriber.subscribe(builder
                    .comment("Should chat messages be shown")
                    .translation(Reference.MOD_ID + ".config.showMessagesProp")
                    .define("showMessagesProp", false));

            this.showCommandsProp = subscriber.subscribe(builder
                    .comment("Should chosen commands be shown if chat messages are enabled")
                    .translation(Reference.MOD_ID + ".config.showCommandsProp")
                    .define("showCommandsProp", true));
            this.chatSecondsProp = subscriber.subscribe(builder
                    .comment("How many seconds until the next command is chosen")
                    .translation(Reference.MOD_ID + ".config.chatSecondsProp")
                    .define("chatSecondsProp", 20));
            this.messageSecondsProp = subscriber.subscribe(builder
                    .comment("How many seconds until a random viewer-written message is shown on screen")
                    .translation(Reference.MOD_ID + ".config.messageSecondsProp")
                    .define("messageSecondsProp", 300));

            this.usernameProp = subscriber.subscribe(builder
                    .comment("The streamer's Minecraft username")
                    .translation(Reference.MOD_ID + ".config.usernameProp")
                    .define("usernameProp", ""));
            this.prefixProp = subscriber.subscribe(builder
                    .comment("The streamer's Minecraft username")
                    .translation(Reference.MOD_ID + ".config.prefixProp")
                    .define("prefixProp", "!"));

            this.cooldownProp = subscriber.subscribe(builder
                    .comment("Prevent the same command from being executed twice in a row.")
                    .translation(Reference.MOD_ID + ".config.cooldownProp")
                    .define("cooldownProp", false));

            builder.pop();

        }

    }

}