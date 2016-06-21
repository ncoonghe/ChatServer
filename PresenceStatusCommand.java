import java.nio.channels.Channel;

/**
 * @author Nirmal
 *
 */
public class PresenceStatusCommand implements Command {
	
	private ChatUser chatUser = null;
	private ChatWorker chatWorker = null;
	
	/**
	 * @param chatUser
	 * @param chatWorker
	 */
	public PresenceStatusCommand(ChatUser chatUser, ChatWorker chatWorker) {
		this.chatUser = chatUser;
		this.chatWorker = chatWorker;
	
	}
	/* (non-Javadoc)
	 * @see Command#execute()
	 */
	public boolean execute() {
		
		//Remove the chat user for the ChatWorker and close the socket and update status to OFFLINE
		// and inform the Observer to update the presence status for this user
		// at all the online contacts end
		chatUser = chatWorker.removeChatUser(chatUser.getUserName());
		Channel channel = chatUser.getSocketChannel();
		
		if (channel != null && channel.isOpen()) {
			try {
				ChatStatus status = chatUser.getStatus();
				ChatMessage chatMessage = chatUser.getChatMessage();
				
				status.setStatusMessage(chatMessage.getPresenceStatusMessage());				
				//invoke the observer
				chatUser.setChanged();
				chatUser.notifyObservers(chatUser.getStatus());
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
				
		
		return true;
		
	}
}