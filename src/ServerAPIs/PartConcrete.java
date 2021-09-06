package ServerAPIs;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class PartConcrete implements Part
{
    private int partID;
    private String partName;
    private String partDescription;
    private ArrayList<Part> subPartList;

    public PartConcrete(int id, String name, String description){
        this.partID = id;
        this.partName = name;
        this.partDescription = description;
        this.subPartList = new ArrayList<>();
    }

    @Override
    public String partPrintTest() throws RemoteException {
        System.out.println("Executando o método partPrintTest() de ServerAPIs.Part.java");
        return "Server.Server : implementacão do método da interface ServerAPIs.Part funcionando corretamente.";
    }

    @Override
    public int getPartID() throws RemoteException {
        return this.partID;
    }

    @Override
    public String getPartName() throws RemoteException {
        return this.partName;
    }

    @Override
    public String getPartDescriptions() throws RemoteException {
        return this.partDescription;
    }

    @Override
    public ArrayList<Part> getSubpartsList() throws RemoteException {
        return this.subPartList;
    }

    @Override
    public int getSubpartListSize() throws RemoteException {
        return this.subPartList.size();
    }

    @Override
    public void addPartToSubPartList(int partID, String partName, String partDescription, int quantity) throws RemoteException
    {
        PartConcrete part = new PartConcrete(partID, partName, partDescription);

        for (int i = 0; i < quantity; i++) {
            this.subPartList.add(part);
        }
    }

    @Override
    public void removePartFromRepository(int id) throws RemoteException {
        this.subPartList.remove(id);
        System.out.printf("Peça de ID %d removida a lista de subcomponentes.\n", id);
    }

    @Override
    public void showListOfSubparts() throws RemoteException
    {
        for (Part part : this.subPartList) {
            System.out.println(part.getPartName());
        }
    }

    @Override
    public int verifyIfNameExist(String name) throws RemoteException
    {
        int index = -1;

        for (Part p : this.subPartList) {
            if (p.getPartName().equals(name))
                index = this.subPartList.indexOf(p);
        }
        return index;
    }
}
