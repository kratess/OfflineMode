package me.kratess.OfflineMode.Utils;

import java.util.ArrayList;

public class BotAttack implements Runnable {

    private static int remove = 2;
    private static int remove_out = 20;

    private static int remove_o = 0;

    @Override
    public void run() {
        remove_o++;
        if (remove_o == remove_out) {
            intensity--;
            remove_o = 0;
        }

        if (UNDER_ATTACK) {
            intensity-=remove;
            if (intensity <= 0) {
                UNDER_ATTACK = false;
                intensity = 0;
                //IPs = new ArrayList<>();
            }
        }
    }

    public static boolean UNDER_ATTACK = false;

    // MIN 0
    // MAX 128
    public static int intensity = 0;

    // POSSIBLE IPs
    //private static ArrayList<String> IPs = new ArrayList<>();

    private static long last_bot = 0L;

    public static void addBotIP(String ip, boolean proxy, String type, int risk, int port) {
        //IPs.add(ip);
        if (proxy) intensity++;
        if (type != null) intensity++;
        if (risk >= 5 && risk < 20) {
            intensity++;
            last_bot = System.currentTimeMillis();
        } else if (risk >= 20 && risk < 40) {
            intensity=+2;
            last_bot = System.currentTimeMillis();
        } else if (risk >= 40 && risk < 60) {
            intensity=+3;
            last_bot = System.currentTimeMillis();
        } else if (risk >= 60) {
            intensity=+5;
            last_bot = System.currentTimeMillis();
        }
        if (port >= 0) intensity++;

        checkForAntiBot();
    }

    public static void checkForAntiBot() {
        if (intensity >= 128) {
            UNDER_ATTACK = true;
        }
    }

}
