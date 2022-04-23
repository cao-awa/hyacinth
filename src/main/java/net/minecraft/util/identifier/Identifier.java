package net.minecraft.util.identifier;

import com.github.cao.awa.hyacinth.network.text.translate.TranslatableText;
import com.google.gson.*;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.json.JsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * The namespace and path must contain only lowercase letters ([a-z]), digits ([0-9]), or the characters '_', '.', and '-'. The path can also contain the standard path separator '/'.
 */
public class Identifier implements Comparable<Identifier> {
    public static final Codec<Identifier> CODEC = Codec.STRING.comapFlatMap(Identifier::validate, Identifier::toString).stable();
    public static final char NAMESPACE_SEPARATOR = ':';
    public static final String DEFAULT_NAMESPACE = "minecraft";
    public static final String REALMS_NAMESPACE = "realms";
    private static final SimpleCommandExceptionType COMMAND_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("argument.id.invalid"));
    protected final String namespace;
    protected final String path;

    /**
     * <p>Takes a string of the form {@code <namespace>:<path>}, for example {@code minecraft:iron_ingot}.
     * <p>The string will be split (on the {@code :}) into an identifier with the specified path and namespace.
     * Prefer using the {@link net.minecraft.util.identifier.Identifier#Identifier(java.lang.String, java.lang.String) Identifier(java.lang.String, java.lang.String)} constructor that takes the namespace and path as individual parameters to avoid mistakes.
     *
     * @throws InvalidIdentifierException
     *         if the string cannot be parsed as an identifier.
     */
    public Identifier(String id) {
        this(Identifier.split(id, ':'));
    }

    protected Identifier(String[] id) {
        this.namespace = StringUtils.isEmpty(id[0]) ? DEFAULT_NAMESPACE : id[0];
        this.path = id[1];
        if (! Identifier.isNamespaceValid(this.namespace)) {
            throw new InvalidIdentifierException("Non [a-z0-9_.-] character in namespace of location: " + this.namespace + ":" + this.path);
        }
        if (! Identifier.isPathValid(this.path)) {
            throw new InvalidIdentifierException("Non [a-z0-9/._-] character in path of location: " + this.namespace + ":" + this.path);
        }
    }

    private static boolean isPathValid(String path) {
        for (int i = 0; i < path.length(); ++ i) {
            if (Identifier.isPathCharacterValid(path.charAt(i)))
                continue;
            return false;
        }
        return true;
    }

    public static boolean isPathCharacterValid(char character) {
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '/' || character == '.';
    }

    private static boolean isNamespaceValid(String namespace) {
        for (int i = 0; i < namespace.length(); ++ i) {
            if (Identifier.isNamespaceCharacterValid(namespace.charAt(i)))
                continue;
            return false;
        }
        return true;
    }

    private static boolean isNamespaceCharacterValid(char character) {
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '.';
    }

    protected static String[] split(String id, char delimiter) {
        String[] strings = new String[]{DEFAULT_NAMESPACE, id};
        int i = id.indexOf(delimiter);
        if (i >= 0) {
            strings[1] = id.substring(i + 1);
            if (i >= 1) {
                strings[0] = id.substring(0, i);
            }
        }
        return strings;
    }

    public Identifier(String namespace, String path) {
        this(new String[]{namespace, path});
    }

    public static Identifier splitOn(String id, char delimiter) {
        return new Identifier(Identifier.split(id, delimiter));
    }

    /**
     * <p>Parses a string into an {@code Identifier}.
     * Takes a string of the form {@code <namespace>:<path>}, for example {@code minecraft:iron_ingot}.
     *
     * @return resulting identifier, or {@code null} if the string couldn't be parsed as an identifier
     */
    @Nullable
    public static Identifier tryParse(String id) {
        try {
            return new Identifier(id);
        } catch (InvalidIdentifierException invalidIdentifierException) {
            return null;
        }
    }

    private static DataResult<Identifier> validate(String id) {
        try {
            return DataResult.success(new Identifier(id));
        } catch (InvalidIdentifierException invalidIdentifierException) {
            return DataResult.error("Not a valid resource location: " + id + " " + invalidIdentifierException.getMessage());
        }
    }

    public static Identifier fromCommandInput(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        while (reader.canRead() && Identifier.isCharValid(reader.peek())) {
            reader.skip();
        }
        String string = reader.getString().substring(i, reader.getCursor());
        try {
            return new Identifier(string);
        } catch (InvalidIdentifierException invalidIdentifierException) {
            reader.setCursor(i);
            throw COMMAND_EXCEPTION.createWithContext(reader);
        }
    }

    public static boolean isCharValid(char c) {
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c == '_' || c == ':' || c == '/' || c == '.' || c == '-';
    }

    public static boolean isValid(String id) {
        String[] strings = Identifier.split(id, ':');
        return Identifier.isNamespaceValid(StringUtils.isEmpty(strings[0]) ? DEFAULT_NAMESPACE : strings[0]) && Identifier.isPathValid(strings[1]);
    }

    public String getPath() {
        return this.path;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Identifier) {
            Identifier identifier = (Identifier) o;
            return this.namespace.equals(identifier.namespace) && this.path.equals(identifier.path);
        }
        return false;
    }

    public String toString() {
        return this.namespace + ":" + this.path;
    }

    @Override
    public int compareTo(Identifier identifier) {
        int i = this.path.compareTo(identifier.path);
        if (i == 0) {
            i = this.namespace.compareTo(identifier.namespace);
        }
        return i;
    }

    public String toUnderscoreSeparatedString() {
        return this.toString().replace('/', '_').replace(':', '_');
    }

    public static class Serializer implements JsonDeserializer<Identifier>, JsonSerializer<Identifier> {
        @Override
        public Identifier deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Identifier(JsonHelper.asString(jsonElement, "location"));
        }

        @Override
        public JsonElement serialize(Identifier identifier, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(identifier.toString());
        }
    }
}

