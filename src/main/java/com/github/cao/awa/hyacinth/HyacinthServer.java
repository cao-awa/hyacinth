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
}
