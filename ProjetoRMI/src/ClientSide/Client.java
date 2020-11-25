package ClientSide;

import ServerSide.ServerInterface;
import Estruturas.Ator;
import Estruturas.Filme;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/*
    Representa o cliente

    Mantem uma referencia para o servidor

    Funcao FindServer -> Procura referencia ao servidor
    Funcao DoCommand -> È meia grande, mas le um comando de entrada do usuario e manda chama alguma funcao do servidor

    Comandos sao

    EXIT // Sai

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

    LIST ATORES IN FILME ID == VALUE // list aatores que estao no filme com id tal
    FILST FILMES IN ATORES ID == VALUE // lista filmes que tem o ator com id tal
*/

public class Client 
{
    public ServerInterface server;
    
    public Client()
    {
       this.server = null;
    }
    
    public boolean FindServer()
    {
        try 
        {
            this.server = (ServerInterface)Naming.lookup("rmi://127.0.0.1/server");
        } 
        catch(NotBoundException | MalformedURLException | RemoteException ex)
        {
            return false;
        }
        
        return true;
    }
    
    public void DoCommand(String command) throws RemoteException
    {
        if(command.equalsIgnoreCase("EXIT")) 
        {
            System.exit(0);
        }
        else if(command.startsWith("ADD ATOR ")) 
        {
            command = command.substring(9);

            String[] vals = command.split(",");

            if(vals.length >= 4) 
            {
                Ator ator = new Ator();

                ator.ID = Integer.parseInt(vals[0]);
                ator.name = vals[1];
                ator.idade = Integer.parseInt(vals[2]);
                ator.biografia = "";
                
                for(int i = 3; i < vals.length; i++) 
                {
                    ator.biografia += vals[i] + ' ';
                }

                if(this.server.AddAtor(ator)) 
                {
                    System.out.println("O Ator {id = " + ator.ID + "| nome = " + ator.name + "| idade = " + ator.idade + "| biografia = " + ator.biografia + "} FOI ADICIONADO!!!");
                } 
                else 
                {
                    System.out.println("Servidor falhou ao adicionar, provavelmente o ID ja foi usado.");
                }
            } 
            else 
            {
                System.out.println("Falha, comando mal formatado!");
            }          
        } 
        else if(command.startsWith("ADD FILME ")) 
        {
            command = command.substring(10);

            String[] vals = command.split(",");

            if(vals.length >= 5) 
            {
                Filme filme = new Filme();

                filme.ID = Integer.parseInt(vals[0]);
                filme.name = vals[1];
                filme.duration = vals[2];
                filme.type = vals[3];

                for(int i = 4; i < vals.length; i++) 
                {
                    filme.actorsID.add(Integer.parseInt(vals[i]));
                }

                if(this.server.AddFilme(filme)) 
                {
                    System.out.println("O Filme {id = " + filme.ID + "| nome = " + filme.name + "| duração = " + filme.duration + "| tipo = " + filme.type + "| Atores(IDs) = " + filme.actorsID.toString() + "} FOI ADICIONADO!!!");
                } 
                else 
                {
                    System.out.println("Servidor falhou ao adicionar! ID ja usado ou Ator do Filme inserido não existe!");
                }
            } 
            else 
            {
                System.out.println("Falha, comando mal formatado!");
            }
        }
        /////////////////////// MODIFY FILME NAME 
        else if(command.startsWith("MODIFY FILME ID "))
        {
            command = command.substring(16);
            
            String[] vals = command.split(" ");
            
            if(vals.length == 3)
            {
                if(this.server.ModifyFilmeAttribute(Integer.parseInt(vals[0]), vals[2], vals[1]))
                {
                    System.out.println("EDITADO COM SUCESSO!!!");
                }
                else
                {
                    System.out.println("Falhao modificar atributo!!! Filme/atributo não encontrado ou ID já usado");
                }
            }
            else if(vals.length == 4 && vals[3].equalsIgnoreCase("ADD") || vals[3].equalsIgnoreCase("SUB") && vals[2].equalsIgnoreCase("ATORES"))
            {
                if(this.server.ModifyFilmeAttribute(Integer.parseInt(vals[0]), vals[2], vals[3]))
                {
                    System.out.println("EDITADO COM SUCESSO!!!");
                }
                else
                {
                    System.out.println("Falhao modificar atributo!!! Filme/atributo não encontrado!");
                }
            }
            else
            {
                System.out.println("Falha, comando mal formatado!");
            }
        }
        else if(command.startsWith("MODIFY ATOR ID "))
        {
            command = command.substring(15);
            
            String[] vals = command.split(" ");
            
            if(vals.length >= 3)
            {
                String att = "";
                
                for(int i = 2; i < vals.length; i++) 
                {
                    att+= vals[i] + ' ';
                }              
                
                if(this.server.ModifyAtorAttribute(Integer.parseInt(vals[0]), att, vals[1]))
                {
                    System.out.println("EDITADO COM SUCESSO!!!");
                }
                else
                {
                    System.out.println("Falhao modificar atributo!!! Ator/atributo não encontrado ou ID já usado");
                }
            }
            else
            {
                System.out.println("Falha, comando mal formatado!");
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////////
        else if(command.startsWith("DELETE FILME "))
        {
            command = command.substring(13);

            Filme filme = this.server.DeleteFilme(Integer.parseInt(command));

            if(filme != null) 
            {
                System.out.println("O Filme {id = " + filme.ID + "| nome = " + filme.name + "| duração = " + filme.duration + "| tipo = " + filme.type + "| Atores(IDs) = " + filme.actorsID.toString() + "} FOI DELETADO!!!");
            }
            else 
            {
                System.out.println("Nao foi possivel deletar, provavelmente filme não existe");
            }
        } 
        else if(command.startsWith("DELETE ATOR ")) 
        {
            command = command.substring(12);

            Ator ator = this.server.DeletaAtor(Integer.parseInt(command));

            if(ator != null)
            {
                System.out.println("O Ator {id = " + ator.ID + "| nome = " + ator.name + "| idade = " + ator.idade + "| biografia = " + ator.biografia + "} FOI DELETADO!!!");
            } 
            else 
            {
                System.out.println("Nao foi possivel deletar, provavelmente ator não existe");
            }
        }
        else if(command.equalsIgnoreCase("LIST ALL ATORES")) 
        {
            ArrayList list = this.server.GetAtores();

            if(list != null) 
            {
                for(Object obj : list)
                {
                    Ator ator = (Ator) obj;

                    System.out.println("- ATOR {id = " + ator.ID + "| nome = " + ator.name + "| idade = " + ator.idade + "| biografia = " + ator.biografia + " }");
                }
            }
        } 
        else if (command.equalsIgnoreCase("LIST ALL FILMES"))
        {
            ArrayList list = this.server.GetFilmes();

            if(list != null) 
            {
                for(Object obj : list) 
                {
                    Filme filme = (Filme) obj;

                    System.out.println("- FILME {id = " + filme.ID + "| nome = " + filme.name + "| duração = " + filme.duration + "| tipo = " + filme.type + "| Atores(IDs) = " + filme.actorsID.toString() + "}");
                }
            }
        }
        else if(command.startsWith("LIST ATORES WHERE ")) 
        {
            command = command.substring(18);

            String[] vals = command.split(" ");

            if(vals.length == 3 && vals[1].equals("=="))
            {
                ArrayList list = this.server.GetAtoresByAttribute(vals[2], vals[0]);

                if(list != null) 
                {
                    for(Object obj : list) 
                    {
                        Ator ator = (Ator) obj;

                        System.out.println("- ATOR {id = " + ator.ID + "| nome = " + ator.name + "| idade = " + ator.idade + "| biografia = " + ator.biografia + "}");
                    }
                }
            }
            else 
            {
                System.out.println("Falha, comando mal formatado!");
            }
        } 
        else if(command.startsWith("LIST FILMES WHERE "))
        {
            command = command.substring(18);

            String[] vals = command.split(" ");

            if(vals.length == 3 && vals[1].equals("=="))
            {
                ArrayList list = this.server.GetFilmesByAttribute(vals[2], vals[0]);

                if(list != null) 
                {
                    for(Object obj : list)
                    {
                        Filme filme = (Filme) obj;

                        System.out.println("Filme {id = " + filme.ID + "| nome = " + filme.name + "| duração = " + filme.duration + "| tipo = " + filme.type + "| Atores(IDs) = " + filme.actorsID.toString() + "}");
                    }
                }
            }
            else 
            {
                System.out.println("Falha, comando mal formatado!");
            }
        } 
        else if(command.startsWith("LIST ATORES IN FILME ID == ")) 
        {
            String message = command.substring(27);

            ArrayList list = this.server.GetFilmesByAttribute(message, "RELATIONAL");

            if(list != null) 
            {
                for(Object obj : list) 
                {
                    Ator ator = (Ator) obj;

                    System.out.println("Ator {id = " + ator.ID + "| nome = " + ator.name + "| idade = " + ator.idade + "| biografia = " + ator.biografia + "}");
                }
            }
        } 
        else if(command.startsWith("LIST FILMES IN ATOR ID == ")) 
        {
            String message = command.substring(26);

            ArrayList list = this.server.GetAtoresByAttribute(message, "RELATIONAL");

            for(Object obj : list) 
            {
                Filme filme = (Filme) obj;

                System.out.println("Filme {id = " + filme.ID + "| nome = " + filme.name + "| duração = " + filme.duration + "| tipo = " + filme.type + "| Atores(IDs) = " + filme.actorsID.toString() + "}");
            }
        }
    }
}