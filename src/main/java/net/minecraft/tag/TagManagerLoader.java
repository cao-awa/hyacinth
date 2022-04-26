package net.minecraft.tag;

import com.google.common.collect.*;
import net.minecraft.resource.*;
import net.minecraft.util.identifier.*;
import net.minecraft.util.profiler.*;
import net.minecraft.util.registry.*;
import org.apache.logging.log4j.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class TagManagerLoader
implements ResourceReloader {
    private static final Logger LOGGER = LogManager.getLogger();
    private final DynamicRegistryManager registryManager;
    private TagManager tagManager = TagManager.EMPTY;

    public TagManagerLoader(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    public TagManager getTagManager() {
        return this.tagManager;
    }

    @Override
    public CompletableFuture<Void> reload(ResourceReloader.Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        ArrayList<RequiredGroup<?>> list = Lists.newArrayList();
        RequiredTagListRegistry.forEach(requiredTagList -> {
            RequiredGroup<?> requiredGroup = this.buildRequiredGroup(manager, prepareExecutor, (RequiredTagList)requiredTagList);
            if (requiredGroup != null) {
                list.add(requiredGroup);
            }
        });
        return (CompletableFuture.allOf(list.stream().map(requiredGroup -> requiredGroup.groupLoadFuture).toArray(CompletableFuture[]::new)).thenCompose(synchronizer::whenPrepared)).thenAcceptAsync(void_ -> {
            TagManager.Builder builder = new TagManager.Builder();
            list.forEach(requiredGroup -> requiredGroup.addTo(builder));
            TagManager tagManager = builder.build();
            Multimap<RegistryKey<? extends Registry<?>>, Identifier> multimap = RequiredTagListRegistry.getMissingTags(tagManager);
            if (!multimap.isEmpty()) {
                throw new IllegalStateException("Missing required tags: " + multimap.entries().stream().map(entry -> entry.getKey() + ":" + entry.getValue()).sorted().collect(Collectors.joining(",")));
            }
            ServerTagManagerHolder.setTagManager(tagManager);
            this.tagManager = tagManager;
        }, applyExecutor);
    }

    @Nullable
    private <T> RequiredGroup<T> buildRequiredGroup(ResourceManager resourceManager, Executor prepareExecutor, RequiredTagList<T> requirement) {
        Optional<? extends Registry<T>> optional = this.registryManager.getOptional(requirement.getRegistryKey());
        if (optional.isPresent()) {
            Registry<T> registry = optional.get();
            TagGroupLoader<T> tagGroupLoader = new TagGroupLoader<>(registry::getOrEmpty, requirement.getDataType());
            CompletableFuture<TagGroup<T>> completableFuture = CompletableFuture.supplyAsync(() -> tagGroupLoader.load(resourceManager), prepareExecutor);
            return new RequiredGroup<>(requirement, completableFuture);
        }
        LOGGER.warn("Can't find registry for {}", requirement.getRegistryKey());
        return null;
    }

    static class RequiredGroup<T> {
        private final RequiredTagList<T> requirement;
        final CompletableFuture<? extends TagGroup<T>> groupLoadFuture;

        RequiredGroup(RequiredTagList<T> requirement, CompletableFuture<? extends TagGroup<T>> groupLoadFuture) {
            this.requirement = requirement;
            this.groupLoadFuture = groupLoadFuture;
        }

        public void addTo(TagManager.Builder builder) {
            builder.add(this.requirement.getRegistryKey(), this.groupLoadFuture.join());
        }
    }
}

