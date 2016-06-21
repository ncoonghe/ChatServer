/**
 * @author Nirmal
 * 
 * ChatTask is a Runnable which is run using the ThreadPools from an executor
 * based on the user generated events like CONNECT, DISCONNECT, SEND, ADD_CONTACT,etc
 * These tasks are submitted from the ChatWorker thread after an entire chat message is
 * decoded fully. The MessageReader is used to read the ChatMessage from the channel
 * Each client connection (SocketChannel) has its own MessageReader registered, so that 
 * as low level READ events arrive the reader reads from the ByteBuffer and translates
 * it to CharBuffer and checks to see if the message is fully read. If the last character of the message
 * is a ~ then the message is fully read. Once fully read the chat message (control or data)
 * is handed over to the ChatTask runnable by submitting it to a ThreadPoolExecutor to
 * decode the message further and execute the appropriate Command.
 * 
 * As an example a CONNECT message is expected to be in the below format
 * CONNECT|userName|~
 * 
 * Disconnect message
 * DISCONNECT|userName|~
 * 
 * Send actual chat message 
 * SEND|fromUserName|toUserName|messageContent|~
 * 
 * Add Contact chat message
 * ADD_CONTACT|userName|contactTOAddUserName|~
 * 
 * Presence Status message update message (This is I am Available, Busy, etc). The sub status 
 * PRESENCE_STATUS_MESSAGE|userName|presenceStatus|~
 * 
 * Remove Contact
 * REMOVE_CONTACT|userName|contactToRemoveUserName|~
 *
 */
public class ChatTask implements Runnable {
	ChatWorker chatWorker = null;
	MessageReader messageReader = null;
	
	
	/**
	 * @param chatWorker
	 * @param messageReader
	 */
	public ChatTask(ChatWorker chatWorker, MessageReader messageReader) {
		this.chatWorker = chatWorker;
		this.messageReader = messageReader;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		ChatMessage chatMessage = messageReader.getChatMessage();
		
		Command command = null;
		ChatUser chatUser = null;
		
		
		if (chatMessage.isConnectMessage()) {
			chatUser = new ChatUser();
			chatUser.setSocketChannel(messageReader.getChannel());
			
			command = new ConnectCommand(chatUser, chatWorker);
			command.execute();
		} else
		if (chatMessage.isDisConnectMessage()) {
				chatUser =  chatWorker.getChatUser(chatMessage.getUserName());
				if (chatUser != null) {
					command = new DisconnectCommand(chatUser, chatWorker);
					if (command.execute()) {
						chatUser.setSocketChannel(messageReader.getChannel());
					}
				}
		} else
		if (chatMessage.isSendMessage()) {
				chatUser =  chatWorker.getChatUser(chatMessage.getUserName());
				if (chatUser != null) {
					command = new SendCommand(chatUser, chatWorker);
					if (command.execute()) {
						System.out.println("Message sent sucessfully to destination user");
					}
				}
		} else
		if (chatMessage.isUpdatePresenceStatusMessage()) {
				chatUser =  chatWorker.getChatUser(chatMessage.getUserName());
				if (chatUser != null) {
					command = new PresenceStatusCommand(chatUser, chatWorker);
					if (command.execute()) {
						System.out.println("Presence Status Message updated successfully and notified");
					}
				}
		}
		
		messageReader.resetReadStatus();
		
	}

}
