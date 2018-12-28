package commandexecution;

import static messages.Responses.RSP_UNKNOWN_REQUEST;

public class UnknownCommandExecutor implements CommandExecutor {
    @Override
    public String getResponse() {
        return RSP_UNKNOWN_REQUEST;
    }
}