package com.github.cao.awa.hyacinth.network.text;

import com.github.cao.awa.hyacinth.server.command.source.ServerCommandSource;
import com.github.cao.awa.hyacinth.server.entity.Entity;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Text} that needs to be parsed when it is loaded into the game.
 */
public interface ParsableText {
    MutableText parse(@Nullable ServerCommandSource var1, @Nullable Entity var2, int var3) throws CommandSyntaxException;
}

