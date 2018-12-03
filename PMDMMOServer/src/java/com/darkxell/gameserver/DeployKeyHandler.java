/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.darkxell.gameserver;

import com.darkxell.common.dbobject.DBDeployKey;
import com.darkxell.utility.RandomString;

/**
 *
 * @author Darkxell
 */
public class DeployKeyHandler {

    /**
     * Generates a deployment key and returns it.
     */
    public static String generateKey(GameServer server) {
        DBDeployKey key = new DBDeployKey();
        key.keyvalue = getNewKey();
        if (server.getDeployKeyDAO().create(key)) {
            return key.keyvalue;
        } else {
            return "ERROR";
        }
    }

    /**
     * Generates a new random 20 chars string with a simple checksum suffix.
     */
    private static String getNewKey() {
        String k = new RandomString(20).nextString();
        int hash = 7;
        for (int i = 0; i < k.length(); i++) {
            hash = hash * 31 + k.charAt(i);
        }
        k += hash;
        return k;
    }

    public static boolean keyExists(String key, GameServer server) {
        return server.getDeployKeyDAO().find(key) != null;
    }

    public static boolean useKey(String key, int playerid, GameServer server) {
        DBDeployKey ke = server.getDeployKeyDAO().find(key);
        if (ke == null) {
            return false;
        }
        ke.timestamp = System.currentTimeMillis();
        ke.playerid = playerid;
        ke.isused = true;
        server.getDeployKeyDAO().update(ke);
        return true;
    }

}
