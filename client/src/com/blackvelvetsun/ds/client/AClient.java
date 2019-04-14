package com.blackvelvetsun.ds.client;

import com.blackvelvetsun.ds.network.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AClient extends JFrame implements ActionListener, TCPConnectionListener {

    private String IP;
    private static final int PORT = 8181;
    private TCPConnection connection;
    private String login;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private final JTextArea chat = new JTextArea();
    private final JTextArea name = new JTextArea();
    JPanel panel = new JPanel();
    JLabel label1 = new JLabel("Enter Text:");
    private final TextField fieldMsg = new TextField(20);
    JLabel label2 = new JLabel("Кому:");
    JButton sendButton = new JButton("Отправить");
    private final TextField fieldReceiver = new TextField(10);
    private final TextField fieldLogin = new TextField(10);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartWindow();
            }
        });

    }

    public AClient(String IP, String login) throws IOException {
        this.login = login;

        drawFace();

        connection = new TCPConnection(this, IP, PORT);

    }

    private void drawFace(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        fieldLogin.setText(login);
        fieldLogin.setEditable(false);

        chat.setLineWrap(true);

        panel.add(label1); // Components Added using Flow Layout
        panel.add(fieldMsg);
        panel.add(label2);
        panel.add(fieldReceiver);
        panel.add(sendButton);

        //Adding Components to the frame.
        add(fieldLogin, BorderLayout.NORTH);
        add(panel, BorderLayout.SOUTH);
        add(chat, BorderLayout.CENTER);
        sendButton.addActionListener(this);
        setVisible(true);
    }


    public void actionPerformed(ActionEvent e){
        String receiver = fieldReceiver.getText();
        String msg = fieldMsg.getText();
        if(msg.equals("") || receiver.equals("")) return;
        Message pack = new Message(login,fieldReceiver.getText(), msg);
        connection.send(pack);
        fieldMsg.setText(null);
        fieldReceiver.setText(null);
    }


    @Override
    public void onConnect(TCPConnection tcpConnection) {
        printMsg("Соединение с сервером установлено.\n");
    }

    @Override
    public void onReceiving(TCPConnection tcpConnection, Object message) {
        DeliveryPack msg = (DeliveryPack) message; //как это работает
        msg.accept(this);

    }

    public void visitLoginPack(LoginPack loginPack){
        loginPack.setSenderLogin(login);
        connection.send(loginPack);
    }

    public void visitMessage(Message message){
        printMsg(message.getSenderLogin() + ": " + message.getMessage());
    }


    @Override
    public void onDisconnect(TCPConnection tcpConnection) {

    }

    private synchronized void printMsg(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chat.append(msg + "\n");
            }
        });
    }

}
