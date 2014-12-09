package kuaipan;


public class OauthSession extends Session {
	public OauthSession(String key, String secret) {
		super(key, secret, Root.APP_FOLDER.toString());
	}
	
	public OauthSession(String key, String secret, String root) {
		super(key, secret, root);
	}
}
