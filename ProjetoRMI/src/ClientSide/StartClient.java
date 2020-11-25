package ClientSide;

import ClientSide.Client;
import java.rmi.RemoteException;
import java.util.Scanner;

public class StartClient
{
    public static void main(String[] args) throws RemoteException
    {
        Client client = new Client();
            
        if(!client.FindServer())
        {
            System.out.println("Não possivel localizar o servidor!");
            
            System.exit(1);
        }
            
        Scanner sc = new Scanner(System.in);
            
        while(true)
        {
            String command = sc.nextLine();              
                
            client.DoCommand(command);
        }    
    }
}