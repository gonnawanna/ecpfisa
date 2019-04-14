package com.blackvelvetsun.ds.server;

import com.blackvelvetsun.ds.network.TCPConnection;

public class User {

    private TCPConnection connection;
    private String publicKey;

    public User(TCPConnection connection, String publicKey) {
        this.connection = connection;
        this.publicKey = publicKey;
    }

    public TCPConnection getConnection() {
        return connection;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
