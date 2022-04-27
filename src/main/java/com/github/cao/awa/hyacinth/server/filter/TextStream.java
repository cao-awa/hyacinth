package com.github.cao.awa.hyacinth.server.filter;

import com.google.common.collect.*;

import java.util.*;
import java.util.concurrent.*;

public interface TextStream {
    TextStream UNFILTERED = new TextStream(){
        @Override
        public void onConnect() {
        }

        @Override
        public void onDisconnect() {
        }

        @Override
        public CompletableFuture<Message> filterText(String text) {
            return CompletableFuture.completedFuture(Message.permitted(text));
        }

        @Override
        public CompletableFuture<List<Message>> filterTexts(List<String> texts) {
            return CompletableFuture.completedFuture(texts.stream().map(Message::permitted).collect(ImmutableList.toImmutableList()));
        }
    };

    void onConnect();

    void onDisconnect();

    CompletableFuture<Message> filterText(String var1);

    CompletableFuture<List<Message>> filterTexts(List<String> var1);

    class Message {
        public static final Message EMPTY = new Message("", "");
        private final String raw;
        private final String filtered;

        public Message(String raw, String filtered) {
            this.raw = raw;
            this.filtered = filtered;
        }

        public String getRaw() {
            return this.raw;
        }

        public String getFiltered() {
            return this.filtered;
        }

        public static Message permitted(String text) {
            return new Message(text, text);
        }

        public static Message censored(String raw) {
            return new Message(raw, "");
        }
    }
}


