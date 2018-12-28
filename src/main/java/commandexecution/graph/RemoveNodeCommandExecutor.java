package commandexecution.graph;

import commandexecution.DefaultCommandExecutor;
import exceptions.NodeNotFoundException;
import graph.Graph;

import java.util.regex.Matcher;

import static messages.Responses.ERROR_NODE_NOT_FOUND;
import static messages.Responses.NODE_REMOVED;

/**
 * Extracts a node name from the given command and remove that node from the graph.
 * Returns an error message if the node does not exist.
 */
public class RemoveNodeCommandExecutor extends DefaultCommandExecutor {
    public RemoveNodeCommandExecutor(final Matcher command) {
        super(command);
    }

    @Override
    public String getResponse() {

        try {
            Graph.getInstance().removeNode(command.group(1));
        } catch (final NodeNotFoundException e) {
            return ERROR_NODE_NOT_FOUND;
        }

        return NODE_REMOVED;
    }
}