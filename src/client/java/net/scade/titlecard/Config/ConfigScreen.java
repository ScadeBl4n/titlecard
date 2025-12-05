package net.scade.titlecard.Config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.scade.titlecard.config.TitleConfig;

public class ConfigScreen {

    public static Screen build(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("Titlecard Config!!"));

        ConfigCategory general = builder.getOrCreateCategory(Text.of("Titlecard Settings"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(
                Text.of("Enabled"),
                TitleConfig.config.enabled)
                .setDefaultValue(true)
                .setTooltip(Text.of("Whether or not to use the default minecraft Title logic"))
                .setSaveConsumer(newValue -> {
                    TitleConfig.config.enabled = newValue;
                    TitleConfig.save();
                })
                .build()
        );
        general.addEntry(entryBuilder.startStrField(
                        Text.of("Window Title Format"),
                        TitleConfig.config.formatPattern)
                .setDefaultValue("%M* %V")
                        .setTooltip(Text.of("Change the window title here, see below for formatting options"))
                .setSaveConsumer(newValue -> {
                    TitleConfig.config.formatPattern = newValue;
                    TitleConfig.save();
                })
                .build()
        );
        general.addEntry(entryBuilder.startTextDescription(Text.of("""
                        Hello! welcome to TITLECARD a mod that allows you to change the title of your game to anything
                        We decided to be nice and throw in some nice format specifiers for you such as:
                        "%M" which just puts "Minecraft"
                        "%V" which gives the version of minecraft you're on
                        "%S" which puts your username
                        "%s" which shows the gamestate (playing online, lan or in singleplayer but with a - before it so it'll be "- Singleplayer")
                        "%d" same as above without the dash
                        "%I" Shows the name of the server you're connected too (the one you put in add server)
                        "%i" Shows the ip address + port of the server you're connected too
                        "%l" Shows current modcount
                        "%F" Shows your current FPS
                        """))
                .build());

        builder.setSavingRunnable(TitleConfig::save);

        return builder.build();
    }
}
