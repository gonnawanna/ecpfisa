package com.blackvelvetsun.ds.client;

import com.blackvelvetsun.ds.network.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;

public class AClient extends JFrame implements ActionListener, TCPConnectionListener {

    private static final int PORT = 8180;
    private TCPConnection connection;
    private String login;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private final JTextArea chat = new JTextArea();
    private final TextField fieldMsg = new TextField(20);
    private final TextField fieldReceiver = new TextField(10);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartWindow::new);
    }

    public AClient(String IP, String login) throws IOException, NoSuchAlgorithmException {
        this.login = login;
        generateKeys();
        drawFace();
        connection = new TCPConnection(this, IP, PORT);
    }

    private void generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        KeyPair pair = keyGen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
    }

    private void drawFace(){
        JLabel labelLogin = new JLabel();
        JPanel panel = new JPanel();
        JLabel label1 = new JLabel("Enter Text:");
        JLabel label2 = new JLabel("Кому:");
        JButton sendButton = new JButton("Отправить");

        setTitle("Клиент");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600,400);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        labelLogin.setText(login);
        chat.setLineWrap(true);
        panel.add(label1); // Components Added using Flow Layout
        panel.add(fieldMsg);
        panel.add(label2);
        panel.add(fieldReceiver);
        panel.add(sendButton);

        //Adding Components to the frame.
        add(labelLogin, BorderLayout.NORTH);
        add(panel, BorderLayout.SOUTH);
        add(chat, BorderLayout.CENTER);
        sendButton.addActionListener(this);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        String receiver = fieldReceiver.getText();
        String msg = fieldMsg.getText();
        if(msg.equals("") || receiver.equals("")) return;
        try {
            byte[] ecp = createEcp(msg);
            Message pack = new Message(login, receiver, msg, ecp, publicKey);
            connection.send(pack);
        } catch (NoSuchAlgorithmException | InvalidKeyException |
                SignatureException e1) {
            e1.printStackTrace();
        }
        fieldMsg.setText(null);
        fieldReceiver.setText(null);
    }

    private byte[] createEcp(String text) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException{
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateKey);
        signature.update(text.getBytes());
        return signature.sign();
    }

    @Override
    public void onConnect(TCPConnection tcpConnection) {
        printMsg("Соединение с сервером установлено.\n");
    }

    @Override
    public void onReceiving(TCPConnection tcpConnection, Object message) {
        DeliveryPack msg = (DeliveryPack) message;
        msg.accept(this);
    }

    public void visitLoginPack(LoginPack loginPack){
        loginPack.setSenderLogin(login);
        connection.send(loginPack);
    }

    public void visitMessage(Message message) {
        try {
            printMsg(message.getSenderLogin() + ":\n"
                    + "сообщение: " + message.getMessage() + "\n"
                    + "ЭЦП: " + new String(message.getEcp(), "Cp280") + "\n");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(verifyEcp(message)){
            printMsg("Подпись проверена. Сообщение отправил пользователь: " + message.getSenderLogin());
        } else {
            printMsg("Подпись не верна");
        }
    }

    public boolean verifyEcp(Message message){
        Signature signature;
        boolean verified = false;
        try {
            signature = Signature.getInstance("SHA1withRSA");
            signature.initVerify(message.getPublicKey());
            signature.update(message.getMessage().getBytes());
            verified = signature.verify(message.getEcp());
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return verified;
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Соединение прервано.\n");
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
