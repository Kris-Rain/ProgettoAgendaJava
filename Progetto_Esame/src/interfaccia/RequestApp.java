package interfaccia;

public class RequestApp {
	private String request;
	private String errMessage;
	
	public RequestApp(String request, String errMessage) {
		this.request = request;
		this.errMessage = errMessage;
	}

	public String getRequest() {
		return request;
	}

	public String getErrMessage() {
		return errMessage;
	}
}
