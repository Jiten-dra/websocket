package Client;

//import org.apache.logging.log4j.message.Message;
import com.learn.demowebsocket.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ClientApp {

    private WebSocketStompClient stompClient;
    private SockJsClient sockJsClient;
    private StompSession stompSession;
    private CustomStompSessionHandler customStompSessionHandler;

    public void connect() throws ExecutionException, InterruptedException {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        this.sockJsClient = new SockJsClient(transports);
        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());//look how to customize

        StompSessionHandler sessionHandler = new CustomStompSessionHandler();

        stompSession = stompClient.connect("ws://localhost:8080/mywebsockets", sessionHandler).get();
    }
    public void send(String sender,String name){
        stompSession.send(sender, new Greeting(name));
    }
    public void subscribe(String toSubcribe){
        stompSession.subscribe(toSubcribe, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Greeting.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Greeting greeting = (Greeting) payload;
                System.out.println(greeting.getContent());
            }
        });
    }




    public static void main(String[] args) {
//        System.out.println("started");
        ClientApp clientApp =new ClientApp();
        ClientApp clientApp2 =new ClientApp();

        try {
            clientApp.connect();
            clientApp2.connect();
            clientApp.subscribe("/topic/greeting");
            clientApp2.subscribe("/topic/onlylisten");
//            while(true){
//                Scanner sc=new Scanner(System.in);
//                String name=sc.nextLine();
                clientApp.send("/app/greeting","name");
                clientApp2.send("/app/onlylisten","bye abhi");
                clientApp.send("/topic/greeting","hi abhi");
            Thread.sleep(2000);
//            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }

    }
}
