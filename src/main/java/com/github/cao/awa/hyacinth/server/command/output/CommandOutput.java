package com.github.cao.awa.hyacinth.server.command.output;

import com.github.cao.awa.hyacinth.network.text.Text;

import java.util.UUID;

/**
 * Represents a subject which can receive command feedback.
 */
public interface CommandOutput {
    CommandOutput DUMMY = new CommandOutput(){
        @Override
        public void sendSystemMessage(Text message, UUID sender) {
        }

        @Override
        public boolean shouldReceiveFeedback() {
            return false;
        }

        @Override
        public boolean shouldTrackOutput() {
            return false;
        }

        @Override
        public boolean shouldBroadcastConsoleToOps() {
            return false;
        }
    };

    void sendSystemMessage(Text var1, UUID var2);

    boolean shouldReceiveFeedback();

    boolean shouldTrackOutput();

    boolean shouldBroadcastConsoleToOps();

    default boolean cannotBeSilenced() {
        return false;
    }
}
