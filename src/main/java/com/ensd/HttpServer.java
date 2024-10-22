package com.ensd;

import com.ensd.core.ServerListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class HttpServer {
    private final static int PORT = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args)  {
        try {
            ServerListener serverListener = new ServerListener(8080);
            LOGGER.info("Server running on PORT: " + PORT);
            serverListener.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
