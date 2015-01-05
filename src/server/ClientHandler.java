package server;

import common.Command;
import model.Model;
import model.MyModel;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable{
    private Socket socket;
    private static Model model = new MyModel();
    Map<String, Command> stringCommandMap = new HashMap<String, Command>();

    public ClientHandler(Socket socket) {
        this.socket = socket;
        mapCommands();
    }

    private void mapCommands() {
        stringCommandMap.put("algorithm", new Command() {
            @Override
            public void doCommand(String param, ObjectOutputStream out) {
                model.selectAlgorithm(param);
            }
        });
        stringCommandMap.put("domain", new Command() {
            @Override
            public void doCommand(String param, ObjectOutputStream out) {
                try {
                    model.selectDomain(param);
                } catch (IllegalArgumentException e) {
                    try {
                        out.writeBytes(e.getMessage());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        stringCommandMap.put("start", new Command() {
            @Override
            public void doCommand(String param, ObjectOutputStream out) {
                model.solveDomain();
                try {
                    out.writeObject(5/*just to write ANY object to let know we finished*/);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stringCommandMap.put("displayState", new Command() {
            @Override
            public void doCommand(String param, ObjectOutputStream out) {
                try {
                    out.writeObject(model.getDomain());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        stringCommandMap.put("getSolution", new Command() {
            @Override
            public void doCommand(String param, ObjectOutputStream out) {
                try {
                    out.writeObject(model.getSolution());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void run() {
        try {
            ObjectInputStream in =
                            new ObjectInputStream(socket.getInputStream());

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            String line = null;
            while (true) {
                line = in.readUTF();
                break;
            }

            runCommand(line, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runCommand(String userAction, ObjectOutputStream out) {
        String commandType = userAction.substring(0, userAction.indexOf("=") > 0 ? userAction.indexOf("=") : userAction.length());
        String commandParam = "";
        if (userAction.indexOf("=") >= 0) {
            commandParam = userAction.substring(userAction.indexOf("=") + 1);
        }

        Command command = stringCommandMap.get(commandType);
        if (command != null) {
            command.doCommand(commandParam, out);
        } else {
            try {
                out.writeBytes("No such command");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
