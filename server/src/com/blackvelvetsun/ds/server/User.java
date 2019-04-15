package com.blackvelvetsun.ds.server;

import com.blackvelvetsun.ds.network.TCPConnection;

import java.security.PublicKey;

public class User {

    private TCPConnection connection;
    private PublicKey publicKey;

    public User(TCPConnection connection, PublicKey publicKey) {
        this.connection = connection;
        this.publicKey = publicKey;
    }

    public TCPConnection getConnection() {
        return connection;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
