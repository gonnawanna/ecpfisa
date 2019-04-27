package com.blackvelvetsun.ds.network;

public class LoginPack extends DeliveryPack {

    private final int idConnection;

    public LoginPack(int id) {
        this.idConnection = id;
    }

    public int getIdConnection() {
        return idConnection;
    }

    public void accept(TCPConnectionListener listener){
        listener.visitLoginPack(this);
    }
}
