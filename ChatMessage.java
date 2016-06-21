import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * @author Nirmal
 * 
 * This is a ChatMessage and is used both as control message as well
 * as data message. IT could also be used on the client side for client related actions
 *
 */
public class ChatMessage {
	
	public static final String CONNECT = "CONNECT";
	public static final String DISCONNECT = "DISCONNECT";
	public static final String ADD_CONTACT = "ADD_CONTACT";
	public static final String SEND = "SEND";
	public static final String PRESENCE_STATUS_MESSAGE = "PRESENCE_STATUS_MESSAGE";
	public static final String REMOVE_CONTACT = "REMOVE_CONTACT";
	
	

	String message = null;
	String portions[] = null;
	
	
	ByteBuffer byteBuffer = ByteBuffer.allocate(1100);
	
	
	public void clearMessage() {
		message = null;
		portions = null;
	}
	public void setMessage(String message) {
		if (message != null) {
			this.message = message;
		}
		
	}
	
	public String getMessage() {
		return this.message.toString();
	}
	
	public ByteBuffer getBuffer() {
		return this.byteBuffer;
				
	}
	
	/**
	 * Clear the ByteBuffer after the message is fully read
	 * @return
	 */
	public boolean isMessageReadFully() {
		
		CharBuffer charBuffer = byteBuffer.asCharBuffer();
		int limit = charBuffer.limit();
		String protocolMessage = null;
		//Check if the last char is ~ indicating the end of the message which also 
		//indicates that the message is fully read
		if (charBuffer.get(limit) == '~') {
			protocolMessage = charBuffer.toString();
			System.out.println("Protocol Message: " + protocolMessage);
			//clear the buffer
			byteBuffer.clear();
			//Decode the message into portions
			decode(protocolMessage);
			return true;
		} else
			return false;
		
		
	}
	
	/**
	 * @param message
	 */
	private void decode(String message) {
		portions = message.split("|");
		
		if (portions.length == 3) {
			System.out.println("This is a control message");
		} else
		if (portions.length == 4) {
			System.out.println("This is an actual chat message");
			message = portions[3];
		}
	}
	
	public String getUserName() {
		if (portions!= null && portions.length >=2) {
			return portions[1];
		} else {
			return null;
		}
	}
	
	public String getDestinationUserName() {
		if (portions!= null && portions.length >=2) {
			return portions[2];
		} else {
			return null;
		}
	}
	
	public String getPresenceStatusMessage() {
		if (portions!= null && portions.length ==4) {
			return portions[2];
		} else {
			return null;
		}
	}
	
	public String getMessageContent() {
		if (portions!= null && portions.length == 5) {
			return portions[4];
		} else {
			return null;
		}
	}
	
	public boolean isConnectMessage() {

			if (portions!= null && portions[0].equals(CONNECT)) {
				return true;
			} else
				return false;
	}
	
	public boolean isDisConnectMessage() {

		if (portions!= null && portions[0].equals(DISCONNECT)) {
			return true;
		} else
			return false;
}
	
	public boolean isAddContact() {

		if (portions!= null && portions[0].equals(ADD_CONTACT)) {
			return true;
		} else
			return false;
	}
	
	public boolean isUpdatePresenceStatusMessage() {

		if (portions!= null && portions[0].equals(PRESENCE_STATUS_MESSAGE)) {
			return true;
		} else
			return false;
	}
	
	public boolean isRemoveContact() {

		if (portions!= null && portions[0].equals(REMOVE_CONTACT)) {
			return true;
		} else
			return false;
	}
	
	public boolean isSendMessage() {

		if (portions!= null && portions[0].equals(SEND)) {
			return true;
		} else
			return false;
	}
	
}
