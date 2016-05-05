package edu.nwtc.chat.message;

public class PrivateMessage extends ChatMessage {
	protected String recipient;

	public PrivateMessage() {
		super();
	}

	public PrivateMessage(String sender, String time, String message, String recipient) {
		super(sender, time, message);
		this.recipient = recipient;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	@Override
	public String getType() {
		return "PRIVATE";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((recipient == null) ? 0 : recipient.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivateMessage other = (PrivateMessage) obj;
		if (recipient == null) {
			if (other.recipient != null)
				return false;
		} else if (!recipient.equals(other.recipient))
			return false;
		return true;
	}
}
