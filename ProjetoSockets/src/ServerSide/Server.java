package ServerSide;

import Estruturas.Filme;
import Estruturas.Ator;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/*
    Representa um server

    Possui Lista de filmes e lista de atores

    Funcao Connect -> chamada para ligar o servidor
    Funções OnServerStart -> é chamada apos o Connect, abres os arquivos de filmes e atores
    Funcao ValidateRelation(int) -> Quando deleta-se um ator procura nos filmes que o referencia então deleta-se tambem
    Funcao Save() -> Salva alteracoes feitas nos filmes ou atores
    Funcao RunClients() -> Aguarda um cliente querer conectar, e entao cria uma thread para ele, utiliza a classe ThreadServer para lidar com o cliente

    Demais funcoes estao tão descrita no projeto RMI arquivo ServerInteface
*/

public class Server
{
    ServerSocket serverSocket = null;
    
    private ArrayList filmes = null;
    private ArrayList atores = null;
    private int count = 0;
            
    private void OnServerStart()
    {
        String PATH_FILMES = "C:/Users/Rodrigo Astros/Desktop/filmes.txt";
        String PATH_ATORES = "C:/Users/Rodrigo Astros/Desktop/atores.txt";
        
        System.out.println("[Server] Tentando abrir arquivo de filmes: " + PATH_FILMES);
        
        this.filmes = Filme.OpenFilmes(PATH_FILMES);
        
        if(this.filmes == null)
            System.out.println("[Server] Falha ao abrir arquivo de filmes: " + PATH_FILMES);
        else
            System.out.println("[Server] Abriu com sucesso arquivo: " + PATH_FILMES);
        
        ///////////////////////////////////////////////////////////////////////////////////
        
        System.out.println("[Server] Tentando abrir arquivo de atores: " + PATH_ATORES);
        
        this.atores = Ator.OpenAtores(PATH_ATORES);
        
        if(this.atores == null)
        {
            System.out.println("[Server] Falha ao abrir arquivo de atores: " + PATH_ATORES);
        }
        else
        {
            System.out.println("[Server] Abriu com sucesso atores: " + PATH_ATORES);
        }
    }
    
    public boolean Connect()
    {
        try 
        {
            this.serverSocket = new ServerSocket(9999);        
            
            this.OnServerStart();
        } 
        catch(IOException ex)
        {
            return false;
        }
    
        return true;
    }
    
    private void Save()
    {
        String PATH_FILMES = "C:/Users/Rodrigo Astros/Desktop/filmes.txt";
        String PATH_ATORES = "C:/Users/Rodrigo Astros/Desktop/atores.txt";
        
        try 
        {
            Filme.SalvaFilmes(PATH_FILMES, this.filmes);
            Ator.SaveAtores(PATH_ATORES, this.atores);
        }
        catch(FileNotFoundException ex) 
        {
            return;
        }
    }
    
    private void ValidateRelation(int new_id)
    {        
        for(int i = 0; i < this.filmes.size(); i++)
        {
            Filme filme = (Filme)this.filmes.get(i);
            
            ArrayList to_delete = new ArrayList();
            
            for(int j = 0; j < filme.actorsID.size(); j++)
            {
                boolean finded = false;
                
                int actorID = (int)filme.actorsID.get(j);
                
                for(Object obj : this.atores)
                {
                    Ator ator = (Ator)obj;
                    
                    if(ator.ID == actorID)
                    {
                        finded = true;       
                        
                        break;
                    }
                }
                
                if(!finded) to_delete.add(actorID);                               
            }
            
            System.out.println(to_delete);
            
            for(Object d : to_delete)
            {
                int delete = (int)d;
                
                for(int j = 0; j < filme.actorsID.size(); j++)
                {
                    int actorID = (int)filme.actorsID.get(j);
                    
                    if(actorID == delete && new_id != -1)
                    {
                        filme.actorsID.set(j, new_id);
                        
                        this.filmes.set(i, filme);
                        
                        break;
                    }
                    else if(actorID == delete)
                    {
                        filme.actorsID.remove(j);
                        
                        this.filmes.set(i, filme);
                        
                        break;
                    }   
                }
            }
        }
    }
    
    public ArrayList GetAtores()
    {
        return this.atores;
    }
    
    public ArrayList GetFilmes()
    {
        return this.filmes;
    }

    public ArrayList GetAtoresByAttribute(String value, String attribute) 
    {
        ArrayList returnList = new ArrayList();
        
        if(attribute.equalsIgnoreCase("ID"))
        {
            for(Object obj : this.atores)
            {
                Ator ator = (Ator)obj;

                if(ator.ID == Integer.parseInt(value))
                {
                    returnList.add(ator);
                }
            }
            
            return returnList;
        }
        else if(attribute.equalsIgnoreCase("NOME"))
        {
            for(Object obj : this.atores)
            {
                Ator ator = (Ator)obj;

                if(ator.name.endsWith(value))
                {
                    returnList.add(ator);
                }
            }
            
            return returnList;
        }
        else if(attribute.equalsIgnoreCase("IDADE"))
        {
            for(Object obj : this.atores)
            {
                Ator ator = (Ator)obj;

                if(ator.idade == Integer.parseInt(value))
                {
                    returnList.add(ator);
                }
            }
            
            return returnList;
        }
        else if(attribute.equalsIgnoreCase("BIOGRAFIA"))
        {
            for(Object obj : this.atores)
            {
                Ator ator = (Ator)obj;

                if(ator.biografia.contains(value))
                {
                    returnList.add(ator);
                }
            }
            
            return returnList;
        }
        else if(attribute.equalsIgnoreCase("RELATIONAL"))
        {
            for(Object obj : this.atores)
            {
                Ator ator = (Ator)obj;

                if(ator.ID == Integer.parseInt(value))
                {
                    for(Object obj2 : this.filmes) 
                    {
                        Filme filme = (Filme) obj2;
                        
                        for(Object obj3 : filme.actorsID)
                        {
                            if((int)obj3 == ator.ID)
                            {
                                returnList.add(filme);
                            }
                        } 
                    }
      
                    break;
                }
            }
            
            return returnList;
        }
        
        return null;
    }

    public ArrayList GetFilmesByAttribute(String value, String attribute)
    {
        ArrayList returnList = new ArrayList();
        
        if(attribute.equalsIgnoreCase("ID"))
        {
            for(Object obj : this.filmes)
            {
                Filme filme = (Filme)obj;

                if(filme.ID == Integer.parseInt(value))
                {
                    returnList.add(filme);
                }
            }
            
            return returnList;
        }
        else if(attribute.equalsIgnoreCase("NOME"))
        {
            for(Object obj : this.filmes)
            {
                Filme filme = (Filme)obj;

                if(filme.name.equalsIgnoreCase(value))
                {
                    returnList.add(filme);
                }
            }
            
            return returnList;
        }
        else if(attribute.equalsIgnoreCase("DURACAO"))
        {
            for(Object obj : this.filmes)
            {
                Filme filme = (Filme)obj;

                if(filme.duration.equalsIgnoreCase(value))
                {
                    returnList.add(filme);
                }
            }
            
            return returnList;
        }
        else if(attribute.equalsIgnoreCase("TIPO"))
        {
            for(Object obj : this.filmes)
            {
                Filme filme = (Filme)obj;

                if(filme.type.equalsIgnoreCase(value))
                {
                    returnList.add(filme);
                }
            }
            
            return returnList;
        }
        else if(attribute.equalsIgnoreCase("RELATIONAL"))
        {
            for(Object obj : this.filmes)
            {
                Filme filme = (Filme)obj;

                if(filme.ID == Integer.parseInt(value))
                {
                    for(Object obj2 : filme.actorsID)
                    {
                        int id = (int)obj2;
                        
                        for(Object obj3 : this.atores)
                        {
                            Ator ator = (Ator)obj3;
                            
                            if(ator.ID == id)
                            {
                                returnList.add(ator);
                            }
                        }
                    }
                    
                    break;
                }
            }
            
            return returnList;
        }
        
        return null;
    }

    public boolean AddAtor(Ator ator)
    {
        for(Object obj : this.atores)
        {
            Ator ator2 = (Ator)obj;
            
            if(ator2.ID == ator.ID)
            {
                return false;
            }
        }
        
        this.atores.add(ator);
        this.Save();
        
        return true;
    }

    public boolean AddFilme(Filme filme)
    {
        for(Object obj : filme.actorsID) 
        {
            int id = (int)obj;
            
            boolean find = false;
            
            for(Object obj2 : this.atores) 
            {
                int id_ = ((Ator)obj2).ID;
                
                if(id == id_)
                {
                    find = true;
                    break;
                }
            }
            
            if(!find) return false;
        }
        
        for(Object obj : this.filmes)
        {
            Filme filme2 = (Filme)obj;
            
            if(filme2.ID == filme.ID)
            {
                return false;
            }
        }
        
        this.filmes.add(filme);
        this.Save();
        
        return true;
    }

    public Filme DeleteFilme(int ID)
    {
        Filme filmeSave = null;
        
        for(Object obj : this.filmes)
        {
            Filme filme = (Filme)obj;
            
            if(filme.ID == ID)
            {
                filmeSave = filme;
                
                this.filmes.remove(filme);
                this.Save();
                
                return filmeSave;
            }
        }
        
        return null;
    }

    public Ator DeletaAtor(int ID)
    {
        Ator atorSave = null;
        
        for(Object obj : this.atores)
        {
            Ator ator = (Ator)obj;
            
            if(ator.ID == ID)
            {
                atorSave = ator;
                
                this.atores.remove(ator);          
                this.ValidateRelation(-1);     
                this.Save();
                
                return atorSave;
            }
        }
        
        return null;
    }

    public boolean ModifyAtorAttribute(int ID, String value, String attribute)
    {
        Ator ator = null;
       
        int index = -1, count = 0;
        boolean find = false;
        for(int i = 0; i < this.atores.size(); i++)
        {
            if(count < 1) index++;
            
            Ator ator2 = (Ator)this.atores.get(i);

            if(ator2.ID == ID)
            {
                ator = ator2;
                
                find = true;
                
                count++;
            }
        }
        
        if(attribute.equalsIgnoreCase("ID") && count > 1) return false;
        if(!find) return false;
        
        if(attribute.equalsIgnoreCase("ID"))
        {
            ator.ID = Integer.parseInt(value);
            
            this.atores.set(index, ator);
            
            this.ValidateRelation(ator.ID);
        }
        else if(attribute.equalsIgnoreCase("NOME"))
        {
            ator.name = value;
            
            this.atores.set(index, ator);
        }
        else if(attribute.equalsIgnoreCase("IDADE")) 
        {
            ator.idade = Integer.parseInt(value);
            
            this.atores.set(index, ator);
        }
        else if(attribute.equalsIgnoreCase("BIOGRAFIA"))
        {
            ator.biografia = value;
            
            this.atores.set(index, ator);
        }
        else
        {
            return false;
        }
        
        this.Save();
        return true;
    }

    public boolean ModifyFilmeAttribute(int ID, String value, String attribute)
    {
        Filme filme = null;

        int index = -1, count = 0;
        boolean find = false;
        for(int i = 0; i < this.filmes.size(); i++)
        {
            if(count < 1) index++;
            
            Filme filme2 = (Filme)this.filmes.get(i);

            if(filme2.ID == ID)
            {
                filme = filme2;
                
                find = true;
                
                count++;
            }
        }
        
        if(attribute.equalsIgnoreCase("ID") && count > 1) return false;
            
        if(!find) return false;
        
        if(attribute.equalsIgnoreCase("ID"))
        {
            filme.ID = Integer.parseInt(value);
            
            this.filmes.set(index, filme);
        }
        else if(attribute.equalsIgnoreCase("NOME"))
        {
            filme.name = value;
            
            this.filmes.set(index, filme);
        }
        else if(attribute.equalsIgnoreCase("DURACAO")) 
        {
            filme.duration = value;
            
            this.filmes.set(index, filme);
        }
        else if(attribute.equalsIgnoreCase("TIPO"))
        {
            filme.type = value;
            
            this.filmes.set(index, filme);
        }
        else if(attribute.equalsIgnoreCase("ADD"))
        {
            boolean exists = false;
            
            for(Object obj : this.atores)
            {
                Ator ator = (Ator)obj;
                
                if(ator.ID == Integer.parseInt(value))
                {
                    exists = true;
                    break;
                }
            }
            
            Filme f = (Filme)this.filmes.get(index);
            
            if(f.actorsID.contains(Integer.parseInt(value))) return true;
            
            if(!exists) return false;
            
            filme.actorsID.add(Integer.parseInt(value));
            
            this.filmes.set(index, filme);
        
        }
        else if(attribute.equalsIgnoreCase("SUB"))
        {
            filme.actorsID.remove(Integer.parseInt(value));
            
            this.filmes.set(index, filme);
        }
        else
        {
            return false;
        }
        
        this.Save();
        return true;
    }
    
    public void RunClients() throws IOException
    {
        final Socket socket = serverSocket.accept();
        final Server server = this;
        
        new Thread() 
        {
        @Override
        public void run() 
        {
            int myID = server.count ++;
            
            try 
            {
                PrintStream out = new PrintStream(socket.getOutputStream());

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                ThreadServer thread = new ThreadServer(server, out, in, myID);
                
                while(true)
                {
                    thread.Run();
                }
            } 
            catch(IOException ex) 
            {
                System.out.println("Cliente " + myID + " saiu");
                    
                this.interrupt();
            }
            
            this.interrupt();
        }
      }.start();
    } 
}