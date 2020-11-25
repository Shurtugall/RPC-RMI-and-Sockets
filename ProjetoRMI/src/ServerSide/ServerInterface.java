package ServerSide;

import Estruturas.Ator;
import Estruturas.Filme;
import java.rmi.*;
import java.util.ArrayList;

/* Funcoes que o server ofere aos clientes */

public interface ServerInterface extends Remote
{
    public ArrayList GetAtores() throws RemoteException; // Servidor retorna todos atores
    public ArrayList GetFilmes() throws RemoteException; // Servidor retorna todos filmes
    
    public ArrayList GetAtoresByAttribute(String value, String attribute) throws RemoteException; // Pega uma lista de atores que satisfaz um valor para um determinado atributo
    public ArrayList GetFilmesByAttribute(String value, String attribute) throws RemoteException;// Pega uma lista de filmes que satisfaz um valor para um determinado atributo
    
    public boolean AddAtor(Ator ator) throws RemoteException; // Adiciona um Ator, bool retornado indica se sucesso
    public boolean AddFilme(Filme filme) throws RemoteException; // Adiciona um Filme, bool retornado indica se sucesso
    
    public Filme DeleteFilme(int ID)throws RemoteException; // Delete um Filme, retorna o Filme que foi deletado, caso falhar retorna null
    public Ator DeletaAtor(int ID)throws RemoteException; // Delete um Ator, retorna o Ator que foi deletado, caso falhar retorna null
    
    public boolean ModifyAtorAttribute(int ID, String value, String attribute) throws RemoteException; // Edita um attributo de algum ator
    public boolean ModifyFilmeAttribute(int ID, String value, String attribute) throws RemoteException; // Editar um attributo de algum filme
} 