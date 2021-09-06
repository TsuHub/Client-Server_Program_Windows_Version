package ServerAPIs;

import java.io.IOException;

/**
 * Optei por criar um ArrayList para o armazenamento dos diferentes processos
 * de servidores porque a listagem (1) vai ser mais rápida do que usando uma
 * LinkedList. E como não deve existir tantas inserções (adição de novos
 * servidores), imagino que nossa estratégia aqui deva ser nos preocuparmos
 * com a varredura desta lista. Já para as peças acho q deva ser o contrário,
 * acho que como podem existir muitas inserções e/ou remoções, o ideal será
 * criar uma LinkedList de peças (ServerAPIs.PartRepository).
 *
 * <p> (1) Então por exemplo, devemos percorrer o arraylist para imprimir os
 *     servidores ativos.
 */

public class ServerProcessBuilder
{
    public static void initServerProcess()
    {
        ProcessBuilder pBRegistry = new ProcessBuilder();
        pBRegistry.command("cmd.exe", "start rmiregistry");

        ProcessBuilder pBServer = new ProcessBuilder();
        pBServer.command("cmd.exe", "start java PackageServer/RunServer");

        try {
            pBRegistry.start();
            pBServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
