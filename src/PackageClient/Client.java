package PackageClient;

import ServerAPIs.PartRepository;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client
{
    private  Client(){
        // ...
    }

    /**
     *     Ao executar o cliente no terminal (java PackageClient.Client localhost), caso o
     *     usuário não dê nenhuma entrada, o retorno será nulo, caso contrário,
     *     o retorno é o nome, no caso 'localhost'.
     *     No caso, para este código fonte, não é necessário o localhost, já
     *     que a porta foi definida para 2001 hardcoded.
     *
     *     (192.168.0.20:2001)
     *
     * @param args : Argumento de entrada no próprio terminal (localhost)
     */
    public static void main(String args[])
    {
        String host = (args.length < 1) ? null : args[0];

        try {
            // Obtendo uma referência para o registro do RMI
            Registry registry = LocateRegistry.getRegistry(2001);

            // Obtendo o objeto de APIgeneralOperations do Servidor
            PartRepository objRP = (PartRepository) registry.lookup("ServerAPIs.PartRepository");

            // Chama o método do servidor e imprime a mensagem
            String response = objRP.partRepositoryPrintTest();
            System.out.println(response);
        } catch (NotBoundException | RemoteException e) {       // Para getRegistry (Registry registry = LocateRegistry.getRegistry(host);)
            e.printStackTrace();                                // Para APIgeneralOperations objAPI = (APIgeneralOperations) registry.lookup
        }                                                       // Para String msg = objAPI.apiPrintFunction()
    }
}
