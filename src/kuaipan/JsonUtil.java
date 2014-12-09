package kuaipan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonUtil {

	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static <T> T fromJson(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}
}
