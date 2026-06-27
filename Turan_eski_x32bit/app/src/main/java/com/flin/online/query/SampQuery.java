package com.flin.online.query; /**
 *
 * @author Edward McKnight (EM-Creations.co.uk)
 */

/* *****************************************************************
// SampQuery.java
// Version 1.1.1
// This class connects to a specific SA-MP server via sockets.
// Copyright 2012 Edward McKnight (EM-Creations.co.uk)
// Creative Commons Attribution-NoDerivs 3.0 Unported License
* *****************************************************************/

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.StringTokenizer;

public class SampQuery {
    private DatagramSocket socket = null;
    private InetAddress server = null;
    private String serverString = "";
    private int port = 0;
    private PrintWriter out = null;
    private BufferedReader in = null;
    
    /**
    * Creates a new SampQuery object. 
    * @param server hostname of the server
    * @param port port of the server
    */
    public SampQuery(String server, int port) {
        // Constructor
        try {
            this.serverString = server;
            this.server = InetAddress.getByName(this.serverString);
        } catch (UnknownHostException e) {
            System.out.println(e);
        }
        try {
            socket = new DatagramSocket(); // DatagramSocket for UDP connections
            socket.setSoTimeout(2000); // Set timeout to 2 seconds
        } catch (SocketException e) {
            System.out.println(e);
        }
        this.port = port;
    }
    
    /**
    * Returns a String array of server information. 
    * @return String[]:<br />
    *   info[0] = password (0 or 1)<br />
    *   info[1] = players<br />
    *   info[2] = maxplayers<br />
    *   info[3] = hostname<br />
    *   info[4] = gamemode<br />
    *   info[5] = map<br />
    */
    public String[] getInfo() { // Finished
        DatagramPacket packet = this.assemblePacket("i");
        this.send(packet);
        byte[] reply = this.receiveBytes();
        ByteBuffer buff = ByteBuffer.wrap(reply);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        buff.position(11);
        String[] serverInfo = new String[6];
        
        int password = buff.get();
        short players = buff.getShort();
        short maxPlayers = buff.getShort();
        int len = buff.getInt();
        byte[] hostnameBA = new byte[len];
        
        for (int i = 0; len > i; i++) {
            hostnameBA[i] = buff.get();
        }
        
        String hostname = new String(hostnameBA);
        int lenG = buff.getInt();
        byte[] gamemodeBA = new byte[lenG];
        
        for (int i = 0; lenG > i; i++) {
            gamemodeBA[i] = buff.get();
        }
        
        String gamemode = new String(gamemodeBA);

        int lenM = buff.getInt();
        byte[] mapBA = new byte[lenM];
        
        for (int i = 0; lenM > i; i++) {
            mapBA[i] = buff.get();
        }
        
        String map = new String(mapBA);
        
        serverInfo[0] = ""+password;
        serverInfo[1] = ""+players;
        serverInfo[2] = ""+maxPlayers;
        serverInfo[3] = hostname;
        serverInfo[4] = gamemode;
        serverInfo[5] = map;
        
        return serverInfo;
    }
    
    /**
    * Returns a multidimensional String array of basic player information. 
    * @return String[][]:<br />
    *   String[][0]:<br />
    *       players[0] = playername<br />
    *       players[1] = score<br />
    * @see getDetailedPlayers
    */
    public String[][] getBasicPlayers() { // Finished
        DatagramPacket packet = this.assemblePacket("c");
        this.send(packet);
        byte[] reply = this.receiveBytes();
        ByteBuffer buff = ByteBuffer.wrap(reply);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        buff.position(11);
        
        short playerCount = buff.getShort();
        String[][] players = new String[playerCount][2];
        
        for (int i = 0; players.length > i; i++) {
            byte len = buff.get();
            byte[] nameBA = new byte[len];
        
            for (int j = 0; len > j; j++) {
                nameBA[j] = buff.get();
            }
            String name = new String(nameBA);
            int score = buff.getInt();
            
            players[i][0] = name;
            players[i][1] = ""+score;
        }
        return players;
    }
    
    /**
    * Returns a multidimensional String array of detailed player information. 
    * @return String[][]:<br />
    *   String[][0]:<br />
    *       players[0] = playerid<br />
    *       players[1] = playername<br />
    *       players[2] = score<br />
    *       players[3] = ping<br />
    * @see getBasicPlayers
    */
    public String[][] getDetailedPlayers() { // Finished
        DatagramPacket packet = this.assemblePacket("d");
        this.send(packet);
        byte[] reply = this.receiveBytes();
        ByteBuffer buff = ByteBuffer.wrap(reply);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        buff.position(11);
        
        int playerCount = buff.getShort();
        String[][] players = new String[playerCount][4];
        
        for (int i = 0; players.length > i; i++) {
            int id = buff.get();
            int len = buff.get();
            byte[] nameBA = new byte[len];
        
            for (int j = 0; len > j; j++) {
                nameBA[j] = buff.get();
            }
            String name = new String(nameBA);
            int score = buff.getInt();
            int ping = buff.getInt();
            
            players[i][0] = ""+id;
            players[i][1] = name;
            players[i][2] = ""+score;
            players[i][3] = ""+ping;
        }
        return players;
    }
    
    /**
    * Returns a multidimensional String array of server rules. 
    * @return String[][]:<br />
    *   String[][0]:<br />
    *       rules[0] = rule<br />
    *       rules[1] = value<br />
    */
    public String[][] getRules() { // Finished
        DatagramPacket packet = this.assemblePacket("r");
        this.send(packet);
        byte[] reply = this.receiveBytes();
        ByteBuffer buff = ByteBuffer.wrap(reply);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        buff.position(11);
        
        short ruleCount = buff.getShort();
        String[][] rules = new String[ruleCount][2];
       
        for (int i = 0; rules.length > i; i++) {
            int len = buff.get();
            byte[] ruleBA = new byte[len];
            
            for (int j = 0; len > j; j++) {
                ruleBA[j] = buff.get();
            }
            String rule = new String(ruleBA);
            
            int lenV = buff.get();
            byte[] valBA = new byte[lenV];
            
            for (int j = 0; lenV > j; j++) {
                valBA[j] = buff.get();
            }
            String val = new String(valBA);
            
            rules[i][0] = rule;
            rules[i][1] = val;
        }
        return rules;
    }
    
    /**
    * Returns the server's ping.
    * @return integer
    */
    public long getPing() { // Finished
        long ping = 0;
        DatagramPacket packet = this.assemblePacket("p0101");
        long beforeSend = System.currentTimeMillis();
        this.send(packet);
        this.receiveBytes();
        long afterReceive = System.currentTimeMillis();
        ping = afterReceive - beforeSend;
        
        return ping;
    }
    
    /**
    * Returns whether a successful connection was made. 
    * @return boolean
    */
    public boolean connect() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DatagramPacket packet = this.assemblePacket("p0101");
        this.send(packet);
        String reply = this.receive();
        
        try {
            // Clean up reply
            reply = reply.substring(10); 
            reply = reply.trim();

            if (reply.equals("p0101")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) { return false; }
    }
    
    /**
    * Closes the connection.
    */
    public void close() {
        socket.close();
    }
    
    private DatagramPacket assemblePacket(String type) {
        DatagramPacket sendPacket = null;
        try {
           StringTokenizer tok = new StringTokenizer(this.serverString, ".");
           
           String packetData = "SAMP";
           
           while (tok.hasMoreTokens()) {
               packetData += (char)(Integer.parseInt(tok.nextToken()));
           }

           packetData += (char)(this.port & 0xFF);
           packetData += (char)(this.port >> 8 & 0xFF);
           packetData += type;
           
           byte[] data = packetData.getBytes("US-ASCII");
           
           sendPacket = new DatagramPacket(data, data.length, this.server, this.port);

        } catch (Exception e) {
            System.out.println(e);
        }
        return sendPacket;
    }
    
    private void send(DatagramPacket packet) {
        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    private String receive() {
        String modifiedSentence = null;
        byte[] receivedData = new byte[1024];
        try {
            DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
            socket.receive(receivedPacket);
            modifiedSentence = new String(receivedPacket.getData());
        } catch (IOException e) {
            System.out.println(e);
        }
        return modifiedSentence;
    }
    
    private byte[] receiveBytes() {
        byte[] receivedData = new byte[3072];
        DatagramPacket receivedPacket = null;
        try {
            receivedPacket = new DatagramPacket(receivedData, receivedData.length);
            socket.receive(receivedPacket);
        } catch (IOException e) {
            System.out.println(e);
        } 
        return receivedPacket.getData();
    }
}