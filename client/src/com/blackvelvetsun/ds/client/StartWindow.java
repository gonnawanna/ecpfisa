package com.blackvelvetsun.ds.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class StartWindow extends JFrame implements ActionListener {

    private JPanel panel = new JPanel();
    private JLabel labelIP = new JLabel("Введите адрес сервера:");
    private TextField fieldIP = new TextField(15);
    private JLabel labelLogin = new JLabel("Введите имя пользователя:");
    private TextField fieldLogin = new TextField(15);
    private JButton buttonConnect = new JButton("Подключиться");

    public StartWindow(){
        setTitle("Подключение");
        setSize(200,200);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        panel.setLayout(new GridLayout(5,1,0,10));

        buttonConnect.addActionListener(this);

        panel.add(labelIP);
        panel.add(fieldIP);
        panel.add(labelLogin);
        panel.add(fieldLogin);
        panel.add(buttonConnect);

        setContentPane(panel);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String serverIP = fieldIP.getText();
        String login = fieldLogin.getText();
        if(serverIP.equals("") || login.equals("")) return;
        try {
            new AClient(serverIP, login);
            setVisible(false);
        } catch (IOException | NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }
}
