package commandexecution.graph;

import commandexecution.CommandExecutor;
import graph.Graph;
import graph.NodeNotFoundException;

import static messages.Commands.CMD_REMOVE_EDGE;
import static messages.Responses.RSP_EDGE_REMOVED;
import static messages.Responses.RSP_ERROR_NODE_NOT_FOUND;

public class RemoveEdgeCommandExecutor implements CommandExecutor {
    private final String command;
    private final Graph graph;

    public RemoveEdgeCommandExecutor(final String command, final Graph graph) {
        this.command = command;
        this.graph = graph;
    }

    @Override
    public String getResponse() {
        String edge = command.replace(CMD_REMOVE_EDGE, "");
        String[] s = edge.split(" ");
        try {
            graph.removeEdge(s[0], s[1]);
        } catch (NodeNotFoundException e) {
            return RSP_ERROR_NODE_NOT_FOUND;
        }

        return RSP_EDGE_REMOVED;
    }
}