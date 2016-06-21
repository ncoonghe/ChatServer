
public class ChatStatus {
	
	
	
	private String statusMessage;
	private Status status; 
	
	public ChatStatus(Status status, String message) {
		this.status = status;
		this.statusMessage = message;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	
	
}
