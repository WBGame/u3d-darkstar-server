package ar.com.cubenet.server.leasson5;

import java.io.Serializable;
import java.util.Properties;

import com.sun.sgs.app.AppListener;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;


public class AppListenerImpl  implements AppListener, Serializable {
    private static final long serialVersionUID = 1L;

    public void initialize(Properties props) {
        System.out.println("se inicializó el server.");
    }

    public ClientSessionListener loggedIn(ClientSession session) {
        return new ClientListener(session);
    }
}
