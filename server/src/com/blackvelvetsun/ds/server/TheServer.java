package com.blackvelvetsun.ds.server;

import com.blackvelvetsun.ds.network.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class TheServer implements TCPConnectionListener {

    private static int id = 0;
    private final Map<Integer, TCPConnection> temp = new HashMap<>();
    private final Map<String, TCPConnection> users = new HashMap<>();

    public static void main(String[] args) {
        new TheServer();
    }

    private TheServer() {
        try(ServerSocket serverSocket = new ServerSocket(8180)){
            System.out.println("Сервер запущен...");
            System.out.println(InetAddress.getLocalHost());
            drawFace();
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

    private void drawFace() throws UnknownHostException {
        JFrame fr = new JFrame();
        TextField ip = new TextField();

        fr.setTitle("Сервер");
        fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fr.setSize(200,100);
        fr.setLocationRelativeTo(null);
        fr.setAlwaysOnTop(true);

        ip.setText(InetAddress.getLocalHost().toString());
        ip.setEditable(false);
        fr.add(ip);
        fr.setVisible(true);
    }

    @Override
    public synchronized void onConnect(TCPConnection tcpConnection) {
        temp.put(id, tcpConnection);
        LoginPack loginPack = new LoginPack(id);
        tcpConnection.send(loginPack);
        id++;

    }

    @Override
    public synchronized void onReceiving(TCPConnection tcpConnection, Object pack){
        DeliveryPack newPack = (DeliveryPack) pack;
        newPack.accept(this);
    }

    public void visitLoginPack(LoginPack loginPack){
        TCPConnection connection = temp.remove(loginPack.getIdConnection());
        users.put(loginPack.getSenderLogin(), connection);
    }

    public void visitMessage(Message message){
        send(message, message.getReceiverLogin());
    }

    public void send(DeliveryPack pack, String receiverLogin){
        TCPConnection receiverConnection = users.get(receiverLogin);;
        receiverConnection.send(pack);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        //users.remove(tcpConnection);
    }

}
