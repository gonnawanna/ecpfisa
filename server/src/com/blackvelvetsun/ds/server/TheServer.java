package com.blackvelvetsun.ds.server;

import com.blackvelvetsun.ds.network.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TheServer implements TCPConnectionListener {

    private static int id = 0;
    private final Map<Integer, TCPConnection> temp = new HashMap<>();
    private final Map<String, TCPConnection> connections = new HashMap<>();

    public static void main(String[] args) {
        new TheServer();
    }

    private TheServer() {
        System.out.println("Сервер запущен...");
        try(ServerSocket serverSocket = new ServerSocket(8181)){
            System.out.println(InetAddress.getLocalHost());
            while(true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e){
                    System.out.println("TCPConnection exception:" + e);
                }
            }
        } catch(IOException e){
                throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnect(TCPConnection tcpConnection) {
        temp.put(id, tcpConnection);
        LoginPack loginPack = new LoginPack(id);
        tcpConnection.send(loginPack);
        id++;

    }

    @Override
    public synchronized void onReceiving(TCPConnection tcpConnection, Object pack) {
        DeliveryPack newPack = (DeliveryPack) pack;
        newPack.accept(this);
    }

    public void visitLoginPack(LoginPack loginPack){
        TCPConnection connection = temp.remove(loginPack.getIdConnection());
        connections.put(loginPack.getSenderLogin(), connection);
        System.out.println("+" + loginPack.getSenderLogin());

    }

    public void visitMessage(Message message){
        TCPConnection receiver = connections.get(message.getReceiverLogin());
        receiver.send(message);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
    }

}
