/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide;

/**
 *
 * @author denes
 */
import java.io.*;
import java.net.*;

class Server
{
    public static void main(String[] args) throws Exception 
    { 
        ServerSocket sersock = new ServerSocket(3000); 
        System.out.println("Server ready"); 
        ServerFiles serverFiles = new ServerFiles("./atores.txt", "./filmes.txt");
        Socket sock = sersock.accept();
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in)); 
        OutputStream ostream = sock.getOutputStream(); 
        PrintWriter pwrite = new PrintWriter(ostream, true);  
        InputStream istream = sock.getInputStream(); 
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));   
        Functions functions = new Functions(pwrite, serverFiles);
        String fun;
        while(true)
        {
            fun = receiveRead.readLine();
            if(fun != null){
                System.out.println("Operation : " + fun);
                functions.runClientRequest(fun);  
            } else {
                pwrite.println("Funcao nao definida");
            }
            System.out.flush();
        }
    }
}