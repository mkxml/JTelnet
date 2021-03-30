import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.Socket;

public class JTelnet {
  public static boolean foreignClosed = false;

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.out.println("Missing arguments");
      System.out.println("Usage: jtelnet host port");
      return;
    }
    String host = args[0];
    int port = Integer.parseInt(args[1]);
    try {
      Socket socket = new Socket(host, port);
      // Start reading data in new thread
      Reader reader = new Reader(socket);
      Thread readerThread = new Thread(reader);
      System.out.println("Connected to " + host + ":" + port);
      System.out.println("Press Ctrl+C to stop");
      readerThread.start();
      Writer writer = new Writer(socket);
      BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
      // Stop handler
      Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
          try {
            reader.stop = true;
            if (socket != null) {
              socket.close();
            }
            if (!foreignClosed) {
              System.out.println("Connection closed by user.");
            }
          } catch (Exception e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
          }
        }
      });
      // Program write loop until termination via Ctrl+C or remote closing
      while (socket.isConnected()) {
        String str = bufferReader.readLine();
        str = str + '\n';
        writer.write(str);
      }
    } catch (ConnectException e) {
      System.out.println("Connection refused for " + host + ":" + port);
    }
  }
}
