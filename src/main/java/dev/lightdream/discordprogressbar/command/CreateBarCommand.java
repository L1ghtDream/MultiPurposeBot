package dev.lightdream.discordprogressbar.command;

import dev.lightdream.discordprogressbar.Main;
import dev.lightdream.discordprogressbar.manager.BarHelper;
import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Arrays;

public class CreateBarCommand extends DiscordCommand {
    public CreateBarCommand() {
        super(Main.instance, Arrays.asList("create-bar", "createbar"), "Creates new progress bar", Permission.ADMINISTRATOR, true, Arrays.asList(
                new CommandArgument(OptionType.STRING, "name", "Create a new progressbar", true),
                new CommandArgument(OptionType.CHANNEL, "channel", "The channel where the bar will be created", false)
        ));
    }

    @Override
    public void executeGuild(GuildCommandContext guildCommandContext) {
        String name = guildCommandContext.getArgument("name").getAsString();
        if (BarHelper.exists(name)) {
            sendMessage(guildCommandContext, Main.instance.config.barExists);
            return;
        }

        MessageChannel channel = guildCommandContext.getArgument("channel").getAsMessageChannel();
        if (channel == null) {
            channel = guildCommandContext.getTextChannel();
        }

        BarHelper.createBar(name, channel);
        sendMessage(guildCommandContext, Main.instance.config.barCreated);
    }

    @Override
    public void executePrivate(PrivateCommandContext privateCommandContext) {
        // Impossible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}