package de.ecotram.backend.handler;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log(topic = "simulation socket")
@Component("socketHandler")
public class SimulationSocketHandler {

    @Autowired
    public SimulationSocketHandler(SocketIOServer server) {
        server.addNamespace("/simulation");
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
    }

    private ConnectListener onConnected() {
        return client -> {
            this.log.info("Client[" + client.getSessionId().toString() + "] - Connected to simulation socket");
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            this.log.info("Client[" + client.getSessionId().toString() + "] - disconnected from simulation socket");
        };
    }
}
