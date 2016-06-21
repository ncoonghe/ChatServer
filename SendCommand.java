import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Nirmal
 *
 */
public class SendCommand implements Command {
	
	private ChatUser chatUser = null;
	private ChatWorker chatWorker = null;
	
	/**
	 * @param chatUser
	 * @param chatWorker
	 */
	public SendCommand(ChatUser chatUser, ChatWorker chatWorker) {
		this.chatUser = chatUser;
		this.chatWorker = chatWorker;
	
	}
	/* (non-Javadoc)
	 * @see Command#execute()
	 */
	public boolean execute() {
		
		chatUser = chatWorker.getChatUser(chatUser.getUserName());
		ChatMessage message = chatUser.getChatMessage();
		
		//get the destination chat username
		String destinationUser = message.getDestinationUserName();
		
		//get the destination chat user
		ChatUser destChatUser = chatWorker.getChatUser(destinationUser);
		
		//get the channel associated with the destination chatuser
		SocketChannel destChannel = destChatUser.getSocketChannel();
		
		try {
		//write out the message content portion of the chat message to the destination channel
		if (destChannel != null && destChannel.isOpen()) {
			ByteBuffer byteBuffer = ByteBuffer.wrap(message.getMessageContent().getBytes());
			destChannel.write(byteBuffer);
			
		}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
				
		return true;
		
	}
}