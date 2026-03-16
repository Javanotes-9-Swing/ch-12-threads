package exercise5;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * This method processes the connection with one client.
 * It creates streams for communicating with the client,
 * reads a command from the client, and carries out that
 * command.  The connection is also logged to standard output.
 * An output beginning with ERROR indicates that a network
 * error occurred.  A line beginning with OK means that
 * there was no network error, but does not imply that the
 * command from the client was a legal command.
 */
public class FileServerWithThreadPool {

    static final int LISTENING_PORT = 3210;

    private static final int THREAD_POOL_SIZE = 5;

    private static final int QUEUE_CAPACITY = 10;

    /**
     * The connectionQueue is used to send connected sockets from the
     * main program to the worker threads.  When a connection request
     * is received, the connected socket is placed into the queue.
     * Worker threads retrieve sockets from the queue as they become
     * available.  This is an ArrayBlockingQueue, with a limited
     * capacity, to prevent the number of clients who are waiting
     * for service in the queue from becoming too large.
     */
    private static ArrayBlockingQueue<Socket> connectionQueue;

    public static void main(String[] args) {

        File directory;        // The directory from which the server
        //    gets the files that it serves.

        ServerSocket listener; // Listens for connection requests.

        Socket connection;     // A socket for communicating with a client.


      /* Check that there is a command-line argument.
         If not, print a usage message and end. */

        if (args.length == 0) {
            System.out.println("Usage:  java FileServer <directory>");
            return;
        }

      /* Get the directory name from the command line, and make
         it into a file object.  Check that the file exists and
         is in fact a directory. */

        directory = new File(args[0]);
        if ( ! directory.exists() ) {
            System.out.println("Specified directory does not exist.");
            return;
        }
        if (! directory.isDirectory() ) {
            System.out.println("The specified file is not a directory.");
            return;
        }

      /* Listen for connection requests from clients.  For
         each connection, call the handleConnection() method
         to process it.  The server runs until the program
         is terminated, for example by a CONTROL-C. */

        try {
            listener = new ServerSocket(LISTENING_PORT);

            connectionQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
            for (int i = 0; i < THREAD_POOL_SIZE; i++) {
                new ConnectionHandler(directory); // Create the thread; it starts itself.
            }

            System.out.println("Listening on port " + LISTENING_PORT);
            while (true) {
                connection = listener.accept();
                try {
                    connectionQueue.put(connection);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Server shut down unexpectedly.");
            System.out.println("Error:  " + e);
            return;
        }

    } // end main()

    /**
     * Defines one of the threads in the thread pool.  Each thread runs
     * in an infinite loop in which it takes a connection from the connection
     * queue and handles communication with that client.  The thread starts
     * itself in its constructor.  The constructor also sets the thread
     * to be a daemon thread.  (A program will end if all remaining
     * threads are daemon threads.)
     */
    private static class ConnectionHandler extends Thread{

        File directory;
        public ConnectionHandler(File directory) {
            this.directory = directory;
            setDaemon(true);
            start();
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                Socket client;
                try {
                    client = connectionQueue.take();
                    handleConnection(directory, client);
                } catch (InterruptedException e) {
                    continue; // If interrupted, just go back to start of while loop.
                }
                String clientAddress = client.getInetAddress().toString();
//                try {
//
//                } catch (Exception e) {
//                    System.out.println("Error on connection with: "
//                            + clientAddress + ": " + e);
//                }
            }
        }
    }

    /**
     * This method processes the connection with one client.
     * It creates streams for communicating with the client,
     * reads a command from the client, and carries out that
     * command.  The connection is also logged to standard output.
     * An output beginning with ERROR indicates that a network
     * error occurred.  A line beginning with OK means that
     * there was no network error, but does not imply that the
     * command from the client was a legal command.
     */
    private static void handleConnection(File directory, Socket connection) {
        Scanner incoming;       // For reading data from the client.
        PrintWriter outgoing;   // For transmitting data to the client.
        String command = "Command not read";
        try {
            incoming = new Scanner( connection.getInputStream() );
            outgoing = new PrintWriter( connection.getOutputStream() );
            command = incoming.nextLine();
            if (command.equalsIgnoreCase("index")) {
                sendIndex(directory, outgoing);
            }
            else if (command.toLowerCase().startsWith("get")){
                String fileName = command.substring(3).trim();
                sendFile(fileName, directory, outgoing);
            }
            else {
                outgoing.println("ERROR unsupported command");
                outgoing.flush();
            }
            System.out.println("OK    " + connection.getInetAddress()
                    + " " + command);
        }
        catch (Exception e) {
            System.out.println("ERROR " + connection.getInetAddress()
                    + " " + command + " " + e);
        }
        finally {
            try {
                connection.close();
            }
            catch (IOException e) {
            }
        }
    }

    /**
     * This is called by the handleConnection() method in response to an "INDEX" command
     * from the client.  Send the list of files in the server's directory.
     */
    private static void sendIndex(File directory, PrintWriter outgoing) throws Exception {
        String[] fileList = directory.list();
        for (int i = 0; i < fileList.length; i++)
            outgoing.println(fileList[i]);
        outgoing.flush();
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Error while transmitting data.");
    }

    /**
     * This is called by the handleConnection() command in response to "GET <fileName>"
     * command from the client.  If the file doesn't exist, send the message "ERROR".
     * Otherwise, send the message "OK" followed by the contents of the file.
     */
    private static void sendFile(String fileName, File directory, PrintWriter outgoing)
            throws Exception {
        File file = new File(directory,fileName);
        if ( (! file.exists()) || file.isDirectory() ) {
            // (Note:  Don't try to send a directory, which
            // shouldn't be there anyway.)
            outgoing.println("ERROR");
        }
        else {
            outgoing.println("OK");
            BufferedReader fileIn = new BufferedReader( new FileReader(file) );
            while (true) {
                // Read and send lines from the file until
                // an end-of-file is encountered.
                String line = fileIn.readLine();
                if (line == null)
                    break;
                outgoing.println(line);
            }
        }
        outgoing.flush();
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Error while transmitting data.");
    }

}
