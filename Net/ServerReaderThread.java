package Net;

import java.io.*;
import java.net.Socket;

public class ServerReaderThread extends Thread{
    Socket socket;
    public  ServerReaderThread(Socket socket){
        this.socket=socket;
    }
    public void run(){
        try {
            InputStream inputStream=socket.getInputStream();
            DataInputStream dataInputStream=new DataInputStream(inputStream);
            while (true) {
                try {
                    String message=dataInputStream.readUTF();
                    System.out.println(message);
                    sendMessageToAll(message);
                } catch (Exception e) {
                    System.out.println("下线："+socket.getRemoteSocketAddress());
                    Server.onlineSockets.remove(socket);
                    dataInputStream.close();
                    socket.close();
                    break;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void sendMessageToAll(String message) throws Exception {
        for (Socket onlineSocket : Server.onlineSockets) {
            OutputStream outputStream=onlineSocket.getOutputStream();
            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        }
    }
}
