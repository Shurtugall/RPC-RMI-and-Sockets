package ClientSide;

import java.io.*;
import java.net.*;
import java.util.Random;

/*
    Representa o cliente

    Mantem uma referencia para o socket

    Funcao Connect -> Connecta no socket
    Funcao RequestToServer -> Manda mensagem pro server e bloqueia para pegar resposta

    Comandos sao

    LIST ALL ATORES // pede ao servidor todos atores
    LIST ALL FILMES // pede ao servidor todos filmes

    ADD ATOR ID,NOME,IDADE,BIOGRAFIA // adiciona um ator, campos devem ser preenchidos ID(int),NOME(string),IDADE(int),BIOGRAFIA(string)
    ADD FILME ID,NOME,DURACAO,TIPO,ATORID1,...,ATORIDN // adiciona um filme, campos devem ser preenchidos ID(int),NOME(string),DURACAO(string),tipo(string),atoresIDS(varios ints separado por,)

    DELETE FILME ID // Deleta filme com id ID
    DELETE ATOR ID  // Deleta ator com id ID

    MODIFY ATOR ID ATTRIBUTE VALUE // modifica um ator com id ID, ATTRIBUTE é o nome do atributo e valor é VALUE (listados ali na linha 26 e 27 o nome e tipo)
    MODIFY FILME ID ATTRIBUTE VALUE
    MODIFY FILME ID ATORES VALUE (ADD|SUB)

    LIST ATORES WHERE ATTRIBUTE == VALUE // lista atores que tem atributo igual a tal valor
    LIST FILMES WHERE ATTRIBUTE == VALUE

    LIST ATORES IN FILME ID == VALUE // lista atores que estao no filme com id tal
    FILST FILMES IN ATORES ID == VALUE // lista filmes que tem o ator com id tal
*/

public class Client 
{
    private Socket socket = null;
    private PrintStream sender = null;
    private BufferedReader reciever = null;   
    
    public boolean Connect()
    {
        try
        {
            this.socket = new Socket("localhost", 9999);

            this.sender = new PrintStream(socket.getOutputStream()); 

            this.reciever = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            return true;
        } 
        catch(IOException ex) 
        {
            return false;
        }
    }

    public String RequestToServer(String Message)
    {
        try
        {
            this.sender.println(Message); 
            
            String resposta = this.reciever.readLine();

            return resposta;
        }
        catch(IOException ex) 
        {
            return null;
        }
    }
}
