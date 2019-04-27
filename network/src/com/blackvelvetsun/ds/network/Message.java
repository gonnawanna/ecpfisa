package com.blackvelvetsun.ds.network;

import java.security.PublicKey;

public class Message extends DeliveryPack  {

    private String receiverLogin;
    private String message;
    private byte[] ecp;
    private PublicKey publicKey;

    public Message(String senderLogin, String receiverLogin, String message, byte[] ecp, PublicKey publicKey) {
        super.setSenderLogin(senderLogin);
        this.receiverLogin = receiverLogin;
        this.message = message;
        this.ecp = ecp;
        this.publicKey = publicKey;
    }

    public String getReceiverLogin() {
        return receiverLogin;
    }

    public String getMessage() {
        return message;
    }

    public void accept(TCPConnectionListener listener)  {
        listener.visitMessage(this);
    }

    public byte[] getEcp() {
        return ecp;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}