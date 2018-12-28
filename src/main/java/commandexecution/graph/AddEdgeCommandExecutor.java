package commandexecution.graph;

import commandexecution.CommandExecutor;
import graph.Graph;
import exceptions.NodeNotFoundException;

import static messages.Commands.CMD_ADD_EDGE;
import static messages.Responses.RSP_EDGE_ADDED;
import static messages.Responses.RSP_ERROR_NODE_NOT_FOUND;

public class AddEdgeCommandExecutor implements CommandExecutor {
    private String command;

    public AddEdgeCommandExecutor(final String command) {
        this.command = command;
    }

    @Override
    public String getResponse() {
        String edge = command.replace(CMD_ADD_EDGE, "");
        String[] s = edge.split(" ");
        try {
            int weight = Integer.parseInt(s[2]);
            Graph.getInstance().addEdge(s[0], s[1], weight);

        } catch (NodeNotFoundException e) {
            return RSP_ERROR_NODE_NOT_FOUND;
        }

        return RSP_EDGE_ADDED;
    }
}
