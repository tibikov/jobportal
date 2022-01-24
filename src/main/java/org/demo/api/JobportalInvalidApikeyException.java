package org.demo.api;

public class JobportalInvalidApikeyException extends RuntimeException {

	private String apiKey;

	private static final long serialVersionUID = 1L;

	public JobportalInvalidApikeyException(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiKey() {
		return apiKey;
	}

	@Override
	public String getMessage() {
		return "Required request header 'api-key' has invalid value: '" + apiKey + "'";
	}

	@Override
	public String toString() {
		return this.getMessage();
	}
}
