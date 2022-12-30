package com.example.playercountweb;

import com.mojang.logging.LogUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.OutputStream;

public class WebHandler implements HttpHandler {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final MinecraftServer server;

    public WebHandler(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LOGGER.info("Handle request");
        final int playerCount = server.getPlayerCount();
        final String responseString = "{\"players\": {\"count\": " + playerCount + "}}";

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, responseString.length());
        OutputStream os = exchange.getResponseBody();
        os.write(responseString.getBytes());
        os.close();
    }
}
