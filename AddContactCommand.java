
/**
 * @author Nirmal
 *
 */
public class AddContactCommand implements Command {
	
	private ChatUser chatUser = null;
	private ChatWorker chatWorker = null;
	
	/**
	 * @param chatUser
	 * @param chatWorker
	 */
	public AddContactCommand(ChatUser chatUser, ChatWorker chatWorker) {
		this.chatUser = chatUser;
		this.chatWorker = chatWorker;
	
	}
	/* (non-Javadoc)
	 * @see Command#execute()
	 */
	public boolean execute() {
		
		ChatMessage chatMessage = chatUser.getChatMessage();
		String contactToAdd = chatMessage.getDestinationUserName();
		ChatUser chatUserToAdd = chatWorker.getChatUser(contactToAdd);
		
		if (chatUserToAdd != null) {
			ChatStatus chatStatus = chatUserToAdd.getStatus();

			if (chatStatus.getStatus() == Status.ONLINE ) {
				
				//add to this users contact usernames and onlineContact list
				// and send/update the presence info of the newly added contact user to this user
				this.chatUser.addContactUserNames(chatUserToAdd.getUserName());
				this.chatUser.addOnlineContact(chatUserToAdd);
				this.chatUser.addObserver(chatUserToAdd);
				this.chatUser.update(chatUserToAdd, chatStatus);
				
				//add to the destination users (new added contact) this users contact usernames
				// and onlineContact list and send/update the presence info of this user to new contact user
				chatUserToAdd.addContactUserNames(chatUser.getUserName());
				chatUserToAdd.addOnlineContact(chatUser);
				chatUserToAdd.addObserver(chatUser);
				chatUserToAdd.update(chatUser, chatStatus);
				
			}
		}
		
		return true;
	}	
}
	