package com.blackvelvetsun.ds.network;

import com.blackvelvetsun.ds.server.TheServer;

public class LoginPack extends DeliveryPack {

    private final int idConnection;

    public LoginPack(int id) {
        this.idConnection = id;
    }

    public void accept(TCPConnectionListener listener){
        listener.visitLoginPack(this);
    }

    public int getIdConnection() {
        return idConnection;
    }
}
