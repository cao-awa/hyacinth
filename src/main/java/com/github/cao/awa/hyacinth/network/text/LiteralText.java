package com.github.cao.awa.hyacinth.network.text;

import com.google.gson.JsonObject;

public class LiteralText extends BaseText {
    public static final Text EMPTY = new LiteralText("");
    private final String text;

    public LiteralText(String string) {
        this.text = string;
    }

    @Override
    public String asString() {
        return this.text;
    }

    @Override
    public LiteralText copy() {
        return new LiteralText(this.text);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof LiteralText) {
            LiteralText literalText = (LiteralText) object;
            return this.text.equals(literalText.getRawString()) && super.equals(object);
        }
        return false;
    }

    public String getRawString() {
        return this.text;
    }

    public JsonObject toJSONObject() {
        JsonObject json = new JsonObject();
        json.addProperty("text", text);
        return json;
    }

    @Override
    public String toString() {
        return "TextComponent{text='" + this.text + "', siblings=" + this.siblings + ", style=" + this.getStyle() + "}";
    }
}