package common;

import java.io.Serializable;

public class MyProperties implements Serializable{
    private int port;
    private int numOfClients;

    public MyProperties() {
    }

    public MyProperties(int port, int numOfClients) {
        this.port = port;
        this.numOfClients = numOfClients;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getNumOfClients() {
        return numOfClients;
    }

    public void setNumOfClients(int numOfClients) {
        this.numOfClients = numOfClients;
    }
}
