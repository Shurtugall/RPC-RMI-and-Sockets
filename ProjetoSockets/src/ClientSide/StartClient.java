package ClientSide;

import java.io.IOException;
import java.util.Scanner;

public class StartClient
{
    public static void main(String[] args) throws IOException 
    {
        Client c = new Client();
        
        if(c.Connect())
        {
            System.out.println("Conectado no server");
        }
        
        Scanner sc = new Scanner(System.in);
            
        while(true)
        {
            String command = sc.nextLine();              
            
            String resposta = c.RequestToServer(command);
            
            if(resposta.startsWith("@"))
            {
                String[] vals  = resposta.substring(1).split("&div&");
                
                for(String s : vals)
                {
                    System.out.println("[Server]: " + s);    
                }
            }
            else
            {
                System.out.println("[Server]: " + resposta);     
            }
        }
    }   
}
