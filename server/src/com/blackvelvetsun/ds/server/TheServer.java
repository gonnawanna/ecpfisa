package com.blackvelvetsun.ds.server;

import com.blackvelvetsun.ds.network.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheServer implements TCPConnectionListener {

    //private final List<User> users = new ArrayList<>();
    private static int id = 0;
    private final Map<Integer, TCPConnection> temp = new HashMap<>();
    private final Map<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        new TheServer();
    }

    private TheServer() {
        try(ServerSocket serverSocket = new ServerSocket(8180)){
            System.out.println("Сервер запущен...");
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
        User user = new User(connection, loginPack.getPublicKey());
        users.put(loginPack.getSenderLogin(), user);
        //мб трай кэч
    }

    public String selectPublicKey(String login){
        return users.get(login).getPublicKey(); //!!!to be continued
    }

    public void visitMessage(Message message){
        User receiver = users.get(message.getReceiverLogin());
        TCPConnection receiverConnection = receiver.getConnection();
        receiverConnection.send(message);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        //users.remove(tcpConnection);
    }

}
