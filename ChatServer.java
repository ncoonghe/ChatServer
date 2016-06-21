import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author Nirmal
 * 
 * This is the Chat Server which accepts client connections and delegates it to 
 * be handled by the ChatWorker thread
 * 
 * ChatServer uses java NIO to handle connections and connection activities based on events. The
 * ChatServer is used to only accept connections.
 * ChatWorker thread also uses java NIO to handle client connections and other connection activities
 * based on events. The Chat Server uses the ServerSocketChannel to accept connections
 * and after the connection is accepted the SocketChannel is used to interact with the 
 * chat client or ChatUser. The SocketChannel used to communicate with the client registers
 * for the READ event. The ChatServer spawns a thread ChatWorker to handle all client communications
 * 
 * Also assumptions are made for some data store to store the user account.
 * The account already exists and all data store related queries are assumed
 * to be there and are not coded.
 */
public class ChatServer implements Runnable {
	
	private  Selector selector = null;
	
	//ChatWorker a Runnable
	private  ChatWorker chatWorker = null;
	private int port = 9999;
	
	//This is the channel that accepts connections from the chat user /client
	private ServerSocketChannel serverSocketChannel = null;
	
	//This is the ChatWorker Thread
	private Thread workerThread = null;
	
	public ChatServer() {

		System.out.println("Initializing with default port " + this.port);
	}
	
	/**
	 * @param port
	 */
	public ChatServer(String port) {
		
		if (port != null) {
			System.out.println("Initializing with port " + port);
			try {
				this.port = Integer.parseInt(port);
			} catch (Exception e){
				e.printStackTrace();
				System.out.println("Initializing with default port " + this.port);
			}
		} else {
			System.out.println("Initializing with default port " + this.port);
		}
		
	}
	
	public void run() {
		startServer();
	}
	public void startServer() {
		System.out.println("Starting server with port " + this.port);

		try {
			
			serverSocketChannel = ServerSocketChannel.open();

			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			  //serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			//Accept Chat Client Connections 	
			selector = SelectorProvider.provider().openSelector();
			while(true){
			    SocketChannel socketChannel =
			            serverSocketChannel.accept();
			    socketChannel.configureBlocking(false);
			    
			    socketChannel.register(selector, SelectionKey.OP_READ, new MessageReader(socketChannel));
			    
			   if (chatWorker == null) {
			    	System.out.println("Initializing Chat Worker Thread");
			    	chatWorker = new ChatWorker(selector, 20, 40);
			    	workerThread = new Thread(chatWorker);
			    	workerThread.setDaemon(true);
			    	workerThread.start();
			    	System.out.println("Started Chat Worker Thread");
			    }
			    
			    
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	public static void main(String args[]) {
		ChatServer chatServer = null;
		
		if (args.length == 2) {
			String port = args[1];
			chatServer = new ChatServer(port);
		
		} else {
			chatServer = new ChatServer();
		}
		
		chatServer.startServer();
		
		//TODO Admin Loop by making the ChatServer running in its own thread instead of the main thread
	}
}
