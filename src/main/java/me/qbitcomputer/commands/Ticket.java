package me.qbitcomputer.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class Ticket extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ticket")) {
            Dotenv dotenv = Dotenv.load();
            String Ticketcategory = dotenv.get("TICKETCATEGORY");
            String Ticketname = "ticket-" +event.getUser().getName();
            List tickets = event.getGuild().getTextChannelsByName(Ticketname, true);
            if (!tickets.isEmpty()) {
                event.reply("You already have an open ticket!").setEphemeral(true).queue();

            } else {
                event.getGuild().getCategoryById(Ticketcategory).createTextChannel(Ticketname).queue(channel -> {
                    channel.upsertPermissionOverride(event.getMember()).grant(Permission.VIEW_CHANNEL).queue();
                    event.reply("Your ticket has been created in channel: <#" + channel.getId() + ">").setEphemeral(true).queue();
                });
            }
        }
    }
}
