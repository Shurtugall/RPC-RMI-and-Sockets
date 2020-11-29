/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide;

import Structures.Ator;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author denes
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
            default: pwrite.println("Funcao nao definida");
        }
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
}
