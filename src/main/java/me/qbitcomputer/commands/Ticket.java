package me.qbitcomputer.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
                event.getGuild().getCategoryById(Ticketcategory).createTextChannel(Ticketname).queue(channel -> {
                    channel.upsertPermissionOverride(event.getMember()).grant(Permission.VIEW_CHANNEL).queue();
                    event.reply("Your ticket has been created in channel: <#" + channel.getId() + ">").setEphemeral(true).queue();
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("Ticket " + event.getUser().getName());
                    embed.setColor(Color.decode("#0013ca"));
                    String name = dotenv.get("SERVERNAME");
                    embed.setFooter(name);
                    embed.addField("Hi there, We are ready to help!", "Please specify your type of issue and a staff member will be with you shortly!", false);
                    embed.addField("Here are the options:", "- " + Emoji.fromUnicode("U+1F4B8").getFormatted() + " Rank, Cosmetics or Billing issues" + "\n" + "- " + Emoji.fromUnicode("U+26D4").getFormatted() + " Glitches, Hacking and Abuse" + "\n" + "- " + Emoji.fromUnicode("U+1F389").getFormatted() + " Events & Giveaways" + "\n" + "- " + Emoji.fromUnicode("U+2753\n").getFormatted() + " Other issues", false);
                    channel.sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    net.dv8tion.jda.api.interactions.components.buttons.Button.primary("billing", "Billing").withEmoji(Emoji.fromUnicode("U+1F4B8")),
                                    net.dv8tion.jda.api.interactions.components.buttons.Button.primary("glitch", "Glitches").withEmoji(Emoji.fromUnicode("U+26D4")),
                                    net.dv8tion.jda.api.interactions.components.buttons.Button.primary("events", "Events").withEmoji(Emoji.fromUnicode("U+1F389")),
                                    net.dv8tion.jda.api.interactions.components.buttons.Button.primary("other", "Other").withEmoji(Emoji.fromUnicode("U+2753"))
                            )
                            .queue();
                });
            }
        }
    }
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("billing")) {
            Dotenv dotenv = Dotenv.load();
            EmbedBuilder billingembed = new EmbedBuilder();
            billingembed.setTitle("We're on our way!");
            billingembed.setColor(Color.decode("#0013ca"));
            String name = dotenv.get("SERVERNAME");
            String billingrole = dotenv.get("STAFFBILLINGROLE");
            billingembed.setFooter(name);
            billingembed.setDescription("A staff member will review your problem!");
            billingembed.setDescription("<@&" + billingrole + ">");
            event.getChannel().sendMessageEmbeds(billingembed.build()).queue();
            }
        }

    }
