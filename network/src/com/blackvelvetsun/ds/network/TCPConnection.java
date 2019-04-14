package com.blackvelvetsun.ds.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class TCPConnection {


    private final Socket socket;
    private final Thread mainThread;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private TCPConnectionListener eventListener;
    private String login;

    public TCPConnection(TCPConnectionListener tcpConnectionListener, String ip, int port) throws IOException {
        this(tcpConnectionListener, new Socket(ip, port));
    }

    public TCPConnection(TCPConnectionListener tcpConnectionListener, Socket socket) throws IOException {
        this.eventListener = tcpConnectionListener;
        this.socket = socket;
        mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnect(TCPConnection.this);
                    while(!mainThread.isInterrupted()){
                        in = new ObjectInputStream(socket.getInputStream());
                        eventListener.onReceiving(TCPConnection.this, in.readObject());
                    }
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
                finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        mainThread.start();

    }

    public synchronized void send(DeliveryPack pack){
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(pack);
            out.flush();
        } catch (IOException e){
            e.printStackTrace();
            disconnect();
        }
    }

    public synchronized void disconnect(){
        mainThread.interrupt();
        try{
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "TCPConnection:" + socket.getInetAddress() + socket.getPort();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
