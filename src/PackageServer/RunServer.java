package PackageServer;

import Repositories.RepositoryHandler;
import ServerAPIs.PartRepository;
import ServerAPIs.PartRepositoryConcrete;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * O método principal desta classe (Server.Server.java) precisa necessariamente:
 * 1. Criar e exportar um objeto remoto;
 * 2. Registrar o objeto remoto com o RMI Java;
 *
 * Caso o servidor não consiga atender a uma solicitação do cliente, seja pela
 * queda da conexão, por um desligamento inesperado da máquina servidor, uma
 * exceção objeto do tipo RemoteException é lançada, verificada e tratada.
 */

public class RunServer
{
    /**
     * Registrando o objRP no RMI Registry para que esta possa ser
     * obtida pelos clientes. Este registro é feito vinculando o
     * registry criado com o objRP.
     *
     * @param objServer : Para o estabelecimento da conexão do servidor é necessário entrar com o seu repositório.
     * @param port      : Definindo a porta em que será estabelecida a conexão.
     */
    public static void establishConnection(Server objServer, int port)
    {
        try
        {
            PartRepository objRP = (PartRepository) UnicastRemoteObject.exportObject(objServer.getRepository(), port);   // Servidor: server, Porta : 0
            Registry registry = LocateRegistry.createRegistry(port);
            registry.bind("ServerAPIs.PartRepository", objRP);  // Vinculando o objeto da interface com o registro para que o cliente possa localizá-lo.
            System.out.printf("%s pronto - Porta : %d\n", objServer.getServerName(), port);

        } catch (RemoteException | AlreadyBoundException e) {       // Lançando erros de exportObject - (UnicastRemoteObject).
            e.printStackTrace();                                    // Lançando erros para bind  - (registry.bind).
        }
    }

    public static void main(String args[]) throws IOException
    {
        int port1 = 2001;
        int port2 = 2002;
        int port3 = 2003;
        int port4 = 2004;

        System.out.println("\n");

        PartRepositoryConcrete repository1 = new PartRepositoryConcrete("Servidor1", "Repo S1");
        Server objServer1 = new Server(1, "Servidor 1", repository1);
        establishConnection(objServer1, port1);
        objServer1.getRepository().inicializePartRepositoryList();

        PartRepositoryConcrete repository2 = new PartRepositoryConcrete("Servidor2", "Repo S2");
        Server objServer2 = new Server(2, "Servidor 2", repository2);
        establishConnection(objServer2,port2);
        objServer2.getRepository().inicializePartRepositoryList();

        PartRepositoryConcrete repository3 = new PartRepositoryConcrete("Servidor3", "Repo S3");
        Server objServer3 = new Server(3, "Servidor 3", repository3);
        establishConnection(objServer3, port3);
        objServer3.getRepository().inicializePartRepositoryList();

        PartRepositoryConcrete repository4 = new PartRepositoryConcrete("Servidor4", "Repo S4");
        Server objServer4 = new Server(4, "Servidor 4", repository4);
        establishConnection(objServer4, port4);
        objServer4.getRepository().inicializePartRepositoryList();

        //==============================================================================================================
        // Bloco de teste
        /*
        System.out.println("\n");

        System.out.println("TESTE\n");
        objServer1.getRepository().showListOfPieces();
        System.out.println();

        String aux[][] = objServer1.getRepository().getRepoMatrix();
        System.out.println(aux[0][0]);
        //*/
        //==============================================================================================================
    }
}