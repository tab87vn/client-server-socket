package com.collibra.codechallenge;

import com.collibra.codechallenge.server.TCPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Well, this is where the program starts.
 */
public class Program {
    private static final int PORT_NUMBER = 50000;
    private static final Logger LOGGER = LogManager.getLogger(Program.class);

    public static void main(String[] args) {
        LOGGER.info("Starting server...");
        new TCPServer().start(PORT_NUMBER);
    }
}