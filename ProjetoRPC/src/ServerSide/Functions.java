package ServerSide;

import Structures.Ator;
import Structures.Filme;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;

/**
 *
 * @author denes, gabriel righi e rodrigo
 */
public class Functions {
    PrintWriter pwrite;
    ServerFiles serverFiles;
    
    public Functions(PrintWriter pwrite, ServerFiles serverFiles) {
        this.pwrite = pwrite;
        this.serverFiles = serverFiles;
    }
    
    public void runClientRequest(String functionName)
    {
        switch(functionName) {
            case "list all atores": this.ListAllActors();
                break;
            case "list all filmes": this.ListAllFilms();
                break;
            default: this.runRequestWithParams(functionName);
        }
    }
    
    public void runRequestWithParams(String functionName)
    {
        if(functionName.contains("add ator")) {
           this.addActor(functionName);
           pwrite.println("Ator adicionado!");
           return;
        }
        if(functionName.contains("add filme")) {
           this.addFilm(functionName);
           pwrite.println("Filme adicionado!");
           return;
        }
        if(functionName.contains("delete ator")) {
            this.deleteActor(functionName);
            pwrite.println("Ator Removido!");
            return;
         }
         if(functionName.contains("delete filme")) {
            this.deleteFilm(functionName);
            pwrite.println("Filme Removido!");
            return;
         }
         if(functionName.contains("modify ator")) {
            this.modifyActor(functionName);
            pwrite.println("Ator Alterado!");
            return;
         }
         if(functionName.contains("modify filme")) {
            this.modifyFilm(functionName);
            pwrite.println("Filme Alterado!");
            return;
         }
         if(functionName.contains("list atores where")) {
            this.listActorsWhere(functionName);
            return;
         }
         if(functionName.contains("list filmes where")) {
            this.listFilmsWhere(functionName);
            return;
         }
        pwrite.println("Funcao nao definida");
    }
            
    public void ListAllActors()
    {
        ArrayList<Ator> atores = serverFiles.GetAtores();
        String response = "Atores:%";
        for (int i = 0; i < atores.size(); i++) {
            Ator atorTemp = (Ator) atores.get(i);
            response += atorTemp.ID + ", " + atorTemp.name + ", " + atorTemp.idade + "," + atorTemp.biografia + ".%";
        }
        pwrite.println(response);
    }
    
    public void ListAllFilms()
    {
        ArrayList<Filme> filmes = serverFiles.GetFilmes();
        String response = "Filmes:%";
        for (int i = 0; i < filmes.size(); i++) {
            Filme filmTemp = (Filme) filmes.get(i);
            response +=  filmTemp.name + ", " + filmTemp.duration + ", " + filmTemp.type + ".%";
            for(int j = 0; j < filmTemp.actorsID.size(); j++) {
                response += filmTemp.actorsID.get(j).toString();
                if(j == filmTemp.actorsID.size() - 1){
                    response += ".%";
                } else {
                    response += ", ";
                }
            }
        }
        pwrite.println(response);
    }
    
    public void addActor(String params)
    {
        String allValues = params.substring(9, params.length());
        String[] values = allValues.split(",");
        Ator newActor = new Ator(parseInt(values[0]), values[1], parseInt(values[2]), values[3]);
        serverFiles.AddAtor(newActor);
    }
    
    public void addFilm(String params)
    {
        String allValues = params.substring(10, params.length());
        String[] values = allValues.split(",");
        ArrayList actors = new ArrayList();
        for (int i = 4; i < values.length; i++) {
            actors.add(values[i]);
        }
        Filme newFilm = new Filme(parseInt(values[0]), values[1], values[2], values[3], actors);
        serverFiles.AddFilme(newFilm);
    }
    
    public void deleteActor(String params)
    {
        String id = params.substring(12, params.length());
        serverFiles.DeletaAtor(parseInt(id));
    }
    
    public void deleteFilm(String params)
    {
        String id = params.substring(13, params.length());
        serverFiles.DeleteFilme(parseInt(id));
    }

    public void modifyActor(String params)
    {
        String allValues = params.substring(12, params.length());
        String[] values = allValues.split(" ");
        serverFiles.ModifyAtorAttribute(parseInt(values[0]), values[2], values[1]);
    }

    public void modifyFilm(String params)
    {
        String allValues = params.substring(13, params.length());
        String[] values = allValues.split(" ");
        serverFiles.ModifyFilmeAttribute(parseInt(values[0]), values[2], values[1]);
    }

    public void listActorsWhere(String params)
    {
        String allValues = params.substring(18, params.length());
        String[] values = allValues.split(" == ");
        ArrayList<Ator> atores = serverFiles.GetAtoresByAttribute(values[1], values[0]);
        if(atores != null && atores.size() > 0) {
            String response = "Atores:%";
            for (int i = 0; i < atores.size(); i++) {
                Ator atorTemp = (Ator) atores.get(i);
                response += atorTemp.ID + ", " + atorTemp.name + ", " + atorTemp.idade + "," + atorTemp.biografia + ".%";
            }
            pwrite.println(response);
        } else {
            pwrite.println("Nenhum ator encontrado!");
        }
    }
    
    public void listFilmsWhere(String params)
    {
        String allValues = params.substring(18, params.length());
        String[] values = allValues.split(" == ");
        ArrayList<Filme> filmes = serverFiles.GetFilmesByAttribute(values[1], values[0]);
        if(filmes != null && filmes.size() > 0 ) {
            String response = "Filmes:%";
            for (int i = 0; i < filmes.size(); i++) {
                Filme filmTemp = (Filme) filmes.get(i);
                response +=  filmTemp.name + ", " + filmTemp.duration + ", " + filmTemp.type + ".%";
                for(int j = 0; j < filmTemp.actorsID.size(); j++) {
                    response += filmTemp.actorsID.get(j).toString();
                    if(j == filmTemp.actorsID.size() - 1){
                        response += ".%";
                    } else {
                        response += ", ";
                    }
                }
            }
            pwrite.println(response);
        } else {
            pwrite.println("Nenhum filme encontrado!");
        }
    }
}
