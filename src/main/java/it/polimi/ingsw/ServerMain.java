
package it.polimi.ingsw;

import it.polimi.ingsw.server.lobbyUtilities.LobbyManager;
import it.polimi.ingsw.server.ServerSocketManager;
import it.polimi.ingsw.server.fileUtilities.FileManager;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.*;

/**
 * Class that takes care of starting the server
 */
public class ServerMain
{
    public static void main( String[] args )
    {
        FileManager serverFileManager= new FileManager();
        ExecutorService ServerExit = Executors.newFixedThreadPool(1);

        System.out.print(INITIALIZE_SCREEN);
        System.out.print(CLEAN);
        System.out.println(consoleSeparator);
        //Start Server Testing - Essential Files
        serverFileManager.testFileReading();
        serverFileManager.readServerSettings();
        ServerSocketManager serverSocketManager = new ServerSocketManager(serverFileManager.getServerPort(), new LobbyManager());

        ServerExit.execute(() -> {
            Scanner inConsole = new Scanner(System.in);
            do{
                System.out.println(ansiRED + "To close Server, write EXIT, at any moment"+ ansiRESET+nextLine);
            }while(!inConsole.nextLine().equalsIgnoreCase("EXIT"));
            System.exit(0);
        });

        System.out.println(consoleSeparator);

        try {
            serverSocketManager.startServerSocket();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}