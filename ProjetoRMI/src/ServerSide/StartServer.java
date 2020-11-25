package ServerSide;

import java.rmi.RemoteException;

public class StartServer
{
    public static void main(String[] args) throws RemoteException
    {
        if(Server.RegisterServer())
        {
            System.out.println("[Server] Server conectado e pronto!");
        }
        else
        {
            System.out.println("[Server] Falhou em conectar");
        }
    }
}
