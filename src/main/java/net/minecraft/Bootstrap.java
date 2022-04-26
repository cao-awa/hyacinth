package net.minecraft;

import com.github.cao.awa.hyacinth.constants.*;
import com.github.cao.awa.hyacinth.language.*;
import com.github.cao.awa.hyacinth.logging.*;
import com.github.cao.awa.hyacinth.server.entity.*;
import net.minecraft.tag.*;
import net.minecraft.util.registry.*;
import org.apache.logging.log4j.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class Bootstrap {
    public static final PrintStream SYSOUT = System.out;
    private static final Logger LOGGER;
    private static volatile boolean initialized;

    static {
        LOGGER = LogManager.getLogger();
    }

    public static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        if (Registry.REGISTRIES.getIds().isEmpty()) {
            throw new IllegalStateException("Unable to load registries");
        }
//        FireBlock.registerDefaultFlammables();
//        ComposterBlock.registerDefaultCompostableItems();
        if (EntityType.getId(EntityType.PLAYER) == null) {
            throw new IllegalStateException("Failed loading EntityTypes");
        }
//        BrewingRecipeRegistry.registerDefaults();
//        EntitySelectorOptions.register();
//        DispenserBehavior.registerDefaults();
//        CauldronBehavior.registerBehavior();
//        ArgumentTypes.register();
        RequiredTagListRegistry.validateRegistrations();
        Bootstrap.setOutputStreams();
    }

    private static void setOutputStreams() {
        if (LOGGER.isDebugEnabled()) {
            System.setErr(new DebugLoggerPrintStream("STDERR", System.err));
            System.setOut(new DebugLoggerPrintStream("STDOUT", SYSOUT));
        } else {
            System.setErr(new LoggerPrintStream("STDERR", System.err));
            System.setOut(new LoggerPrintStream("STDOUT", SYSOUT));
        }
    }

    public static void logMissing() {
        Bootstrap.ensureBootstrapped(() -> "validate");
        if (SharedConstants.isDevelopment) {
//            Bootstrap.getMissingTranslations().forEach(key -> LOGGER.error("Missing translations: {}", key));
//            CommandManager.checkMissing();
//            Bootstrap.logMissingBiomePlacementModifier();
        }
//        DefaultAttributeRegistry.checkMissing();
    }

//    public static Set<String> getMissingTranslations() {
//        TreeSet<String> set = new TreeSet<String>();
//        Bootstrap.collectMissingTranslations(Registry.ATTRIBUTE, EntityAttribute::getTranslationKey, set);
//        Bootstrap.collectMissingTranslations(Registry.ENTITY_TYPE, EntityType::getTranslationKey, set);
//        Bootstrap.collectMissingTranslations(Registry.STATUS_EFFECT, StatusEffect::getTranslationKey, set);
//        Bootstrap.collectMissingTranslations(Registry.ITEM, Item::getTranslationKey, set);
//        Bootstrap.collectMissingTranslations(Registry.ENCHANTMENT, Enchantment::getTranslationKey, set);
//        Bootstrap.collectMissingTranslations(Registry.BLOCK, Block::getTranslationKey, set);
//        Bootstrap.collectMissingTranslations(Registry.CUSTOM_STAT, stat -> "stat." + stat.toString().replace(':', '.'), set);
//        Bootstrap.collectMissingGameRuleTranslations(set);
//        return set;
//    }

    private static <T> void collectMissingTranslations(Iterable<T> registry, Function<T, String> keyExtractor, Set<String> translationKeys) {
        Language language = Language.getInstance();
        registry.forEach(object -> {
            String string = keyExtractor.apply(object);
            if (! language.hasTranslation(string)) {
                translationKeys.add(string);
            }
        });
    }

    public static void ensureBootstrapped(Supplier<String> callerGetter) {
        if (! initialized) {
            throw Bootstrap.createNotBootstrappedException(callerGetter);
        }
    }

    private static RuntimeException createNotBootstrappedException(Supplier<String> callerGetter) {
        try {
            String string = callerGetter.get();
            return new IllegalArgumentException("Not bootstrapped (called from " + string + ")");
        } catch (Exception string) {
            IllegalArgumentException runtimeException = new IllegalArgumentException("Not bootstrapped (failed to resolve location)");
            runtimeException.addSuppressed(string);
            return runtimeException;
        }
    }

    public static void println(String str) {
        SYSOUT.println(str);
    }
}

