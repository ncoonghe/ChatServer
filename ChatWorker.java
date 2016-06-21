import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Nirmal
 * 
 * ChatWorker is responsible to monitor the events arriving from various channel
 * and helps with reading the data from the channel using the MessageReader
 *
 */
public class ChatWorker implements Runnable {
	
	private Map<String, ChatUser> userNameToChatUser = new HashMap<String, ChatUser>();
	private Selector selector = null;
	private ThreadPoolExecutor executor = null;
	private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	
	//private Map<SocketChannel, Reader> socketToReader = new HashMap<SocketChannel, Reader>();
	
	/**
	 * @param userToSocket
	 */
	public ChatWorker(Selector selector, int initPoolCount, int maxPoolCount){
		this.selector = selector;
		this.executor = new ThreadPoolExecutor(initPoolCount, maxPoolCount, 10000L, TimeUnit.MILLISECONDS, queue);
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Reader messageReader = null;
		while (true) {
		try {
			int channelReadyCount = selector.select();
			System.out.println("Ready Channel Count :" + channelReadyCount);
		} catch (IOException e) {
			e.printStackTrace();
			continue;
		}
		Set<SelectionKey> selectedKeys = selector.selectedKeys();

		Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

		while(keyIterator.hasNext()) {
		    
		    SelectionKey key = keyIterator.next();

		    if(key.isAcceptable()) {
		        // not applicable

		    } else if (key.isConnectable()) {
		    	// not applicable
		    } else if (key.isReadable()) {
		        // a channel is ready for reading
		    	//SocketChannel channel = (SocketChannel)key.channel();
		    	
		    	//The MessageReader object has the SocketChannel initialized to it
		    	// when the connection was accepted by the CatServer
		    	messageReader = (MessageReader)key.attachment();
		    	
		    //	socketToReader.put(channel, messageReader);
		    	messageReader.read();
		    	if(messageReader.isReadFully()) {
		    		executor.execute(new ChatTask(this, (MessageReader)messageReader));
		    		
		    	}

		    } else if (key.isWritable()) {
		        // a channel is ready for writing
		    }

		    keyIterator.remove();
		}
		}
		
	}
	
	/**
	 * @param userName
	 * @param socketChannel
	 */
	public void setUserToChatUser(String userName, ChatUser chatUser) {
		
		if (userNameToChatUser != null) {
			userNameToChatUser.put(userName, chatUser);
		}
	}
	
	
	public ChatUser getChatUser(String userName) {
		return userNameToChatUser.get(userName);
	}
	
	public ChatUser removeChatUser(String userName) {
		return userNameToChatUser.remove(userName);
	}

}
