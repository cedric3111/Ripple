package com.ripple.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;

import com.ripple.controller.Controller;
import com.ripple.model.ConfigFactory;
import com.ripple.model.GNUPlotBuilder;

@SuppressWarnings("serial")
public class WindowMain extends JFrame{
	
	private PanelMain pm = new PanelMain();
	private PanelTop pt = new PanelTop();
	private PanelBottom pb = new PanelBottom();
	private PanelCenter pc = new PanelCenter();
	
	private JButton startbtn = new JButton("Start new polling");
	private JButton viewbtn = new JButton("View latest plot");
	
	private JComboBox <String> netCombo;
	private JComboBox <String> protocolsCombo;
	private JComboBox <String> freqCombo;
	private JComboBox <String> occCombo;
	
	private JLabel netLabel = new JLabel("Select the network: ");
	private JLabel protocolLabel = new JLabel("Select the protocol: ");
	private JLabel freqLabel = new JLabel("Select polling frequency: ");
	private JLabel occLabel = new JLabel("Select polling occurences: ");
	
	private JPanel netpanel = new JPanel();
	private JPanel protocolpanel = new JPanel();
	private JPanel freqpanel = new JPanel();
	private JPanel occpanel = new JPanel();
	
	private Map<String, String> httpnetwork = new HashMap<String, String> ();
	private Map<String, String> wsnetwork = new HashMap<String, String> ();
	private Map<String, Integer> frequency = new HashMap<String, Integer> ();
	
	//Initialise all variables loading them from config file
	private String gnuplotfilepath = ConfigFactory.getInstance().getGnuplotfilepath();
	private String gnuplotexepath = ConfigFactory.getInstance().getGnuplotexepath();
	private String httpmainnet = ConfigFactory.getInstance().getHttpmainnet();
	private String httptestnet = ConfigFactory.getInstance().getHttptestnet();
	private String wsmainnet = ConfigFactory.getInstance().getWsmainnet();
	private String wstestnet = ConfigFactory.getInstance().getWstestnet();
	
	private GNUPlotBuilder plotbuilder = new GNUPlotBuilder(gnuplotfilepath);
	
    private Controller controller = new Controller(this.plotbuilder);
	
	  public WindowMain () {
		   
		//Purge any potential existing file on load
		plotbuilder.deleteFile();
		
		//Then we set up the main window decoration
		this.setTitle("Rippled Server Info");
	    this.setSize(400, 500);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setBackground(Color.darkGray);
	    this.setResizable(false);
	    
	    httpnetwork.put("mainnet", httpmainnet);
	    httpnetwork.put("testnet", httptestnet);
	    
	    wsnetwork.put("mainnet", wsmainnet);
	    wsnetwork.put("testnet", wstestnet);
	    
	    frequency.put("1s", 1000);
	    frequency.put("3s", 3000);
	    frequency.put("6s", 6000);
	    
	    //Enforce the size for the header
	    pt.setPreferredSize(new Dimension(200, 130));
	    pc.setBorder(new CompoundBorder(
	    		BorderFactory.createMatteBorder(0, 20, 0, 20, Color.DARK_GRAY), 
	    	    BorderFactory.createMatteBorder(1, 0, 1, 0, Color.GRAY)));
	    
	    //Use border layout for Main panel
	    pm.setLayout(new BorderLayout());
	    pm.add(pt,BorderLayout.NORTH);
	    pm.add(pc,BorderLayout.CENTER);
	    pm.add(pb,BorderLayout.SOUTH);
	    
		//Add form inputs to their respective sub panel
	    String [] netList = {"mainnet" , "testnet"};	    
	    netCombo = new JComboBox<String> (netList);
	  
	    String [] protocolsList = {"JSON-RPC" , "WebSocket"};	    
	    protocolsCombo = new JComboBox <String> (protocolsList);
	    
	    String [] freqList = {"1s", "3s" , "6s"};	    
	    freqCombo = new JComboBox<String> (freqList);
	  
	    String [] occList = {"20", "30", "40", "50", "60"};	    
	    occCombo = new JComboBox <String> (occList);
	    
	    startbtn.setPreferredSize(new Dimension(this.getWidth()/2-startbtn.getWidth()/2-26,30));
	    startbtn.setBackground(Color.decode("#0F72E5"));
	    startbtn.setForeground(Color.WHITE);
	    
	    viewbtn.setPreferredSize(new Dimension(this.getWidth()/2-startbtn.getWidth()/2-26,30));
	    viewbtn.setBackground(Color.decode("#0F72E5"));
	    viewbtn.setForeground(Color.WHITE);
	    
	    startbtn.addActionListener(new StartButtonListener());
	    viewbtn.addActionListener(new ViewButtonListener());
	    
	    pb.add(startbtn);
	    pb.add(viewbtn);
	    
	    pc.setLayout(new GridLayout(4,1));

	    netLabel.setForeground(Color.WHITE);
	    netLabel.setVerticalAlignment(JLabel.CENTER);
	    netLabel.setPreferredSize(new Dimension(180,30));
	    netCombo.setPreferredSize(new Dimension(150,30));

	    protocolLabel.setForeground(Color.WHITE);
	    protocolLabel.setPreferredSize(new Dimension(180,30));
	    protocolsCombo.setPreferredSize(new Dimension(150,30));

	    freqLabel.setForeground(Color.WHITE);
	    freqLabel.setPreferredSize(new Dimension(180,30));
	    freqCombo.setPreferredSize(new Dimension(150,30));

	    occLabel.setForeground(Color.WHITE);
	    occLabel.setPreferredSize(new Dimension(180,30));
	    occCombo.setPreferredSize(new Dimension(150,30));

	    //Used to centre elements for each central pane
	    netpanel.setLayout(new GridBagLayout());
	    protocolpanel.setLayout(new GridBagLayout());
	    freqpanel.setLayout(new GridBagLayout());
	    occpanel.setLayout(new GridBagLayout());
	    
	    netpanel.setBackground(Color.DARK_GRAY);
	    netpanel.add(netLabel);
	    netpanel.add(netCombo);
	    
	    protocolpanel.setBackground(Color.DARK_GRAY);
	    protocolpanel.add(protocolLabel);
	    protocolpanel.add(protocolsCombo);
	    
	    freqpanel.setBackground(Color.DARK_GRAY);
	    freqpanel.add(freqLabel);
	    freqpanel.add(freqCombo);
	    
	    occpanel.setBackground(Color.DARK_GRAY);
	    occpanel.add(occLabel);
	    occpanel.add(occCombo);
	    
	    pc.add(netpanel);
	    pc.add(protocolpanel);
	    pc.add(freqpanel);
	    pc.add(occpanel);

	    setContentPane(pm);
	    this.setVisible(true); 
	  }
	  
	  //Inner class listening to the start polling button events
	  private class StartButtonListener implements ActionListener {
	    //actionsPerformed override
	    public void actionPerformed(ActionEvent arg0) {
	    	try {
	  		  //Capture current state of the UI
	            Integer selectedFrequency = frequency.get(freqCombo.getSelectedItem());
	            Integer selectedNumOccurences = Integer.parseInt(occCombo.getSelectedItem().toString());
	            String selectedNetwork = netCombo.getSelectedItem().toString();
	            String selectedProtocol = protocolsCombo.getSelectedItem().toString();
	            String wsselectednetwork = wsnetwork.get(selectedNetwork);
	            String httpselectednetwork = httpnetwork.get(selectedNetwork);
	  	      	
	            //Pass the captured UI selected data to the controller
	  	      	controller.startPolling(selectedFrequency, selectedNumOccurences, selectedProtocol, httpselectednetwork, wsselectednetwork);

			} catch (IOException e) {
				e.printStackTrace();
			}      
	    }
	  }
	      
	  //Inner class listening to the view plot buttons events
	  private class ViewButtonListener implements ActionListener {
		//actionsPerformed override
	    public void actionPerformed(ActionEvent arg0) {     
	    	controller.createPlot(gnuplotexepath);
	    }
	 }  
}
