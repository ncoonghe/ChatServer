import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @author Nirmal
 * This is a MEssageReader associated with every Channel.
 * Used to read the complete ChatMessage and decode it into
 * appropriate commands.
 *
 */
public class MessageReader implements Reader {
	
	private SocketChannel channel = null;
	private ChatMessage message = new ChatMessage();
	private boolean readFully = false;
	
	MessageReader(SocketChannel channel) {
		this.channel = channel;
	}
	
	
	public void resetReadStatus() {
		this.readFully = false;
		message.clearMessage();
	}
	/* (non-Javadoc)
	 * @see Reader#isReadFully()
	 */
	public boolean isReadFully() {
		return readFully;
	}
	/* (non-Javadoc)
	 * @see Reader#read()
	 */
	public void read() {
		
		if (channel != null && channel.isOpen()) {
			try {
				int size = channel.read(message.getBuffer());
				if (size != 0 && message.isMessageReadFully()) {
					readFully = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				readFully = false;
			}
		}
		
	}
	
	public ChatMessage getChatMessage() {
		return this.message;
	}


	public SocketChannel getChannel() {
		return channel;
	}


	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}
}
