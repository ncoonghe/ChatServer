import java.util.HashSet;
import java.util.Set;

/**
 * @author Nirmal
 *
 */
public class ConnectCommand implements Command {
	
	private ChatUser chatUser = null;
	private ChatWorker chatWorker = null;
	
	public ConnectCommand(ChatUser chatUser, ChatWorker chatWorker) {
		this.chatUser = chatUser;
		this.chatWorker = chatWorker;
	
	}
	public boolean execute() {
		//Check is user name exists and and password match
		//TODO
		
		String firstName ="";
		String lastName = "";
		String userName="";
		
		chatUser.setFirstName(firstName);
		chatUser.setLastName(lastName);
		chatUser.setUserName(userName);
		chatUser.setStatus(new ChatStatus(Status.ONLINE, "Iam Available"));
		
		chatWorker.setUserToChatUser(chatUser.getUserName(), chatUser);
		loadContacts(userName);
		for (ChatUser user : chatUser.getOnlineContacts()) {
			user.addObserver(chatUser);
		}
		chatUser.setChanged();
		chatUser.notifyObservers(chatUser.getStatus());
		
		
		return true;
		
	}
	
	private void loadContacts(String userName) {
		ChatUser chatUser = null;
		Set<String> contacts = loadContactUsers(userName);
		
		for (String user : contacts) {
			chatUser = chatWorker.getChatUser(user);
			if (chatUser != null) {
				//Add all the online users/contacts into the list of contacts
				this.chatUser.addOnlineContact(chatUser);
				
			}		
			this.chatUser.addContactUserNames(user);
		}
		
		
		
	}
	
	private Set<String> loadContactUsers(String userName) {
		Set<String> contactUserNames = new HashSet<String>();
		
		return contactUserNames;
	}
}
