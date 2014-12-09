package kuaipan;

import kuaipan.*;

public abstract class Session {
	
	public ConsumerToken consumer;
	public RequestToken requestToken;
	public AccessToken accessToken;
	public final String root;
	
	public Session(String key, String secret, String root) {
		this.consumer = new ConsumerToken(key, secret);
		this.root = root.toString();
	}
	
	public void setAuthToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}
	
	public void setTempToken(RequestToken requestToken) {
		this.requestToken = requestToken;
	}
	
	public boolean isAuth() {
		if (this.accessToken == null)
			return false;

		return (this.accessToken instanceof AccessToken);
	}
	
	public void unAuth() {
		this.requestToken = null;
		this.accessToken = null;
	}
	
	public void assertAuth() throws KuaipanAuthExpiredException {
		if (! isAuth())
			throw new KuaipanAuthExpiredException();
	}
	
	public static enum Root {
		KUAIPAN, APP_FOLDER;
		public String toString() {
			return this.name().toLowerCase();
		}
	}
}
