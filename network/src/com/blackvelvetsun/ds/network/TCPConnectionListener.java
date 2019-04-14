package com.blackvelvetsun.ds.network;

import java.util.ArrayList;

public interface TCPConnectionListener {

    void onConnect(TCPConnection tcpConnection);
    void onReceiving(TCPConnection tcpConnection, Object deliveryPack);
    void onDisconnect(TCPConnection tcpConnection);
    void visitMessage(Message message);
    void visitLoginPack(LoginPack loginPack);
    //void onException();
}
