import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.SocketException;

public class ClientDatagramSocket extends DatagramSocket {
	static final int MAX_LEN = 1024;

	ClientDatagramSocket() throws SocketException {
		super();
	}

	ClientDatagramSocket(int portNo) throws SocketException {
		super(portNo);
	}
	//@prams 分别是要请求的服务器地址，服务器端口，要发送的请求资源符
	public void sendRequest(InetAddress receiverHost, int serverPort, String uri) throws IOException {
		byte[] sendBuffer = uri.getBytes();
		DatagramPacket datagram = new DatagramPacket(sendBuffer, sendBuffer.length, receiverHost, serverPort);
		this.send(datagram);
		//program stops after executing this line and don't have any error 
	} // end sendMessage

	public void receiveFile(String hostName, String portNum, String fileName) {
		try {
			byte[] buffer = new byte[MAX_LEN];
			String savedPath = "E:\\saved\\" + fileName;
			//作为接收数据包的缓冲区
			DatagramPacket filePacket = new DatagramPacket(buffer, MAX_LEN);
			FileOutputStream outputFile = new FileOutputStream(new File(savedPath));
			while(true) {
				//ClientSocket正在监听本地的端口，receive方法会阻塞，等待数据
				this.receive(filePacket);
				byte[] data = filePacket.getData();
				outputFile.write(buffer, 0, filePacket.getLength());
				//缓冲区的大小比实际接收的的数据要小，说明接受结束
				if(data.length>filePacket.getLength()) break;
			}
			outputFile.close();
			System.out.println("finished receiving file and store at E:\\saved");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}