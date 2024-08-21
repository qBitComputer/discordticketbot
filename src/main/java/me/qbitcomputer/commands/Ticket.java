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
            String Ticketname = "ticket-" + event.getUser().getName();
            List tickets = event.getGuild().getTextChannelsByName(Ticketname, true);
            if (!tickets.isEmpty()) {
                event.reply("You already have an open ticket!").setEphemeral(true).queue();

            } else {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Creating a ticket for: " + event.getUser().getName());
                embed.setColor(Color.decode("#0013ca"));
                String name = dotenv.get("SERVERNAME");
                embed.setFooter(name);
                embed.addField("Hi there, We're ready to help! ", "A staff member from team " + "<@&" + dotenv.get("BILLINGROLE") + ">" +" will be with you shortly!", false);
                event.replyEmbeds(embed.build()).addActionRow(StringSelectMenu.create("choose-ticket")
                        .addOption("Billing, Ranks & Cosmetics " + Emoji.fromUnicode("U+1F4B8").getFormatted(), "billing")
                        .addOption("Glitches, Hacking and Abuse " + Emoji.fromUnicode("U+26D4").getFormatted(), "glitch")
                        .addOption("Events & Giveaways " + Emoji.fromUnicode("U+1F389").getFormatted(), "event")
                        .addOption("Other issues " + Emoji.fromUnicode("U+2753").getFormatted(), "other")
                        .build()).setEphemeral(true).queue();
            }
        }
    }
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        Dotenv dotenv = Dotenv.load();
        if (event.getComponentId().equals("choose-ticket")) {
            String Ticketcategory = dotenv.get("TICKETCATEGORY");
            String Ticketname = "ticket-" + event.getUser().getName();
            event.getGuild().getCategoryById(Ticketcategory).createTextChannel(Ticketname).queue(channel -> {
                channel.upsertPermissionOverride(event.getMember()).grant(Permission.VIEW_CHANNEL).queue();
                event.reply("Ticket has been created: <#" + channel.getId() + ">").setEphemeral(true).queue();
                if (event.getValues().get(0).equals("billing")) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("Billing issue " + event.getUser().getName());
                    embed.setColor(Color.decode("#0013ca"));
                    String name = dotenv.get("SERVERNAME");
                    embed.setFooter(name);
                    embed.addField("Hi there, We're ready to help! ", "A staff member from team " + "<@&" + dotenv.get("BILLINGROLE") + ">" + " will be with you shortly!", false);
                    channel.sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    net.dv8tion.jda.api.interactions.components.buttons.Button.danger("close", "Close").withEmoji(Emoji.fromUnicode("U+274C"))
                            )

                            .queue();
                    channel.sendMessage("<@&" + dotenv.get("BILLINGROLE") + ">").queue();
                } else if (event.getValues().get(0).equals("event")) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("Event issue " + event.getUser().getName());
                    embed.setColor(Color.decode("#0013ca"));
                    String name = dotenv.get("SERVERNAME");
                    embed.setFooter(name);
                    embed.addField("Hi there, We're ready to help! ", "A staff member from team " + "<@&" + dotenv.get("EVENTROLE") + ">" + " will be with you shortly!", false);
                    channel.sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    net.dv8tion.jda.api.interactions.components.buttons.Button.danger("close", "Close").withEmoji(Emoji.fromUnicode("U+274C"))
                            )
                            .queue();
                    channel.sendMessage("<@&" + dotenv.get("EVENTROLE") + ">").queue();
                } else if (event.getValues().get(0).equals("other")) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("Other issue " + event.getUser().getName());
                    embed.setColor(Color.decode("#0013ca"));
                    embed.addField("Hi there, We're ready to help! ", "A staff member from team " + "<@&" + dotenv.get("OTHERROLE") + ">" + " will be with you shortly!", false);
                    String name = dotenv.get("SERVERNAME");
                    embed.setFooter(name);

                    channel.sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    net.dv8tion.jda.api.interactions.components.buttons.Button.danger("close", "Close").withEmoji(Emoji.fromUnicode("U+274C"))
                            )

                            .queue();
                    channel.sendMessage("<@&" + dotenv.get("OTHERROLE") + ">").queue();
                } else if (event.getValues().get(0).equals("glitch")) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setTitle("Glitch issue " + event.getUser().getName());
                    embed.setColor(Color.decode("#0013ca"));
                    String name = dotenv.get("SERVERNAME");
                    embed.setFooter(name);
                    embed.addField("Hi there, We're ready to help! ", "A staff member from team " + "<@&" + dotenv.get("GLITCHROLE") + ">" + " will be with you shortly!", false);
                    channel.sendMessageEmbeds(embed.build())
                            .addActionRow(
                                    net.dv8tion.jda.api.interactions.components.buttons.Button.danger("close", "Close").withEmoji(Emoji.fromUnicode("U+274C"))
                            )
                            .queue();
                    channel.sendMessage("<@&" + dotenv.get("GLITCHGROLE") + ">").queue();
                }
            });
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("close")) {
            event.getChannel().delete().queue();
        }
    }
}