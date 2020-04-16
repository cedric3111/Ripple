package com.ripple.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import com.ripple.beans.ServerInfo;
import com.ripple.model.GNUPlotBuilder;
import com.ripple.model.HTTPClient;
import com.ripple.model.WebsocketClient;

public class Controller {
	
	private GNUPlotBuilder plotBuilder;
	
	public Controller (GNUPlotBuilder plotBuilder) {
		this.plotBuilder = plotBuilder;
	}
		
	public void startPolling (Integer selectedFrequency, Integer numOccurences, String protocol, String httpnetwork, String wsnetwork) throws IOException {
		
	  if (protocol == "JSON-RPC") {
	    //Configure the polling request
	    String HTTPmessage = "{\"method\": \"server_info\", \"params\": [ {} ] }";
	    
	    //Polling attempt
		try {
				//Initialise the HTTP client
				HTTPClient httpclient = new HTTPClient(httpnetwork);
				
				//Start polling and retrieve list of ServerInfo
				List <ServerInfo> siList = httpclient.pollHTTP(HTTPmessage, selectedFrequency, numOccurences);
				
				//Build file with object list retrieved from polling
				this.plotBuilder.createFile(siList);
				this.plotBuilder.setMinTime(siList);
				this.plotBuilder.setAverageTime(siList,selectedFrequency);
				this.plotBuilder.setMaxTime(siList);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
	    
	} else if (protocol == "WebSocket") {
		
	    //Configure the polling request
	    String WSmessage = "{ \"id\": 1, \"command\": \"server_info\" }";
	    
	    //Polling attempt
		try {
				//Create instance of the WebSocketClient to open the Web Socket
				WebsocketClient wsclient = new WebsocketClient(new URI(wsnetwork));
				
				//Start polling and retrieve list of ServerInfo
				List <ServerInfo> siList = wsclient.pollWS( WSmessage, selectedFrequency, numOccurences);
				
				//Build file with object list retrieved from polling
				this.plotBuilder.createFile(siList);
				this.plotBuilder.setMinTime(siList);
				this.plotBuilder.setAverageTime(siList,selectedFrequency);
				this.plotBuilder.setMaxTime(siList);
					} catch (ParseException | IOException | URISyntaxException e) {
						e.printStackTrace();
					}
			  	}
	
		    }
	
	public void createPlot (String gnuplotexepath) {
		this.plotBuilder.createPlot(gnuplotexepath);
	}
}
