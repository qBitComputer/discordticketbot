package me.qbitcomputer.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
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
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Ticket " + event.getUser().getName());
                embed.setColor(Color.decode("#0013ca"));
                String name = dotenv.get("SERVERNAME");
                embed.setFooter(name);
                embed.addField("Hi there, We are ready to help!", "Please specify your type of issue and a staff member will be with you shortly!", false);
                event.replyEmbeds(embed.build()).addActionRow(StringSelectMenu.create("choose-food")
                        .addOption("testtest", "test1", "testing") // SelectOption with only the label, value, and description
                        .addOptions(SelectOption.of("test2", "testing2")) // another way to create a SelectOption) // while also being the default option
                                .build()).setEphemeral(true).queue();
                }
            }
        }
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        Dotenv dotenv = Dotenv.load();
        if (event.getComponentId().equals("choose-food")) {
            String Ticketcategory = dotenv.get("TICKETCATEGORY");
            String Ticketname = "testing-" +event.getUser().getName();
            event.reply("a ticket will be created with issue related to: " + event.getValues().get(0)).setEphemeral(true).queue();
            event.getGuild().getCategoryById(Ticketcategory).createTextChannel(Ticketname).queue(channel -> {
                channel.upsertPermissionOverride(event.getMember()).grant(Permission.VIEW_CHANNEL).queue();
            });
        }
    }
}
