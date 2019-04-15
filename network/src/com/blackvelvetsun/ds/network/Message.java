package com.blackvelvetsun.ds.network;

import com.blackvelvetsun.ds.server.TheServer;

import java.io.Serializable;

public class Message extends DeliveryPack  {

    private String receiverLogin;
    private String message;
    private byte[] ecp;

    public Message(String senderLogin, String receiverLogin, String message, byte[] ecp) {
        super.setSenderLogin(senderLogin);
        this.receiverLogin = receiverLogin;
        this.message = message;
        this.ecp = ecp;
    }

    public String getReceiverLogin() {
        return receiverLogin;
    }

    public String getMessage() {
        return message;
    }

    public void accept(TCPConnectionListener listener){
        listener.visitMessage(this);
    }

    public byte[] getEcp() {
        return ecp;
    }
}