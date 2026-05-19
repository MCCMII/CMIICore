package com.cmii.cmiicore.exnihilo.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

public class CommandHandNBT extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "enhandnbt";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender) {
        return "enhandnbt";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (args.length > 0)
            return;
        if (sender instanceof EntityPlayer) {
            ItemStack stack = ((EntityPlayer) sender).getHeldItem(EnumHand.MAIN_HAND);
            if (stack.isEmpty() || !stack.hasTagCompound())
                return;
            String dump = stack.getTagCompound().toString();
            sender.sendMessage(new TextComponentString(dump));
        }
    }
}