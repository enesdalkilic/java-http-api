package com.ensd;

//CORE

import com.ensd.core.ServerListener;

//Dependencies
import com.ensd.database.Database;
import com.ensd.session.SessionManager;
import com.ensd.utils.SnowflakeID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Exceptions
import java.io.*;

public class HttpServer {
    private final static int PORT = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        try {
            ServerListener serverListener = new ServerListener(8080);
            LOGGER.info("Server running on PORT: " + PORT);

            SnowflakeID.setup(1728835510, 1, 5);
            Database.connect();


            SessionManager.fetchSessions();
            serverListener.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
