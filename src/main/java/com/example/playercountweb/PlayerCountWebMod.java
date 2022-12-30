package com.example.playercountweb;

import com.mojang.logging.LogUtils;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

@Mod(PlayerCountWebMod.MODID)
public class PlayerCountWebMod {
    public static final String MODID = "playercountweb";

    public static final int PORT = 8000;

    private static final Logger LOGGER = LogUtils.getLogger();

    private HttpServer httpServer;

    public PlayerCountWebMod() {
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStopping);
    }

    private void onServerStarted(ServerStartedEvent event) {
        MinecraftServer minecraftServer = event.getServer();
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/", new WebHandler(minecraftServer));
            httpServer.setExecutor(null);
            httpServer.start();
            LOGGER.info("HTTP Server started on port: " + PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onServerStopping(ServerStoppingEvent event) {
        final int WITHOUT_DELAY = 0;
        httpServer.stop(WITHOUT_DELAY);
    }
}
