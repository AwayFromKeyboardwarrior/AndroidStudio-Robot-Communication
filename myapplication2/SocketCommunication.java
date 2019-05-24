package com.example.krjikim.myapplication2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketCommunication {

    static Socket socket;
    static String socketRead = "";
    static String socketSend = "";
    static boolean socket_repeat;

    static String ipaddress;
    static int portnumber;
    static int socketsending_repeat_interval = 1000;

    public void SocketConnection(String ip, int port) throws Exception {
        ipaddress = ip;
        portnumber = port;
        new Thread(new SocketCreation()).start();
        //c.execute();
    }


    static class SocketCreation implements Runnable {
        @Override
        public void run() {
            try {
                socket = new Socket(ipaddress, portnumber);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    static public boolean SocketStatusChk() {
        try {
            return socket.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    Thread socketsendingThread;

    static class SocketSending_Thread implements Runnable {
        public SocketSending_Thread(String str) {
            socketSend = str;
        }
        @Override
        public void run() {
                try {
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.write(socketSend);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

    Thread socketreading;

    public void SocketReading() {
        try {
            socketreading = new Thread(new SocketReading());
            socketreading.start();
        } catch (Exception e) {
        }
    }

    class SocketReading implements Runnable {
        @Override
        public void run() {
            while (socket_repeat) {
                try {
                    InputStream input = socket.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(input));
                    socketRead = in.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
