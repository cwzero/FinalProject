package edu.nwtc.chat.message;

public class LoginResponse extends Message {
	protected static final LoginResponse SUCCESS = new LoginResponse("SUCCESS");
	protected static final LoginResponse FAILURE = new LoginResponse("FAILURE");

	protected String status;

	public LoginResponse() {
		
	}

	public LoginResponse(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String getType() {
		return "RESPONSE";
	}
	
	public static LoginResponse success() {
		return SUCCESS;
	}
	
	public static LoginResponse failure() {
		return FAILURE;
	}
}
