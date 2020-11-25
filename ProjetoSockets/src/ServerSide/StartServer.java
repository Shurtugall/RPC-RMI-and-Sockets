package ServerSide;

import java.io.IOException;

public class StartServer
{
    public static void main(String[] args) throws IOException 
    {
       Server s = new Server();
       
      if(s.Connect())
      {
          System.out.println("[Server] Conectado");
      }
       
       while(true) s.RunClients();
    }   
}
