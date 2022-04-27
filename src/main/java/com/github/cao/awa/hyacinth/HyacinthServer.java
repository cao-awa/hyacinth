package com.github.cao.awa.hyacinth;

import com.github.cao.awa.hyacinth.logging.*;
import com.github.cao.awa.hyacinth.network.*;
import com.github.cao.awa.hyacinth.network.text.Text;
import com.github.cao.awa.hyacinth.server.MinecraftServer;
import com.github.cao.awa.hyacinth.server.task.ServerTask;
import com.github.zhuaidadaya.rikaishinikui.handler.times.TimeUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.*;
import net.minecraft.*;
import net.minecraft.util.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.*;
import java.util.UUID;
import java.util.function.*;

import static com.github.cao.awa.hyacinth.constants.SharedConstants.createGameVersion;

public class HyacinthServer extends MinecraftServer {
    private static final Logger LOGGER = LogManager.getLogger("Hyacinth");

    public HyacinthServer(Thread serverThread, DynamicRegistryManager.Impl registryManager) throws UnknownHostException {
        super(serverThread, registryManager);
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
        EntrustExecution.notNull(getNetworkIo(), ServerNetworkIo::tick);
    }

    public static void main(String[] args) {
        PrintUtil.debugging = true;

        EntrustExecution.catchingTemporary(() -> {
            createGameVersion();
            Bootstrap.initialize();
            Bootstrap.logMissing();
            MinecraftServer server = MinecraftServer.startServer(t -> EntrustParser.tryCreate(() -> new HyacinthServer(t, DynamicRegistryManager.create()), null));
        }, Throwable::printStackTrace);
    }
}
