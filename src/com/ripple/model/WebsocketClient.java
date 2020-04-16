package com.ripple.model;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.google.gson.Gson;
import com.ripple.beans.ServerInfo;

public class WebsocketClient extends Endpoint {
	
	private Session userSession = null;
	private BlockingQueue<String> queue;
	
    public WebsocketClient(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
	@Override
	public void onOpen(Session userSession, EndpointConfig arg1) {
		
		//As web socket messages are asynchronous, this is used to pass message to the main thread
		//Doing this with another object would not guarantee this has finished running
		queue = new SynchronousQueue<>(true);
		   userSession.addMessageHandler (new MessageHandler.Whole<String>() {
		      public void onMessage(String receivedMessage) {
              		try {
              			//We add the message to the blocking queue
						queue.put(receivedMessage);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	             }
		   });
		   this.userSession = userSession;
	}
	
		//Polling method
	  public List <ServerInfo> pollWS ( String message, Integer frequency, Integer numOccurrences) throws ParseException, IOException {
		  
		  List <ServerInfo> siList = new ArrayList<ServerInfo>();
		  Gson json = new Gson();
		  String messageSent = message;
	
		  try {
	            //Poll until max number of occurrence selected int the ComboBox is reached
	            for (int i=0; i<numOccurrences; i++) {
		            this.sendMessage(messageSent);
		            
		            //Wait for the frequency selected in the ComboBox
		            Thread.sleep(frequency);
		            
		            String messageReceived = this.getQueue().poll();
					//Serialise the JSON response into the ServerInfo objects using GSon library
					ServerInfo si = json.fromJson(messageReceived.toString(), ServerInfo.class);
					siList.add(si);
	            }
	            
	            //Very important to close the socket once polling is completed
	            this.getSession().close();
	
	        } catch (InterruptedException e) {
	
			}
	
			return siList;
	    }
	
	public BlockingQueue<String> getQueue() {
		return this.queue;
	}
	
	public Session getSession( ) {
		return this.userSession;
	}
	
	public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }
 
}
