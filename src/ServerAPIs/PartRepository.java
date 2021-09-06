package ServerAPIs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface PartRepository extends Remote
{
    String partRepositoryPrintTest() throws RemoteException;

    String getServerName() throws RemoteException;

    String getRepositoryName() throws RemoteException;

    Part getPiece(int id) throws RemoteException;

    String[][] getRepoMatrix() throws RemoteException;

    ArrayList<Part> getRepository() throws RemoteException;

    void addPartToRepository(String partName, String partDescription) throws RemoteException;      // Primitiva ou agregada

    void removePartFromRepository(int id) throws RemoteException;

    int getNumberOfParts() throws RemoteException;

    String getPartName(int id) throws RemoteException;

    String getPartDescription(int id) throws  RemoteException;

    ArrayList<Part> getSubpartList(int id) throws RemoteException;

    String[][] getSubpartMatrix(int id) throws RemoteException;

    void addToSubpartList(int idToReceive, int idToAdd, String partName, String partDescription, int quantity) throws RemoteException;

    void removeFromSubpartList(String nameToRemove, int idPart, int quantity) throws RemoteException;

    void inicializePartRepositoryList() throws RemoteException;

    void showListOfPieces() throws RemoteException;

    void showListOfSubparts(int id) throws RemoteException;

    int verifyIfIDAlreadyExist(int id) throws RemoteException;
}
