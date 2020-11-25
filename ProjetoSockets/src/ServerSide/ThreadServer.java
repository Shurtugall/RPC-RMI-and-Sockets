package ServerSide;

import Estruturas.Ator;
import Estruturas.Filme;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/*
    Classe utilizada pela thread que serve um cliente, a classe que representa o Servidor em si, é a classe Server

    Mantem referencia para a classe Server e o socket

    Funcao Run() -> Le um comando do cliente, interpreta e usa uma das funcoes da classe Server
*/

public class ThreadServer
{
    private Server server = null;
    
    private PrintStream out = null;
    private BufferedReader in = null;
    
    int ThreadID = 0;
    
    ThreadServer(Server server, PrintStream out, BufferedReader in, int ThreadID)
    {
        this.server = server;
        this.out = out;
        this.in = in;
        this.ThreadID = ThreadID;
    }
    
    public void Run() throws IOException
    {
        String command = in.readLine();

        System.out.println("[Thread " + this.ThreadID + "] " + command);

        if(command.startsWith("ADD ATOR "))
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

                if(server.AddAtor(ator))
                {
                    out.println("O Ator {id = " + ator.ID + "| nome = " + ator.name + "| idade = " + ator.idade + "| biografia = " + ator.biografia + "} FOI ADICIONADO!!!");
                } 
                else 
                {
                    out.println("Servidor falhou ao adicionar, provavelmente o ID ja foi usado.");
                }
            } 
            else 
            {
                out.println("Falha, comando mal formatado!");
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
                if(server.AddFilme(filme)) 
                {
                    out.println("O Filme {id = " + filme.ID + "| nome = " + filme.name + "| duração = " + filme.duration + "| tipo = " + filme.type + "| Atores(IDs) = " + filme.actorsID.toString() + "} FOI ADICIONADO!!!");
                } 
                else 
                {
                    out.println("Servidor falhou ao adicionar! ID ja usado ou Ator do Filme inserido não existe!");
                }
            }
            else 
            {
                out.println("Falha, comando mal formatado!");
            }
        }
        else if(command.startsWith("MODIFY FILME ID ")) 
        {
            command = command.substring(16);

            String[] vals = command.split(" ");

            if(vals.length == 3) 
            {
                if(server.ModifyFilmeAttribute(Integer.parseInt(vals[0]), vals[2], vals[1])) 
                {
                    out.println("EDITADO COM SUCESSO!!!");
                } 
                else 
                {
                    out.println("Falhao modificar atributo!!! Filme/atributo não encontrado ou ID já usado");
                }
            }
            else if(vals.length == 4 && vals[3].equalsIgnoreCase("ADD") || vals[3].equalsIgnoreCase("SUB") && vals[2].equalsIgnoreCase("ATORES"))
            {
                if(server.ModifyFilmeAttribute(Integer.parseInt(vals[0]), vals[2], vals[3]))
                {
                    out.println("EDITADO COM SUCESSO!!!");
                } 
                else
                {
                    out.println("Falhao modificar atributo!!! Filme/atributo não encontrado!");
                }
            } 
            else 
            {
                out.println("Falha, comando mal formatado!");
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
                    att += vals[i] + ' ';
                }

                if(server.ModifyAtorAttribute(Integer.parseInt(vals[0]), att, vals[1])) 
                {
                    out.println("EDITADO COM SUCESSO!!!");
                } 
                else
                {
                    out.println("Falhao modificar atributo!!! Ator/atributo não encontrado ou ID já usado");
                }
            }
            else 
            {
                out.println("Falha, comando mal formatado!");
            }
        } 
        else if(command.startsWith("DELETE FILME ")) 
        {
            command = command.substring(13);

            Filme filme = server.DeleteFilme(Integer.parseInt(command));

            if(filme != null)
            {
                out.println("O Filme {id = " + filme.ID + "| nome = " + filme.name + "| duração = " + filme.duration + "| tipo = " + filme.type + "| Atores(IDs) = " + filme.actorsID.toString() + "} FOI DELETADO!!!");
            }
            else 
            {
                out.println("Nao foi possivel deletar, provavelmente filme não existe");
            }
        } 
        else if(command.startsWith("DELETE ATOR ")) 
        {
            command = command.substring(12);

            Ator ator = server.DeletaAtor(Integer.parseInt(command));

            if(ator != null)
            {
                out.println("O Ator {id = " + ator.ID + "| nome = " + ator.name + "| idade = " + ator.idade + "| biografia = " + ator.biografia + "} FOI DELETADO!!!");
            } 
            else 
            {
                out.println("Nao foi possivel deletar, provavelmente ator não existe");
            }
        } ////////// MULTILNE ABAIXO
        else if(command.equalsIgnoreCase("LIST ALL ATORES")) 
        {
            String message = "@";

            ArrayList list = server.GetAtores();

            if(list != null)
            {
                for(Object obj : list)
                {
                    Ator ator = (Ator) obj;

                    message += ("- ATOR {id = " + ator.ID + "| nome = " + ator.name + "| idade = " + ator.idade + "| biografia = " + ator.biografia + " }&div&");
                }

                out.println(message);
            }
        } 
        else if(command.equalsIgnoreCase("LIST ALL FILMES"))
        {
            String message = "@";

            ArrayList list = server.GetFilmes();

            if(list != null) 
            {
                for(Object obj : list) 
                {
                    Filme filme = (Filme) obj;

                    message += ("- FILME {id = " + filme.ID + "| nome = " + filme.name + "| duração = " + filme.duration + "| tipo = " + filme.type + "| Atores(IDs) = " + filme.actorsID.toString() + "}&div&");
                }

                out.println(message);
            }
        } 
        else if(command.startsWith("LIST ATORES WHERE ")) 
        {
            String message = "@";

            command = command.substring(18);

            String[] vals = command.split(" ");

            if(vals.length == 3 && vals[1].equals("==")) 
            {
                ArrayList list = server.GetAtoresByAttribute(vals[2], vals[0]);

                if(list != null)
                {
                    for(Object obj : list)
                    {
                        Ator ator = (Ator) obj;

                        message += ("- ATOR {id = " + ator.ID + "| nome = " + ator.name + "| idade = " + ator.idade + "| biografia = " + ator.biografia + "}&div&");
                    }

                    out.println(message);
                }
            } 
            else 
            {
                out.println("Falha, comando mal formatado!");
            }
        } 
        else if(command.startsWith("LIST FILMES WHERE ")) 
        {
            String message = "@";

            command = command.substring(18);

            String[] vals = command.split(" ");

            if(vals.length == 3 && vals[1].equals("==")) 
            {
                ArrayList list = server.GetFilmesByAttribute(vals[2], vals[0]);

                if(list != null) 
                {
                    for(Object obj : list)
                    {
                        Filme filme = (Filme) obj;

                        message += ("Filme {id = " + filme.ID + "| nome = " + filme.name + "| duração = " + filme.duration + "| tipo = " + filme.type + "| Atores(IDs) = " + filme.actorsID.toString() + "}&div&");
                    }

                    out.println(message);
                }
            } 
            else 
            {
                out.println("Falha, comando mal formatado!");
            }
        } 
        else if(command.startsWith("LIST ATORES IN FILME ID == "))
        {
            String message = command.substring(27);

            String message2 = "@";

            ArrayList list = server.GetFilmesByAttribute(message, "RELATIONAL");

            if(list != null) 
            {
                for(Object obj : list) 
                {
                    Ator ator = (Ator) obj;

                    message2 += ("Ator {id = " + ator.ID + "| nome = " + ator.name + "| idade = " + ator.idade + "| biografia = " + ator.biografia + "}&div&");
                }

                out.println(message2);
            }
        } 
        else if(command.startsWith("LIST FILMES IN ATOR ID == "))
        {
            String message = command.substring(26);

            String message2 = "@";

            ArrayList list = server.GetAtoresByAttribute(message, "RELATIONAL");

            if(list == null) 
            {
                return;
            }

            for(Object obj : list) 
            {
                Filme filme = (Filme) obj;

                message2 += ("Filme {id = " + filme.ID + "| nome = " + filme.name + "| duração = " + filme.duration + "| tipo = " + filme.type + "| Atores(IDs) = " + filme.actorsID.toString() + "}&div&");
            }

            out.println(message2);
        }
        else
        {
            out.println("???");
        }
    }
}
