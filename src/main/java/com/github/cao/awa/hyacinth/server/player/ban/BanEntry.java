package com.github.cao.awa.hyacinth.server.player.ban;

import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.server.config.ServerConfigEntry;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BanEntry<T>
        extends ServerConfigEntry<T> {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    public static final String FOREVER = "forever";
    protected final Date creationDate;
    protected final String source;
    @Nullable
    protected final Date expiryDate;
    protected final String reason;

    public BanEntry(T key, @Nullable Date creationDate, @Nullable String source, @Nullable Date expiryDate, @Nullable String reason) {
        super(key);
        this.creationDate = creationDate == null ? new Date() : creationDate;
        this.source = source == null ? "(Unknown)" : source;
        this.expiryDate = expiryDate;
        this.reason = reason == null ? "Banned by an operator." : reason;
    }

    protected BanEntry(T key, JsonObject json) {
        super(key);
        Date parseException;
        Date date;
        try {
            date = json.has("created") ? DATE_FORMAT.parse(json.get("created").getAsString()) : new Date();
        }
        catch (ParseException parseException2) {
            date = new Date();
        }
        this.creationDate = date;
        this.source = json.has("source") ? json.get("source").getAsString() : "(Unknown)";
        try {
            parseException = json.has("expires") ? DATE_FORMAT.parse(json.get("expires").getAsString()) : null;
        }
        catch (ParseException parseException2) {
            parseException = null;
        }
        this.expiryDate = parseException;
        this.reason = json.has("reason") ? json.get("reason").getAsString() : "Banned by an operator.";
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public String getSource() {
        return this.source;
    }

    @Nullable
    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public String getReason() {
        return this.reason;
    }

    public abstract Text toText();

    @Override
    public boolean isInvalid() {
        if (this.expiryDate == null) {
            return false;
        }
        return this.expiryDate.before(new Date());
    }

    @Override
    public void write(JsonObject json) {
        json.addProperty("created", DATE_FORMAT.format(this.creationDate));
        json.addProperty("source", this.source);
        json.addProperty("expires", this.expiryDate == null ? FOREVER : DATE_FORMAT.format(this.expiryDate));
        json.addProperty("reason", this.reason);
    }
}

