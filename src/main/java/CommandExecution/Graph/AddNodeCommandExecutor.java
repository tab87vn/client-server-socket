package CommandExecution.Graph;

import CommandExecution.CommandExecutor;
import Graph.Graph;
import Graph.NodeAlreadyExistsException;

import static Messages.Commands.CMD_ADD_NODE;
import static Messages.Responses.RSP_ERROR_NODE_ALREADY_EXISTS;
import static Messages.Responses.RSP_NODE_ADDED;

public class AddNodeCommandExecutor implements CommandExecutor {
    private String command;
    private Graph graph;

    public AddNodeCommandExecutor(final String command, final Graph graph) {
        this.command = command;
        this.graph = graph;
    }

    @Override
    public String getResponse() {
        final String nodeName = command.replace(CMD_ADD_NODE, "");
        try {
            graph.addNode(nodeName);
        } catch (NodeAlreadyExistsException e) {
            return RSP_ERROR_NODE_ALREADY_EXISTS;
        }

        return RSP_NODE_ADDED;
    }
}
