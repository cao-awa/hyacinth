package com.github.cao.awa.hyacinth;

import com.github.cao.awa.hyacinth.network.ServerNetworkIo;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.server.MinecraftServer;
import com.github.cao.awa.hyacinth.server.task.ServerTask;
import com.github.zhuaidadaya.rikaishinikui.handler.times.TimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.jmx.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import static com.github.cao.awa.hyacinth.constants.SharedConstants.createGameVersion;

public class HyacinthServer extends MinecraftServer {
    private static final Logger LOGGER = LogManager.getLogger("Hyacinth");

    public HyacinthServer(Thread serverThread) throws Exception {
        super(serverThread);
    }

    @Override
    protected ServerTask createTask(Runnable var1) {
        return null;
    }

    @Override
    protected boolean canExecute(ServerTask var1) {
        return false;
    }

    @Override
    public void runServer() {
        while (true) {
            tick();

            try {
                TimeUtil.sleep(20);
            } catch (InterruptedException e) {

            }
        }
    }

    @Override
    public void sendSystemMessage(Text var1, UUID var2) {

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

    public void tick() {
        ++ this.ticks;
        getNetworkIo().tick();
    }

    public static void main(String[] args) {
        try {
            createGameVersion();
            MinecraftServer server = MinecraftServer.startServer(t -> {
                try {
                    return new HyacinthServer(t);
                } catch (Exception e) {

                }
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        SharedConstants.createGameVersion();
//        OptionParser optionParser = new OptionParser();
//        OptionSpecBuilder optionSpec = optionParser.accepts("nogui");
//        OptionSpecBuilder optionSpec2 = optionParser.accepts("initSettings", "Initializes 'server.properties' and 'eula.txt', then quits");
//        OptionSpecBuilder optionSpec3 = optionParser.accepts("demo");
//        OptionSpecBuilder optionSpec4 = optionParser.accepts("bonusChest");
//        OptionSpecBuilder optionSpec5 = optionParser.accepts("forceUpgrade");
//        OptionSpecBuilder optionSpec6 = optionParser.accepts("eraseCache");
//        OptionSpecBuilder optionSpec7 = optionParser.accepts("safeMode", "Loads level with vanilla datapack only");
//        AbstractOptionSpec<Void> optionSpec8 = optionParser.accepts("help").forHelp();
//        ArgumentAcceptingOptionSpec<String> optionSpec9 = optionParser.accepts("singleplayer").withRequiredArg();
//        ArgumentAcceptingOptionSpec<String> optionSpec10 = optionParser.accepts("universe").withRequiredArg().defaultsTo(".");
//        ArgumentAcceptingOptionSpec<String> optionSpec11 = optionParser.accepts("world").withRequiredArg();
//        ArgumentAcceptingOptionSpec<Integer> optionSpec12 = optionParser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(- 1);
//        ArgumentAcceptingOptionSpec<String> optionSpec13 = optionParser.accepts("serverId").withRequiredArg();
//        OptionSpecBuilder optionSpec14 = optionParser.accepts("jfrProfile");
//        NonOptionArgumentSpec<String> optionSpec15 = optionParser.nonOptions();
//        try {
//            Object serverPropertiesHandler;
//            ServerResourceManager serverResourceManager;
//            OptionSet optionSet = optionParser.parse(args);
//            if (optionSet.has(optionSpec8)) {
//                optionParser.printHelpOn(System.err);
//                return;
//            }
//            CrashReport.initCrashReport();
//            if (optionSet.has(optionSpec14)) {
//                FlightProfiler.INSTANCE.start(InstanceType.SERVER);
//            }
//            Bootstrap.initialize();
//            Bootstrap.logMissing();
//            Util.startTimerHack();
//            DynamicRegistryManager.Impl impl = DynamicRegistryManager.create();
//            Path path = Paths.get("server.properties");
//            ServerPropertiesLoader serverPropertiesLoader = new ServerPropertiesLoader(path);
//            serverPropertiesLoader.store();
//            Path path2 = Paths.get("eula.txt");
//            EulaReader eulaReader = new EulaReader(path2);
//            if (optionSet.has(optionSpec2)) {
//                LOGGER.info("Initialized '{}' and '{}'", path.toAbsolutePath(), path2.toAbsolutePath());
//                return;
//            }
//            if (! eulaReader.isEulaAgreedTo()) {
//                LOGGER.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
//                return;
//            }
//            File file = new File(optionSet.valueOf(optionSpec10));
//            YggdrasilAuthenticationService yggdrasilAuthenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY);
//            MinecraftSessionService minecraftSessionService = yggdrasilAuthenticationService.createMinecraftSessionService();
//            GameProfileRepository gameProfileRepository = yggdrasilAuthenticationService.createProfileRepository();
//            UserCache userCache = new UserCache(gameProfileRepository, new File(file, MinecraftServer.USER_CACHE_FILE.getName()));
//            String string = Optional.ofNullable(optionSet.valueOf(optionSpec11)).orElse(serverPropertiesLoader.getPropertiesHandler().levelName);
//            LevelStorage levelStorage = LevelStorage.create(file.toPath());
//            LevelStorage.Session session = levelStorage.createSession(string);
//            LevelSummary levelSummary = session.getLevelSummary();
//            if (levelSummary != null) {
//                if (levelSummary.requiresConversion()) {
//                    LOGGER.info("This world must be opened in an older version (like 1.6.4) to be safely converted");
//                    return;
//                }
//                if (! levelSummary.isVersionAvailable()) {
//                    LOGGER.info("This world was created by an incompatible version.");
//                    return;
//                }
//            }
//            DataPackSettings dataPackSettings = session.getDataPackSettings();
//            boolean bl = optionSet.has(optionSpec7);
//            if (bl) {
//                LOGGER.warn("Safe mode active, only vanilla datapack will be loaded");
//            }
//            ResourcePackManager resourcePackManager = new ResourcePackManager(ResourceType.SERVER_DATA, new VanillaDataPackProvider(), new FileResourcePackProvider(session.getDirectory(WorldSavePath.DATAPACKS).toFile(), ResourcePackSource.PACK_SOURCE_WORLD));
//            DataPackSettings dataPackSettings2 = MinecraftServer.loadDataPacks(resourcePackManager, dataPackSettings == null ? DataPackSettings.SAFE_MODE : dataPackSettings, bl);
//            CompletableFuture<ServerResourceManager> completableFuture = ServerResourceManager.reload(resourcePackManager.createResourcePacks(), impl, CommandManager.RegistrationEnvironment.DEDICATED, serverPropertiesLoader.getPropertiesHandler().functionPermissionLevel, Util.getMainWorkerExecutor(), Runnable::run);
//            try {
//                serverResourceManager = completableFuture.get();
//            } catch (Exception exception) {
//                LOGGER.warn("Failed to load datapacks, can't proceed with server load. You can either fix your datapacks or reset to vanilla with --safeMode", (Throwable) exception);
//                resourcePackManager.close();
//                return;
//            }
//            serverResourceManager.loadRegistryTags();
//            RegistryOps<NbtElement> exception = RegistryOps.ofLoaded(NbtOps.INSTANCE, serverResourceManager.getResourceManager(), (DynamicRegistryManager) impl);
//            serverPropertiesLoader.getPropertiesHandler().getGeneratorOptions(impl);
//            SaveProperties saveProperties = session.readLevelProperties(exception, dataPackSettings2);
//            if (saveProperties == null) {
//                if (optionSet.has(optionSpec3)) {
//                    levelInfo = MinecraftServer.DEMO_LEVEL_INFO;
//                    generatorOptions = GeneratorOptions.createDemo(impl);
//                } else {
//                    serverPropertiesHandler = serverPropertiesLoader.getPropertiesHandler();
//                    levelInfo = new LevelInfo(((ServerPropertiesHandler) serverPropertiesHandler).levelName, ((ServerPropertiesHandler) serverPropertiesHandler).gameMode, ((ServerPropertiesHandler) serverPropertiesHandler).hardcore, ((ServerPropertiesHandler) serverPropertiesHandler).difficulty, false, new GameRules(), dataPackSettings2);
//                    generatorOptions = optionSet.has(optionSpec4) ? ((ServerPropertiesHandler) serverPropertiesHandler).getGeneratorOptions(impl).withBonusChest() : ((ServerPropertiesHandler) serverPropertiesHandler).getGeneratorOptions(impl);
//                }
//                saveProperties = new LevelProperties((LevelInfo) levelInfo, (GeneratorOptions) generatorOptions, Lifecycle.stable());
//            }
//            if (optionSet.has(optionSpec5)) {
//                Main.forceUpgradeWorld(session, Schemas.getFixer(), optionSet.has(optionSpec6), () -> true, saveProperties.getGeneratorOptions());
//            }
//            session.backupLevelDataFile(impl, saveProperties);
//            levelInfo = saveProperties;
//            generatorOptions = MinecraftServer.startServer(arg_0 -> startServer(impl, session, resourcePackManager, serverResourceManager, (SaveProperties) levelInfo, serverPropertiesLoader, minecraftSessionService, gameProfileRepository, userCache, optionSet, optionSpec9, optionSpec12, optionSpec3, optionSpec13, optionSpec, optionSpec15, arg_0));
//            serverPropertiesHandler = new Thread(() -> ((MinecraftDedicatedServer) generatorOptions).stop(true));
//            ((Thread) serverPropertiesHandler).setUncaughtExceptionHandler(new UncaughtExceptionLogger(LOGGER));
//            Runtime.getRuntime().addShutdownHook((Thread) serverPropertiesHandler);
//        } catch (Exception optionSet) {
//            LOGGER.fatal("Failed to start the minecraft server", optionSet);
//        }
//    }
//
//    private static MinecraftDedicatedServer startServer(DynamicRegistryManager.Impl registryTracker, LevelStorage.Session session, ResourcePackManager resourcePackManager, ServerResourceManager serverResourceManager, SaveProperties saveProperties, ServerPropertiesLoader propertiesLoader, MinecraftSessionService sessionService, GameProfileRepository profileRepository, UserCache userCache, OptionSet optionSet, OptionSpec<?> singleplayer, OptionSpec<?> serverPort, OptionSpec<?> demo, OptionSpec<?> serverId, OptionSpec<?> noGui, OptionSpec<?> nonOptions, Thread serverThread) {
//        boolean bl;
//        MinecraftDedicatedServer minecraftDedicatedServer = new MinecraftDedicatedServer(serverThread, registryTracker, session, resourcePackManager, serverResourceManager, saveProperties, propertiesLoader, Schemas.getFixer(), sessionService, profileRepository, userCache, WorldGenerationProgressLogger::new);
//        minecraftDedicatedServer.setSinglePlayerName((String) optionSet.valueOf(singleplayer));
//        minecraftDedicatedServer.setServerPort((Integer) optionSet.valueOf(serverPort));
//        minecraftDedicatedServer.setDemo(optionSet.has(demo));
//        minecraftDedicatedServer.setServerId((String) optionSet.valueOf(serverId));
//        bl = ! optionSet.has(noGui) && ! optionSet.valuesOf(nonOptions).contains("nogui");
//        if (bl && ! GraphicsEnvironment.isHeadless()) {
//            minecraftDedicatedServer.createGui();
//        }
//        return minecraftDedicatedServer;
//    }
//
//    private static void forceUpgradeWorld(LevelStorage.Session session, DataFixer dataFixer, boolean eraseCache, BooleanSupplier continueCheck, GeneratorOptions generatorOptions) {
//        LOGGER.info("Forcing world upgrade!");
//        WorldUpdater worldUpdater = new WorldUpdater(session, dataFixer, generatorOptions, eraseCache);
//        Text text = null;
//        while (! worldUpdater.isDone()) {
//            int i;
//            Text text2 = worldUpdater.getStatus();
//            if (text != text2) {
//                text = text2;
//                LOGGER.info(worldUpdater.getStatus().getString());
//            }
//            if ((i = worldUpdater.getTotalChunkCount()) > 0) {
//                int j = worldUpdater.getUpgradedChunkCount() + worldUpdater.getSkippedChunkCount();
//                LOGGER.info("{}% completed ({} / {} chunks)...", MathHelper.floor((float) j / (float) i * 100.0f), j, i);
//            }
//            if (! continueCheck.getAsBoolean()) {
//                worldUpdater.cancel();
//                continue;
//            }
//            try {
//                Thread.sleep(1000L);
//            } catch (InterruptedException interruptedException) {
//            }
//        }
//    }
}
