/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide;

import Structures.Ator;
import Structures.Filme;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author denes
 */
public class ServerFiles {
    private ArrayList filmes = null;
    private ArrayList atores = null;
    String PATH_FILMES;
    String PATH_ATORES;
    
    public ServerFiles(String PATH_ATORES, String PATH_FILMES)
    {
        this.PATH_FILMES = PATH_FILMES;
        this.PATH_ATORES = PATH_ATORES;
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
    
    private void Save()
    {
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
    
    public void printActors() {
        for (int i = 0; i < this.atores.size(); i++) {
            Ator ator = (Ator) this.atores.get(i);
            System.out.println("Ator " + ator.name);
        }
    }
}
