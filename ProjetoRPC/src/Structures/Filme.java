package Structures;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

/**
    Representa um Filme

    função OpenFilmes retorna uma lista de filmes aberta por .txt 
    função SalvaFilmes recebe o nome a ser salvo e a lista
**/

public class Filme implements Serializable
{
    public int ID;
    public String name;
    public String duration;
    public String type;
    public ArrayList actorsID = new ArrayList();
    
    public Filme() { }
    
    public Filme(int ID, String name, String duration, String type, ArrayList actors) {
        this.ID = ID;
        this.name = name;
        this.duration = duration;
        this.type = type;
        for (int i = 0; i < actors.size(); i++) {
            this.actorsID.add(Integer.parseInt((String) actors.get(i)));
        }
    }
    
    public static ArrayList OpenFilmes(String file)
    {
        ArrayList filmes = new ArrayList();

        try(BufferedReader br = new BufferedReader(new FileReader(file))) 
        {
            String line;
            
            while((line = br.readLine()) != null) 
            {
                Filme filme = new Filme();
                
                String[] data = line.split(",");
                
                filme.ID = Integer.parseInt(data[0]);
                filme.name = data[1];
                filme.duration = data[2];
                filme.type = data[3];
                
                line = br.readLine();
                
                if(line != null && line.length() > 0)
                {
                    String[] atores = line.split(" ");
                    
                    for(String ator : atores) 
                    {
                        filme.actorsID.add(Integer.parseInt(ator));
                    }
                }
                
                filmes.add(filme);
            }
            
            return filmes;
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("Erro ao abrir arquivo " + file + " : " + ex.getMessage());
        }
        catch(IOException ex)
        {
           System.out.println("Erro ao abrir arquivo " + file + " : " + ex.getMessage());
        }
        
        return null;
    }
    
    public static void SalvaFilmes(String file, ArrayList list) throws FileNotFoundException
    {
        PrintWriter writer = new PrintWriter(file);
        
        for(Object obj : list)
        {
            Filme filme = (Filme)obj;
            
            writer.println(filme.ID  + "," + filme.name + "," + filme.duration + "," + filme.type);
            
            String line = "";
            
            for(Object id : filme.actorsID) 
            {
                int num = (int)id;
                
                line += id.toString() + ' ';
            }
            
            if(line.length() > 0) 
            {
                line = line.substring(0, line.length()-1);
            }
            
            writer.println(line);
        }
        
        writer.close();
    }
}
