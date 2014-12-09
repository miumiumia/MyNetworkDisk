package kuaipan;


public abstract class TokenPair {
	public String oauth_token;
	public String oauth_token_secret;
	
	public TokenPair(){}
	
	public TokenPair(String key, String secret) {
		this.oauth_token = key;
		this.oauth_token_secret = secret;
	}
	
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("key=");
		buf.append(oauth_token);
		buf.append(";");
		buf.append("secret=");
		buf.append(oauth_token_secret);
		return buf.toString();
	}
}
