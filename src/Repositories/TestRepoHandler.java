package Repositories;

public class TestRepoHandler
{
    public static void main(String[] args)
    {
        String aux = RepositoryHandler.getTxtContents("Repositories/Repo S1.txt");
        System.out.println(aux);
    }
}
