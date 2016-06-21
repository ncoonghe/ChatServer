import java.nio.channels.Channel;

/**
 * @author Nirmal
 *
 */
public class DisconnectCommand implements Command {
	
	private ChatUser chatUser = null;
	private ChatWorker chatWorker = null;
	
	/**
	 * @param chatUser
	 * @param chatWorker
	 */
	public DisconnectCommand(ChatUser chatUser, ChatWorker chatWorker) {
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
				channel.close();
				chatUser.setSocketChannel(null);
				chatUser.setStatus(new ChatStatus(Status.OFFLINE, ""));
				//invoke the observer
				chatUser.setChanged();
				chatUser.notifyObservers(chatUser.getStatus());
				//Remove this chat user from being observed further by other other 
				//Chat users for presence
				for (ChatUser user : chatUser.getOnlineContacts()) {
					user.deleteObserver(chatUser);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
				
		
		return true;
		
	}
}