package ServerAPIs;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * RMI - Invocação de Métodos Remotos
 *
 * O RMI possibilita um programa ser executado remotamente, isto é, um
 * programa ser executado em um sistema distribuído. Onde um programa cliente
 * solicita um serviço, por meio de um método e o servidor que implementa
 * este método retorna o resultado solicitado ao cliente.
 *
 * E para que o servidor saiba quais serviços serão disponibilizados, uma
 * interface se faz necessária, indicando os métodos a serem implementados
 * pela mesma.
 */

public interface Part extends Remote
{
    String partPrintTest() throws RemoteException;

    int getPartID() throws RemoteException;

    String getPartName() throws RemoteException;

    String getPartDescriptions() throws RemoteException;

    ArrayList<Part> getSubpartsList() throws RemoteException;

    int getSubpartListSize() throws RemoteException;

    void addPartToSubPartList(int partID, String partName, String partDescription, int quantity) throws RemoteException;

    void removePartFromRepository(int id) throws RemoteException;

    void showListOfSubparts() throws RemoteException;

    int verifyIfNameExist(String name) throws RemoteException;
}
