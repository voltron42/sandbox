package routing;

/**
 * Created by daniel.johnson on 6/8/2017.
 */
public class ServerException extends Exception {
    private final int status;

    public ServerException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
