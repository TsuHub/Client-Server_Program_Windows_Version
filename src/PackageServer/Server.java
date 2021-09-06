package PackageServer;

import ServerAPIs.PartRepositoryConcrete;

public class Server
{
    private int serverID;
    private String serverName;
    private PartRepositoryConcrete repository;

    public Server(int id, String name, PartRepositoryConcrete repository) {
        this.serverID = id;
        this.serverName = name;
        this.repository = repository;
    }

    public int getServerID(){
        return this.serverID;
    }

    public String getServerName(){
        return this.serverName;
    }

    public PartRepositoryConcrete getRepository(){
        return this.repository;
    }
}
