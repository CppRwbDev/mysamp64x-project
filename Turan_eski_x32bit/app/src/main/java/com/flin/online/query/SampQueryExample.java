package com.flin.online.query;

/**
 *
 * @author Edward McKnight (EM-Creations.co.uk)
 */

public class SampQueryExample {
    public static void main(String args[]) {
        System.out.println("Starting program..");
        SampQuery query = new SampQuery("127.0.0.1", 7777);

        if (query.connect()) { // If a successful connection has been made

            String[] serverInfo = query.getInfo(); // Get server info
            System.out.println(serverInfo[0]+" - "+serverInfo[1]+"/"+serverInfo[2]+" - "+serverInfo[3]+" | "+serverInfo[4]+" | "+serverInfo[5]);

            String[][] basicPlayers = query.getBasicPlayers(); // Get basic players, connection will time out if the player counter is above 100 and will return an empty array if no players are online
            System.out.println("Basic players:");
            for (int i = 0; basicPlayers.length > i; i++) {
                System.out.println((i + 1)+") "+basicPlayers[i][0]+" - "+basicPlayers[i][1]);
            }

            String[][] detailedPlayers = query.getDetailedPlayers(); // Get detailed players, connection will time out if the player counter is above 100 and will return an empty array if no players are online
            System.out.println("Detailed players:");
            for (int i = 0; detailedPlayers.length > i; i++) {
                System.out.println("("+detailedPlayers[i][0]+") "+detailedPlayers[i][1]+" | "+detailedPlayers[i][2]+" | "+detailedPlayers[i][3]);
            }

            System.out.println("Rules:");
            String[][] rules = query.getRules(); // Get server rules
            for (int i = 0; rules.length > i; i++) {
                System.out.println(rules[i][0]+" : "+rules[i][1]);
            }
            query.close(); // Close the connection
        } else {
            System.out.println("Server did not respond!");
        }
    }
}