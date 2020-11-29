/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

/**
 *
 * @author denes
 */

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
import java.io.*;
import java.net.*;
class Client
{
    public static void main(String[] args) throws Exception
    {
        Socket sock = new Socket("192.168.0.107", 3000); 
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in)); 
        OutputStream ostream = sock.getOutputStream(); 
        PrintWriter pwrite = new PrintWriter(ostream, true); 
        InputStream istream = sock.getInputStream(); 
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));   
        System.out.println("Client ready, type and press Enter key");
        String receiveMessage, sendMessage,temp; 
        while(true) 
        {
            System.out.println("Enter operation: ");
            temp = keyRead.readLine();
            sendMessage=temp.toLowerCase();
            pwrite.println(sendMessage);
            System.out.flush();
            if((receiveMessage = receiveRead.readLine()) != null) {
                receiveMessage = receiveMessage.replaceAll("%", "\n");
                System.out.println(receiveMessage);
            }
        }
    }
}
