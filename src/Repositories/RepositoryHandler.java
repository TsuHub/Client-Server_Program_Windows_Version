package Repositories;

import ServerAPIs.Part;
import ServerAPIs.PartConcrete;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A estrutura do arquivo é: id;nome;descrição
 */

public class RepositoryHandler
{
    public static void initializeRepositoryTxt(String fileRepoName, ArrayList<Part> partArrayList)
    {
        int id;
        String nome;
        String description;

        String path = "Repositories/" + fileRepoName + ".txt";

        try
        {
            File objTxt = new File(path);
            Scanner reader = new Scanner(objTxt);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                Scanner lineScan = new Scanner(line).useDelimiter(";");

                id = lineScan.nextInt();
                nome = lineScan.next();
                description = lineScan.next();

                partArrayList.add(new PartConcrete(id, nome, description));
                //System.out.printf("ID: %d    Nome: %s    Descrição: %s\n", id, nome, description);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getTxtContents(String fileName)
    {
        String contents = "";
        String line = "";

        String path = fileName;
        File objTxt = new File(path);

        try
        {
            Scanner reader = new Scanner(objTxt);
            if (reader.hasNextLine()) contents = reader.nextLine();

            while (reader.hasNextLine()) {
                line = reader.nextLine();
                contents = contents + "\n" + line;
            }
            System.out.println(contents);

            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return contents;
    }

    public static void addPartToRepoTxt(String fileRepoName, Part part) throws RemoteException
    {
        String path = "Repositories/" + fileRepoName + ".txt";
        String cont = getTxtContents(path);
        String registry = part.getPartID() + ";" + part.getPartName() + ";" + part.getPartDescriptions();

        try
        {
            FileWriter objTxt = new FileWriter(path);
            PrintWriter pw = new PrintWriter(objTxt);

            if (cont.equals("")) pw.print(registry);
            else {
                cont = cont + "\n" + registry;
                pw.print(cont);
            }
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removePartRepoTxt(String fileRepoName, int idToRemove)
    {
        String path = "Repositories/" + fileRepoName + ".txt";
        String contents = "";
        int id;

        try
        {
            File objTxt = new File(path);
            Scanner reader = new Scanner(objTxt);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                Scanner lineScan = new Scanner(line).useDelimiter(";");

                id = lineScan.nextInt();

                if (!(id == idToRemove)) {
                    if (contents.equals("")) contents = line;
                    else contents = contents + "\n" + line;
                }
            }
            reader.close();

            File newObjTxt = new File(path);
            PrintWriter pw = new PrintWriter(newObjTxt);
            pw.append(contents);

            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
