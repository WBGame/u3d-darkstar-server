package ar.com.cubenet.server.leasson5;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.ManagedReference;

public class ClientListener implements Serializable, ClientSessionListener {

    private static final long serialVersionUID = 1L;

    private static final Logger logger =
        Logger.getLogger(ClientListener.class.getName());

    private final ManagedReference<ClientSession> sessionRef;

    public ClientListener(ClientSession session) {
        if (session == null)
            throw new NullPointerException("null session");

        sessionRef = AppContext.getDataManager().createReference(session);
    }
    
    protected ClientSession getSession() {
        return sessionRef.get();
    }

    public void receivedMessage(ByteBuffer message) {
        logger.log(Level.INFO, "Message from {0}", getSession().getName());
    }

    public void disconnected(boolean graceful) {
        String grace = graceful ? "graceful" : "forced";
        logger.log(Level.INFO,
                   "User {0} has logged out {1}",
                   new Object[] { getSession().getName(), grace }
        );
    }
}