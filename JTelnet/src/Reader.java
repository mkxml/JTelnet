import java.io.InputStream;
import java.net.Socket;

public class Reader implements Runnable {
  public boolean stop = false;
  public Socket socket;
  public InputStream is;

  public Reader(Socket socket) {
    this.socket = socket;
    try {
      is = socket.getInputStream();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void run() {
    try {
      // Read loop
      while (!stop && socket.isConnected()) {
        int data = is.read();
        if (data > -1) {
          char character = (char) data;
          System.out.print(character);
        } else {
          JTelnet.foreignClosed = true;
          System.out.println("Connection closed by foreign host.");
          System.exit(0);
          break;
        }
      }
    } catch (Exception e) {
    }
  }
}
