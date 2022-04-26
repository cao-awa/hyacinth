package net.minecraft.resource;

import com.github.cao.awa.hyacinth.network.text.*;
import com.google.common.base.*;
import com.google.common.collect.*;
import net.minecraft.resource.metadata.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.Objects;
import java.util.stream.*;

/**
 * A resource pack manager manages a list of {@link ResourcePackProfile}s and
 * builds {@linkplain #createResourcePacks() a list of resource packs} when the
 * resource manager reloads.
 */
public class ResourcePackManager
implements AutoCloseable {
    private final Set<ResourcePackProvider> providers;
    private Map<String, ResourcePackProfile> profiles = ImmutableMap.of();
    private List<ResourcePackProfile> enabled = ImmutableList.of();
    private final ResourcePackProfile.Factory profileFactory;

    public ResourcePackManager(ResourcePackProfile.Factory profileFactory, ResourcePackProvider ... providers) {
        this.profileFactory = profileFactory;
        this.providers = ImmutableSet.copyOf(providers);
    }

//    public ResourcePackManager(ResourceType type, ResourcePackProvider ... providers) {
//        this((String name, Text displayName, boolean alwaysEnabled, Supplier<ResourcePack> packFactory, PackResourceMetadata metadata, ResourcePackProfile.InsertionPosition direction, ResourcePackSource source) -> new ResourcePackProfile(name, displayName, alwaysEnabled, packFactory, metadata, type, direction, source), providers);
//    }

    public void scanPacks() {
        List<String> list = this.enabled.stream().map(ResourcePackProfile::getName).collect(ImmutableList.toImmutableList());
        this.close();
        this.profiles = this.providePackProfiles();
        this.enabled = this.buildEnabledProfiles(list);
    }

    private Map<String, ResourcePackProfile> providePackProfiles() {
        TreeMap<String, ResourcePackProfile> map = Maps.newTreeMap();
        for (ResourcePackProvider resourcePackProvider : this.providers) {
            resourcePackProvider.register(profile -> map.put(profile.getName(), profile), this.profileFactory);
        }
        return ImmutableMap.copyOf(map);
    }

    public void setEnabledProfiles(Collection<String> enabled) {
        this.enabled = this.buildEnabledProfiles(enabled);
    }

    private List<ResourcePackProfile> buildEnabledProfiles(Collection<String> enabledNames) {
        List<ResourcePackProfile> list = this.streamProfilesByName(enabledNames).collect(Collectors.toList());
        for (ResourcePackProfile resourcePackProfile : this.profiles.values()) {
            if (!resourcePackProfile.isAlwaysEnabled() || list.contains(resourcePackProfile)) continue;
            resourcePackProfile.getInitialPosition().insert(list, resourcePackProfile, Functions.identity(), false);
        }
        return ImmutableList.copyOf(list);
    }

    private Stream<ResourcePackProfile> streamProfilesByName(Collection<String> names) {
        return names.stream().map(this.profiles::get).filter(Objects::nonNull);
    }

    public Collection<String> getNames() {
        return this.profiles.keySet();
    }

    public Collection<ResourcePackProfile> getProfiles() {
        return this.profiles.values();
    }

    public Collection<String> getEnabledNames() {
        return this.enabled.stream().map(ResourcePackProfile::getName).collect(ImmutableSet.toImmutableSet());
    }

    public Collection<ResourcePackProfile> getEnabledProfiles() {
        return this.enabled;
    }

    @Nullable
    public ResourcePackProfile getProfile(String name) {
        return this.profiles.get(name);
    }

    @Override
    public void close() {
        this.profiles.values().forEach(ResourcePackProfile::close);
    }

    public boolean hasProfile(String name) {
        return this.profiles.containsKey(name);
    }

    public List<ResourcePack> createResourcePacks() {
        return this.enabled.stream().map(ResourcePackProfile::createResourcePack).collect(ImmutableList.toImmutableList());
    }
}

