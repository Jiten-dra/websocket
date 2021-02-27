package Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

public class ClientApp {
    @Autowired
    private WebSocketStompClient stompClient;
    private StompSession stompSession;
    private CustomStompSessionHandler customStompSessionHandler;


    public void connect() throws ExecutionException, InterruptedException {
        StompSessionHandler sessionHandler = new CustomStompSessionHandler();

        stompSession = stompClient.connect("/greetings", sessionHandler).get();
    }
    public void send(){
        stompSession.send("topic/greetings", "Hello new user");
    }
    public void subscribe(){
        stompSession.subscribe("topic/greetings", customStompSessionHandler);
    }



    public static void main(String[] args) {
        ClientApp clientApp =new ClientApp();
        try {
            clientApp.connect();
            clientApp.subscribe();
            clientApp.send();
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
