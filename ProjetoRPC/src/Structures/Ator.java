package Structures;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

/**
    Representa um ator

    função OpenAtores retorna uma lista de atores aberta por .txt
    funcao SalvaAtores salva os atores, dada o nome do txt e lista
**/

public class Ator implements Serializable
{
    public int ID;
    public String name;
    public int idade;
    public String biografia;
    
    public Ator() { }
    
    public Ator(int id, String name, int idade, String biografia)
    {
        ID = id;
        this.name = name;
        this.idade = idade;
        this.biografia = biografia;
    }
    
    public static ArrayList OpenAtores(String file)
    {
        ArrayList atores = new ArrayList();

        try(BufferedReader br = new BufferedReader(new FileReader(file))) 
        {
            String line;
            
            while((line = br.readLine()) != null) 
            {
                Ator ator = new Ator();
                
                String[] data = line.split(",");
                
                ator.ID = Integer.parseInt(data[0]);
                ator.name = data[1];
                ator.idade = Integer.parseInt(data[2]);
                ator.biografia = data[3];    
                
                atores.add(ator);
            }
            
            return atores;
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
    
    public static void SaveAtores(String file, ArrayList list) throws FileNotFoundException
    {
        PrintWriter writer = new PrintWriter(file);
        
        for(Object obj : list)
        {
            Ator ator = (Ator)obj;
            
            writer.println(ator.ID + "," + ator.name + "," + ator.idade + "," + ator.biografia);
        }
        
        writer.close();
    }
}
