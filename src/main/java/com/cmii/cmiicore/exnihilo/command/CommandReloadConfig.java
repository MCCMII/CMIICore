package com.cmii.cmiicore.exnihilo.command;

import com.cmii.cmiicore.Reference;
import com.cmii.cmiicore.exnihilo.config.ModConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

import javax.annotation.Nonnull;

public class CommandReloadConfig extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    @Nonnull
    public String getName() {
        return "enreloadconfig";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "enreloadconfig";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        ModConfig.loadConfigs();
        ConfigManager.load(Reference.MOD_ID, Config.Type.INSTANCE);
        sender.sendMessage(new TextComponentTranslation("commands.enreloadconfig.confirm"));
    }
}