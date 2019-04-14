package com.blackvelvetsun.ds.network;

import com.blackvelvetsun.ds.server.TheServer;

import java.io.Serializable;
import java.security.PublicKey;

public class LoginPack extends DeliveryPack {

    private final int idConnection;
    private TCPConnection tcpConnection;
    private String publicKey;

    public LoginPack(int id) {
        this.idConnection = id;
    }

    public void accept(TCPConnectionListener listener){
        listener.visitLoginPack(this);
    }

    public int getIdConnection() {
        return idConnection;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public TCPConnection getTcpConnection() {
        return tcpConnection;
    }
}
