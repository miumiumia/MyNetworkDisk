package kuaipan;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 该类是配置文件的实体类，用来读取和保存配置文件的相关信息。
 * 
 * @author alex
 */
public class Configuration {

	public String consumerKey = "xcZb1f50yM4K8fHE";
	public String consumerSecret = "guw2Ow9pk41Q4zmn";
	public String oauth_token;
	public String oauth_token_secret;
	public String userName;
	public String password;
	public String root = "app_folder";
	public String proxyHost;
	public String proxyPort;
	public String authType;
	
	private String configFile = "config.json";

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * 解析 指定的配置文件，获取Configuration实例。
	 * @param configFile 指定配置文件的路劲。
	 * @return Configuration实例.
	 */
	public static Configuration getConfig(String configFile) {
		Configuration config = null;

		try {
			config = gson.fromJson(new FileReader(configFile), Configuration.class);
			config.configFile = configFile;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			config = new Configuration();
		}
		
		if (!("app_folder".equals(config.root) || "kuaipan".equals(config.root))) {
			config.root = "app_folder";
		}

		return config;
	}
	
	/**
	 * 将最新的信息保存到文件。
	 */
	public void save() {
		String json = gson.toJson(this);

		FileWriter writer = null;
		try {
			writer = new FileWriter(configFile, false);
			writer.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
	}
}
