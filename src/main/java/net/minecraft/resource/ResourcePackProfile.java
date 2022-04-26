package net.minecraft.resource;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.cao.awa.hyacinth.network.text.*;
import com.github.cao.awa.hyacinth.network.text.event.*;
import com.github.cao.awa.hyacinth.network.text.style.*;
import net.minecraft.resource.metadata.PackResourceMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a resource pack in a {@link ResourcePackManager}.
 * 
 * <p>Compared to a single-use {@link ResourcePack}, a profile is persistent
 * and serves as {@linkplain #createResourcePack a factory} for the single-use
 * packs. It also contains user-friendly information about resource packs.
 * 
 * <p>The profiles are registered by {@link ResourcePackProvider}s.
 * 
 * <p>Closing the profile doesn't have any effect.
 */
public class ResourcePackProfile
implements AutoCloseable {
    private static final Logger LOGGER = LogManager.getLogger();
    private final String name;
    private final Supplier<ResourcePack> packFactory;
    private final Text displayName;
    private final Text description;
    private final ResourcePackCompatibility compatibility;
    private final InsertionPosition position;
    private final boolean alwaysEnabled;
    private final boolean pinned;
    private final ResourcePackSource source;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    /**
     * Creates a resource pack profile from the given parameters.
     * 
     * <p>Compared to calling the factory directly, this utility method obtains the
     * pack's metadata information from the pack created by the {@code packFactory}.
     * If the created pack doesn't have metadata information, this method returns
     * {@code null}.
     * 
     * @return the created profile, or {@code null} if missing metadata
     */
    @Nullable
    public static ResourcePackProfile of(String name, boolean alwaysEnabled, Supplier<ResourcePack> packFactory, Factory profileFactory, InsertionPosition insertionPosition, ResourcePackSource packSource) {
        try (ResourcePack resourcePack = packFactory.get()){
            PackResourceMetadata packResourceMetadata = resourcePack.parseMetadata(PackResourceMetadata.READER);
            if (packResourceMetadata != null) {
                ResourcePackProfile resourcePackProfile = profileFactory.create(name, new LiteralText(resourcePack.getName()), alwaysEnabled, packFactory, packResourceMetadata, insertionPosition, packSource);
                return resourcePackProfile;
            }
            LOGGER.warn("Couldn't find pack meta for pack {}", name);
            return null;
        }
        catch (IOException resourcePack2) {
            LOGGER.warn("Couldn't get pack info for: {}", resourcePack2.toString());
        }
        return null;
    }

    public ResourcePackProfile(String name, boolean alwaysEnabled, Supplier<ResourcePack> packFactory, Text displayName, Text description, ResourcePackCompatibility compatibility, InsertionPosition direction, boolean pinned, ResourcePackSource source) {
        this.name = name;
        this.packFactory = packFactory;
        this.displayName = displayName;
        this.description = description;
        this.compatibility = compatibility;
        this.alwaysEnabled = alwaysEnabled;
        this.position = direction;
        this.pinned = pinned;
        this.source = source;
    }

    public ResourcePackProfile(String name, Text displayName, boolean alwaysEnabled, Supplier<ResourcePack> packFactory, PackResourceMetadata metadata, ResourceType type, InsertionPosition direction, ResourcePackSource source) {
        this(name, alwaysEnabled, packFactory, displayName, metadata.getDescription(), ResourcePackCompatibility.from(metadata, type), direction, false, source);
    }

    public Text getDisplayName() {
        return this.displayName;
    }

    public Text getDescription() {
        return this.description;
    }

//    public Text getInformationText(boolean enabled) {
//        return Texts.bracketed(this.source.decorate(new LiteralText(this.name))).styled(style -> style.withColor(enabled ? Formatting.GREEN : Formatting.RED).withInsertion(StringArgumentType.escapeIfRequired(this.name)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("").append(this.displayName).append("\n").append(this.description))));
//    }

    public ResourcePackCompatibility getCompatibility() {
        return this.compatibility;
    }

    public ResourcePack createResourcePack() {
        return this.packFactory.get();
    }

    public String getName() {
        return this.name;
    }

    public boolean isAlwaysEnabled() {
        return this.alwaysEnabled;
    }

    public boolean isPinned() {
        return this.pinned;
    }

    public InsertionPosition getInitialPosition() {
        return this.position;
    }

    public ResourcePackSource getSource() {
        return this.source;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourcePackProfile resourcePackProfile)) {
            return false;
        }
        return this.name.equals(resourcePackProfile.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public void close() {
    }

    @FunctionalInterface
    public interface Factory {
        @Nullable ResourcePackProfile create(String var1, Text var2, boolean var3, Supplier<ResourcePack> var4, PackResourceMetadata var5, InsertionPosition var6, ResourcePackSource var7);
    }

    public enum InsertionPosition {
        TOP,
        BOTTOM;


        public <T> int insert(List<T> items, T item, Function<T, ResourcePackProfile> profileGetter, boolean listInverted) {
            ResourcePackProfile resourcePackProfile;
            int i;
            InsertionPosition insertionPosition;
            InsertionPosition insertionPosition2 = insertionPosition = listInverted ? this.inverse() : this;
            if (insertionPosition == BOTTOM) {
                ResourcePackProfile resourcePackProfile2;
                int i2;
                for (i2 = 0; i2 < items.size() && (resourcePackProfile2 = profileGetter.apply(items.get(i2))).isPinned() && resourcePackProfile2.getInitialPosition() == this; ++i2) {
                }
                items.add(i2, item);
                return i2;
            }
            for (i = items.size() - 1; i >= 0 && (resourcePackProfile = profileGetter.apply(items.get(i))).isPinned() && resourcePackProfile.getInitialPosition() == this; --i) {
            }
            items.add(i + 1, item);
            return i + 1;
        }

        public InsertionPosition inverse() {
            return this == TOP ? BOTTOM : TOP;
        }
    }
}

