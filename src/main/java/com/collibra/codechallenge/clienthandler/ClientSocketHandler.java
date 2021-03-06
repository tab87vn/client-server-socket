package com.collibra.codechallenge.clienthandler;

import com.collibra.codechallenge.communication.CommunicationManager;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.String.format;

/**
 * Takes care of the conversation between the server and a client connected.
 * Delegates the job of executing clients'commands and getting right responses to the
 * {@link CommunicationManager}.
 *
 * This class is meant to run as a thread.
 */
public class ClientSocketHandler implements Runnable {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(ClientSocketHandler.class);
    private static final int CLIENT_TIMEOUT = 30000;
    private static final String CLIENT_SAYS = "Client: %s";
    private static final String SERVER_SAYS = "Server: %s";
    private static final String MSG_TIMED_OUT = "Timed out.";
    private static final String MSG_ERROR_READING_CLIENT_COMMAND = "Error reading client's command...";
    private static final String MSG_ERROR_TERMINATE_CLIENT_SOCKET = "Error terminating client socket...";
    private static final String MSG_ERROR_INITIALISING_CLIENT_SOCKET = "Error initialising client socket...";
    private static final String MSG_ERROR_PARSING_COMMAND = "Error parsing command.";

    private final Socket clientSocket;
    private final CommunicationManager comm;
    private PrintWriter out;
    private BufferedReader in;

    public ClientSocketHandler(final Socket clientSkt) {
        clientSocket = clientSkt;
        comm = new CommunicationManager();
        initialize();
    }

    @Override
    public void run() {
        // The communication starts with server introducing itself...
        sendResponse(comm.getServerGreeting());
        LOGGER.debug(format("Server: %s", comm.getServerGreeting()));

        String command, response;
        try {
            // Reads client's command and looks for the right response
            // until the client wants to stop the conversation
            while ((command = in.readLine()) != null) {
                LOGGER.debug(format(CLIENT_SAYS, command));
                response = comm.getResponse(command);
                sendResponse(response);
                LOGGER.debug(format(SERVER_SAYS, response));

                if (comm.clientSaysGoodBye(command)) {
                    return;
                }
            }
        } catch (final InterruptedIOException e) { // if client says nothing for 30 seconds
            LOGGER.debug(MSG_TIMED_OUT, e);
            sendResponse(comm.getServerGoodbye());
        } catch (final IndexOutOfBoundsException e) { // if somehow the command cannot be parsed
            LOGGER.debug(MSG_ERROR_PARSING_COMMAND, e);
            sendResponse(comm.getServerGoodbye());
        } catch (final IOException e) { // if there's a problem reading what the client sends to the server
            LOGGER.error(MSG_ERROR_READING_CLIENT_COMMAND, e);
        } finally {
            terminate();
        }
    }

    /**
     * Closes client socket and the relevant I/O reader/writer
     */
    private void terminate() {
        try {
            clientSocket.close();
            in.close();
            out.close();
        } catch (final IOException e) {
            LOGGER.error(MSG_ERROR_TERMINATE_CLIENT_SOCKET, e);
        }
    }

    private void sendResponse(final String response) {
        out.println(response);
    }


    /**
     * Gets ready for the incoming and outgoing messages
     */
    private void initialize()  {
        try {
            clientSocket.setSoTimeout(CLIENT_TIMEOUT);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (final IOException e) {
            LOGGER.error(MSG_ERROR_INITIALISING_CLIENT_SOCKET, e);
        }
    }
}