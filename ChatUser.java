import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * @author Nirmal
 * 
 * This is a ChatUser which maintains a list of ONLINE contacts and userNames
 * of all the contacts ONLINE or not.
 * 
 * The latter list is sent to the chat client after the ChatUser CONNECT's to the 
 * Chat Server
 * 
 * ChatUser implements Observer pattern. It is an Observable as well as an Observer
 * Any Changes to the Connectivity status or presence status results in the 
 * notification to the appropriate client's/online contacts with the latest
 * presence information.
 * 
 * The ChatUser could be used both as a client representation as well as to represent
 * users at the server
 *
 */
public class ChatUser extends Observable implements Observer {
	

	private String firstName;
	private String lastName;	
	private String password;
	//ChatUser Set on ONLINE Contacts
	private Set<ChatUser> onlineContacts = new HashSet<ChatUser>();
	
	//All Contact irrespective of ONLINE or not. Only userNames are stored here
	private Set<String> allContactUserNames = new HashSet<String>();
	
	//The Channel associated with this ChatUser
	private SocketChannel socketChannel = null;
	
	//TODO - to store last few messages
	//private List<ChatMessage> receivedMessages = new LinkedList<ChatMessage>();
	
	//The ChatMessage this includes both control messages and data messages
	//Control Messages are CONNECT, DISCONNECT, etc
	//Data Messages are SEND
	private ChatMessage message = null;
		
	//Chat Status includes the status ONLINE/OFFLINE as well as a 
	//Status Message (like, I'am Available, Busy, etc)
	private ChatStatus status = null;

	private String userName;
	
	
	/* (non-Javadoc)
	 * @see java.util.Observable#setChanged()
	 */
	public void setChanged() {
		super.setChanged();
	}
	
	
	//Used to update the chat status by the observers as well as relay the presence
	//information to the contacts
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object obj) {
		
		if (obj instanceof ChatStatus) {
			ChatStatus status = (ChatStatus)obj;
			
			switch (status.getStatus()) {
			
			case OFFLINE:
				//send presence info of this user to all the online contacts
				//when the user DISCONNECT's from the chat server
				sendPresenceInfo();
				break;
			case ONLINE:
				//send contact list of this user to chat client when the user becomes
				//ONLINE after a CONNECT to the chat server
				sendContactListToChatClient();
				//Also send presence info of this user to all the online contacts
				sendPresenceInfo();
				break;
			default:
			}
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observable#addObserver(java.util.Observer)
	 */
	public void addObserver(Observer obj) {
		
		if (obj instanceof ChatUser) {
			onlineContacts.add((ChatUser)obj);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observable#deleteObserver(java.util.Observer)
	 */
	public void deleteObserver(Observer obj) {
		if (obj instanceof ChatUser) {
			onlineContacts.remove((ChatUser)obj);
		}
	}
	
	
	public Set<ChatUser> getOnlineContacts() {
		return onlineContacts;
	}
	
	/**
	 * sends the contact userNames to the chat client
	 */
	private void sendContactListToChatClient() {
		//TODO
	}

	
	private void sendPresenceInfo() {
		//TODO
	}
	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	

	public ChatStatus getStatus() {
		return status;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public void setStatus(ChatStatus status) {
		this.status = status;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	/**
	 * The Channel associated with this ChatUser
	 * @param socketChannel
	 */
	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	/**
	 * Adds an online contact
	 * @param chatUser
	 * @return
	 */
	public boolean addOnlineContact(ChatUser chatUser) {
		return onlineContacts.add(chatUser);
	}
	
	/**
	 * @param chatUser
	 * @return
	 */
	public boolean removeOnlineContact(ChatUser chatUser){
		return onlineContacts.remove(chatUser);
	}
	
	/**
	 * Add contact userNames
	 * @param userName
	 * @return
	 */
	public boolean addContactUserNames(String userName) {
		return allContactUserNames.add(userName);
	}
	
	/**
	 * Removes Contact userNames
	 * @param userName
	 * @return
	 */
	public boolean removeContactUserNames(String userName){
		return allContactUserNames.remove(userName);
	}
	
	public ChatMessage getChatMessage() {
		return this.message;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		
		if (obj instanceof ChatUser) {
			ChatUser chatUser = (ChatUser)obj;
			return this.userName.equals(chatUser.getUserName());
		} else
			return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		
		char[] ch = userName.toCharArray();
		int hash = 0;
		for (int i =0 ; i < ch.length; i++) {
			hash = hash + (int)Math.round(ch[i] * Math.random());
		}
		
		return hash;
	}
}
