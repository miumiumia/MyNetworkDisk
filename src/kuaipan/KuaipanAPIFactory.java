package kuaipan;

import java.util.HashMap;

import kuaipan.*;

public class KuaipanAPIFactory {

	private KuaipanAPIFactory() {
	}

	private static HashMap<String, KuaipanAPI> apis = new HashMap<String, KuaipanAPI>();

	public static synchronized KuaipanAPI getInstance() {
		try {
			return getInstance("config.json");
		} catch (KuaipanException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static synchronized KuaipanAPI getInstance(String configFile)
			throws KuaipanException {
		return getInstance(configFile, null);
	}

	public static synchronized KuaipanAPI getInstance(String configFile,
			AuthorizeListner listner) throws KuaipanException {
		KuaipanAPI api = apis.get(configFile);
		if (api == null) {
			Configuration config = Configuration.getConfig(configFile);
			if (config.consumerKey.isEmpty() || config.consumerSecret.isEmpty()) {
				System.err.println("请先在 " + configFile
						+ "中设置你的consumerKey和 consumerSecret。");
				System.exit(1);
			}

			api = new KuaipanAPI(config);
			api.setAuthorizeListner(listner);

			AccessToken t = loadAccessToken(config);
			if (t != null) {
				api.getSession().setAuthToken(t);
				try {
					api.accountInfo();
				} catch (Exception e) {
					e.printStackTrace();
					api.refreshAccessToken();
				}
			} else {
				api.refreshAccessToken();
			}

			apis.put(configFile, api);
		}

		return api;

	}

	private static AccessToken loadAccessToken(Configuration config) {
		if (config == null || config.oauth_token == null
				|| config.oauth_token_secret == null)
			return null;

		return new AccessToken(config.oauth_token, config.oauth_token_secret);
	}

	public static void main(String[] args) throws KuaipanException {
		KuaipanAPI api = getInstance("config.json");
		System.out.println(api.accountInfo());
		
		System.out.println("--------------------------");
		KuaipanFile file = api.metadata("/", true);
		System.out.println(file);
	}
}
