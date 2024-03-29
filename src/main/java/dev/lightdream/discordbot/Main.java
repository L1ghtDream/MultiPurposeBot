package dev.lightdream.discordbot;

import dev.lightdream.discordbot.command.announcer.AnnounceCommand;
import dev.lightdream.discordbot.command.bar.CreateBarCommand;
import dev.lightdream.discordbot.command.bar.DeleteBarCommand;
import dev.lightdream.discordbot.command.bar.UpdateBarCommand;
import dev.lightdream.discordbot.dto.config.Config;
import dev.lightdream.discordbot.dto.config.Data;
import dev.lightdream.discordbot.manager.ScheduleManager;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.dto.JDAConfig;
import dev.lightdream.jdaextension.managers.DiscordCommandManager;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.messagebuilder.MessageBuilderManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.util.Arrays;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

public class Main implements JDAExtensionMain, FileManagerMain, LoggableMain {


    // Static
    public static Main instance;

    // Instances
    public JDA bot;

    // Config
    public Config config;
    public Data data;

    // Manager
    public FileManager fileManager;
    public ScheduleManager scheduleManager;

    public void enable() {
        instance = this;
        Logger.init(this);

        fileManager = new FileManager(this);
        MessageBuilderManager.init(fileManager);
        loadConfigs();

        bot = JDAExtensionMain.generateBot(this , Arrays.asList(
                GUILD_BANS,
                GUILD_MODERATION,
                GUILD_EMOJIS_AND_STICKERS,
                GUILD_WEBHOOKS,
                GUILD_INVITES,
                GUILD_VOICE_STATES,
                GUILD_PRESENCES,
                GUILD_MESSAGES,
                GUILD_MESSAGE_REACTIONS,
                GUILD_MESSAGE_TYPING,
                DIRECT_MESSAGES,
                DIRECT_MESSAGE_REACTIONS,
                DIRECT_MESSAGE_TYPING,
                MESSAGE_CONTENT,
                SCHEDULED_EVENTS
        ));

        new DiscordCommandManager(this, Arrays.asList(
                new CreateBarCommand(),
                new DeleteBarCommand(),
                new UpdateBarCommand(),
                new AnnounceCommand()
        ));

        scheduleManager = new ScheduleManager();
    }

    private void loadConfigs() {
        config = fileManager.load(Config.class);
        data = fileManager.load(Data.class);
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public JDA getBot() {
        return bot;
    }

    @Override
    public JDAConfig getJDAConfig() {
        return config;
    }

    @Override
    public File getDataFolder() {
        return new File(System.getProperty("user.dir"));
    }

    @Override
    public boolean debugToConsole() {
        return config.debug;
    }
}
