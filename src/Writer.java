import java.io.OutputStream;
import java.net.Socket;

public class Writer {
  public Socket socket;
  public OutputStream os;

  public Writer(Socket socket) {
    this.socket = socket;
    try {
      os = socket.getOutputStream();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void write(String str) {
    byte[] bytes = str.getBytes();
    int len = bytes.length;
    try {
      os.write(bytes, 0, len);
      os.flush();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
