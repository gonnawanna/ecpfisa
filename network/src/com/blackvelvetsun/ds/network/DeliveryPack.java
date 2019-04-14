package com.blackvelvetsun.ds.network;

import java.io.Serializable;

public abstract class DeliveryPack implements Serializable {

    private String senderLogin;

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public abstract void accept(TCPConnectionListener listener);
}
