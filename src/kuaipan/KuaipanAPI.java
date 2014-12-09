package kuaipan;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

import kuaipan.*;

/**
 * <p>
 * 该类是快盘接口的封装类。
 * </p>
 * 
 * @author alex
 * 
 */
public class KuaipanAPI {

	private Session session;
	public Configuration config;
	private AuthorizeListner authorizeListner;

	/**
	 * 通过指定配置实例化API。
	 * 
	 * @param config
	 *            指定的环境配置。
	 */
	public KuaipanAPI(Configuration config) {
		if (config == null)
			throw new IllegalArgumentException();

		this.config = config;
		this.session = new OauthSession(config.consumerKey,
				config.consumerSecret, config.root);

		if ("smart".equals(config.authType)) {
			authorizeListner = new SmartAuthorizeListner(config);
		} else if ("popup".equals(config.authType)) {
			authorizeListner = new BrowserPopupAuthorizeListner(config);
		} else {
			authorizeListner = new DefaultAuthorizeListner();
		}

		if (config.proxyHost != null && !config.proxyHost.isEmpty()
				&& config.proxyPort != null && !config.proxyPort.isEmpty()) {
			System.setProperty("http.proxyHost", config.proxyHost);
			System.setProperty("http.proxyPort", config.proxyPort);
			System.setProperty("https.proxyHost", config.proxyHost);
			System.setProperty("https.proxyPort", config.proxyPort);
		}
	}

	public Session getSession() {
		return session;
	}

	public void setAuthorizeListner(AuthorizeListner listner) {
		authorizeListner = (listner == null ? authorizeListner : listner);
	}

	/**
	 * 用户登陆并授权应用。
	 * 
	 * @param url
	 *            授权用url。
	 */
	private void authorize(String url) {
		authorizeListner.authorize(url);
	}

	/**
	 * 获取未授权的临时Token。
	 * 
	 * @return
	 * @throws KuaipanIOException
	 * @throws KuaipanException
	 */
	public String requestToken() throws KuaipanIOException, KuaipanException {
		String host = KuaipanHTTPUtility.API_HOST;
		session.unAuth();
		KuaipanURL url = OauthUtility.buildGetURL(host, "/open/requestToken",
				null, session.consumer, null, true);

		KuaipanHTTPResponse resp = KuaipanHTTPUtility.requestByGET(url);
		RequestToken requestToken = resp.fromJson(RequestToken.class);

		if (requestToken.oauth_token == null
				|| requestToken.oauth_token_secret == null)
			throw new KuaipanIOException(resp.toString());

		session.setTempToken(requestToken);
		return KuaipanHTTPUtility.AUTH_URL + requestToken.oauth_token;
	}

	/**
	 * 获取 access_token。
	 * 
	 * @throws KuaipanIOException
	 * @throws KuaipanException
	 */
	public void accessToken() throws KuaipanIOException, KuaipanException {
		String host = KuaipanHTTPUtility.API_HOST;
		KuaipanURL url = OauthUtility.buildGetURL(host, "/open/accessToken",
				null, session.consumer, session.requestToken, true);

		KuaipanHTTPResponse resp = KuaipanHTTPUtility.requestByGET(url);
		AccessToken accessToken = resp.fromJson(AccessToken.class);

		if (accessToken.oauth_token == null
				|| accessToken.oauth_token_secret == null)
			throw new KuaipanIOException(resp.toString());

		session.setAuthToken(accessToken);
	}

	/**
	 * 查看用户信息。
	 * 
	 * @return 用户信息
	 * @throws KuaipanException
	 */
	public KuaipanUser accountInfo() throws KuaipanException {
		session.assertAuth();

		String host = KuaipanHTTPUtility.API_HOST;
		KuaipanURL url = OauthUtility.buildGetURL(host, "/1/account_info",
				null, session.consumer, session.accessToken, false);

		KuaipanHTTPResponse resp = KuaipanHTTPUtility.requestByGET(url);
		KuaipanUser user = resp.fromJson(KuaipanUser.class);

		return user;
	}

	/**
	 * 获取文件分享链接。
	 * 
	 * @param path
	 *            文件路径
	 * @param name
	 *            默认本文件名。 在下载页面显示的名字。
	 * @param access_code
	 *            提取码，若需要则输入此值， [a-zA-Z]，6-10位。
	 * @return 文件链接。
	 * @throws KuaipanException
	 */
	public KuaipanPublicLink shares(String path, String name, String access_code)
			throws KuaipanException {

		StringBuffer location = new StringBuffer("/1/shares/");
		location.append(session.root);
		if (!path.startsWith("/"))
			location.append("/");
		location.append(path);

		Map<String, String> params = new TreeMap<String, String>();
		if (name != null)
			params.put("name", name);
		if (access_code != null)
			params.put("access_code", access_code);

		String host = KuaipanHTTPUtility.API_HOST;
		KuaipanURL url = OauthUtility.buildGetURL(host, location.toString(),
				params, session.consumer, session.accessToken, false);

		KuaipanHTTPResponse resp = KuaipanHTTPUtility.requestByGET(url);
		KuaipanPublicLink link = resp.fromJson(KuaipanPublicLink.class);
		return link;
	}

	/**
	 * 获取单个文件，文件夹信息。
	 * 
	 * @param path
	 *            文件 （夹）路径。
	 * @param list
	 *            默认true。当路径指向是文件夹时，会返回其包含的子文件
	 * @return 文件夹信息。
	 * @throws KuaipanIOException
	 * @throws KuaipanServerException
	 * @throws KuaipanAuthExpiredException
	 */
	public KuaipanFile metadata(String path, Boolean list)
			throws KuaipanException {
		session.assertAuth();
		StringBuffer location = new StringBuffer("/1/metadata/");
		location.append(session.root);
		if (!path.startsWith("/"))
			location.append("/");
		location.append(path);

		Map<String, String> params = new TreeMap<String, String>();
		if (list != null)
			params.put("list", list.toString());

		String host = KuaipanHTTPUtility.API_HOST;
		KuaipanURL url = OauthUtility.buildGetURL(host, location.toString(),
				params, session.consumer, session.accessToken, false);

		KuaipanHTTPResponse resp = KuaipanHTTPUtility.requestByGET(url);
		return resp.fromJson(KuaipanFile.class);
	}

	/**
	 * 新建文件夹。
	 * 
	 * @param path
	 *            相对于root的路径,包含文件名。长度不超过255，其所在绝对路径长度也不能超过255。
	 * @return
	 * @throws KuaipanException
	 */
	public KuaipanFile createFolder(String path) throws KuaipanException {
		String location = "/1/fileops/create_folder";
		Map<String, String> params = new TreeMap<String, String>();
		params.put("root", session.root);
		params.put("path", path);

		String host = KuaipanHTTPUtility.API_HOST;
		KuaipanURL url = OauthUtility.buildGetURL(host, location, params,
				session.consumer, session.accessToken, false);

		KuaipanHTTPResponse resp = KuaipanHTTPUtility.requestByGET(url);

		return resp.fromJson(KuaipanFile.class);
	}

	/**
	 * 移动文件，文件夹，不能移动带共享的文件，当然也不支持其他不能移动的情况（如形成环路）。
	 * 
	 * @param from_path
	 *            相对于root的旧文件路径。
	 * @param to_path
	 *            相对于root的新文件路径，包含文件名(和from_path 的文件不同名的话，则重命名)
	 * @return 新文件信息。
	 * @throws KuaipanException
	 */
	public KuaipanFile move(String from_path, String to_path)
			throws KuaipanException {
		String location = "/1/fileops/move";
		Map<String, String> params = new TreeMap<String, String>();
		params.put("root", session.root);
		params.put("from_path", from_path);
		params.put("to_path", to_path);

		String host = KuaipanHTTPUtility.API_HOST;
		KuaipanURL url = OauthUtility.buildGetURL(host, location, params,
				session.consumer, session.accessToken, false);

		KuaipanHTTPResponse resp = KuaipanHTTPUtility.requestByGET(url);

		return resp.fromJson(KuaipanFile.class);
	}

	/**
	 * 复制文件，文件夹。
	 * 
	 * @param from_path
	 *            相对于root的旧文件（夹）路径
	 * @param to_path
	 *            相对于root的新文件夹路径，包含文件名（和from_path的文件不同名的话，则重命名）
	 * @return 新文件信息
	 * @throws KuaipanException
	 */
	public KuaipanFile copy(String from_path, String to_path)
			throws KuaipanException {
		String location = "/1/fileops/copy";
		Map<String, String> params = new TreeMap<String, String>();
		params.put("root", session.root);
		params.put("from_path", from_path);
		params.put("to_path", to_path);

		String host = KuaipanHTTPUtility.API_HOST;
		KuaipanURL url = OauthUtility.buildGetURL(host, location, params,
				session.consumer, session.accessToken, false);

		KuaipanHTTPResponse resp = KuaipanHTTPUtility.requestByGET(url);

		return resp.fromJson(KuaipanFile.class);
	}

	/**
	 * 删除文件，文件夹，以及文件夹下所有文件到回收站。删除有风险，操作需谨慎。
	 * 
	 * @param path
	 *            相对于root将被删除的文件(夹)路径
	 * @return
	 * @throws KuaipanIOException
	 * @throws KuaipanException
	 */
	public KuaipanFile delete(String path) throws KuaipanIOException,
			KuaipanException {
		String location = "/1/fileops/delete";
		Map<String, String> params = new TreeMap<String, String>();
		params.put("root", session.root);
		params.put("path", path);

		String host = KuaipanHTTPUtility.API_HOST;
		KuaipanURL url = OauthUtility.buildGetURL(host, location, params,
				session.consumer, session.accessToken, false);

		KuaipanHTTPResponse resp = KuaipanHTTPUtility.requestByGET(url);

		return resp.fromJson(KuaipanFile.class);
	}

	/**
	 * 获取128x128缩略图。
	 * 
	 * @param path
	 *            相对于root的路径,图片文件的位置
	 * @param os
	 *            获取缩略图后的输入流。
	 * @param lr
	 *            获取过程中的ProgressListener。
	 * @return 缩略图
	 * @throws KuaipanAuthExpiredException
	 * @throws KuaipanServerException
	 * @throws KuaipanIOException
	 */
	public KuaipanHTTPResponse thumbnail(String path, OutputStream os,
			ProgressListener lr) throws KuaipanAuthExpiredException,
			KuaipanServerException, KuaipanIOException {
		return thumbnail(128, 128, path, os, lr);
	}

	/**
	 * 获取缩略图。
	 * 
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @param path
	 *            相对于root的路径,图片文件的位置
	 * @param os
	 *            获取缩略图后的输入流。
	 * @param lr
	 *            获取过程中的ProgressListener。
	 * @return 缩略图
	 * @throws KuaipanAuthExpiredException
	 * @throws KuaipanServerException
	 * @throws KuaipanIOException
	 */
	public KuaipanHTTPResponse thumbnail(int width, int height, String path,
			OutputStream os, ProgressListener lr)
			throws KuaipanAuthExpiredException, KuaipanServerException,
			KuaipanIOException {
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("width", Integer.toString(width));
		params.put("height", Integer.toString(height));
		return do_download(path, KuaipanHTTPUtility.CONV_HOST,
				"/1/fileops/thumbnail", os, lr, params);
	}

	/**
	 * 文档转换。
	 * 
	 * @param type
	 *            文档的类型，支持格式如下：'pdf', 'doc', 'wps', 'csv', 'prn', 'xls', 'et',
	 *            'ppt', 'dps', 'txt', 'rtf'
	 * @param view
	 *            视图格式，下面的参数会影响在浏览器显示的效果（页面大小和排版）：'normal' -
	 *            假如是PC浏览器的话请选择这个；'android' - android客户端请选择这个；'iPad' -
	 *            iPad客户端的请选择这个；'iphone' - iphone客户端。
	 * @param path
	 *            相对于root的路径，要转换的文档的位置
	 * @param os
	 *            获取后的输入流。
	 * @param lr
	 *            获取过程中的ProgressListener。
	 * @return
	 * @throws KuaipanAuthExpiredException
	 * @throws KuaipanServerException
	 * @throws KuaipanIOException
	 */
	public KuaipanHTTPResponse documentView(ConvType type, ConvView view,
			String path, OutputStream os, ProgressListener lr)
			throws KuaipanAuthExpiredException, KuaipanServerException,
			KuaipanIOException {
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("type", type.toString());
		params.put("view", view.toString());
		return do_download(path, KuaipanHTTPUtility.CONV_HOST,
				"/1/fileops/documentView", os, lr, params);
	}

	/**
	 * 下载文件。
	 * 
	 * @param path
	 *            相对于root的路径
	 * @param os
	 *            输出流
	 * @param lr
	 *            下载过程中的ProgressListener。
	 * @return
	 * @throws KuaipanIOException
	 * @throws KuaipanAuthExpiredException
	 * @throws KuaipanServerException
	 */
	public KuaipanHTTPResponse downloadFile(String path, OutputStream os,
			ProgressListener lr) throws KuaipanIOException,
			KuaipanAuthExpiredException, KuaipanServerException {
		return do_download(path, KuaipanHTTPUtility.CONTENT_HOST,
				"/1/fileops/download_file", os, lr, null);
	}

	private KuaipanHTTPResponse do_download(String path, String host,
			String location, OutputStream os, ProgressListener lr,
			Map<String, String> otherParams) throws KuaipanIOException,
			KuaipanAuthExpiredException, KuaipanServerException {
		Map<String, String> params = otherParams;
		if (params == null)
			params = new TreeMap<String, String>();
		params.put("root", session.root);
		params.put("path", path);

		KuaipanURL url = OauthUtility.buildGetURL(host, location, params,
				session.consumer, session.accessToken, false);
		KuaipanHTTPResponse resp = KuaipanHTTPUtility.doDownload(url, os, lr);
		return resp;
	}

	/**
	 * 上传文件。
	 * 
	 * @param path
	 *            文件将要上传到的路径，<b>必须包含文件名</b>。
	 * @param is
	 *            将要上传的文件流
	 * @param size
	 *            将要上传的文件size
	 * @param overwrite
	 *            是否覆盖
	 * @param lr
	 *            上传过程中的ProgressListener。
	 * @return
	 * @throws KuaipanException
	 * @throws KuaipanServerException
	 * @throws KuaipanAuthExpiredException
	 */
	public KuaipanFile uploadFile(String path, InputStream is, long size,
			boolean overwrite, ProgressListener lr) throws KuaipanException,
			KuaipanServerException, KuaipanAuthExpiredException {
		String host = KuaipanHTTPUtility.UploadHostFactory.getUploadHost();
		Map<String, String> params = new TreeMap<String, String>();
		params.put("root", session.root);
		params.put("path", path);
		params.put("overwrite", Boolean.toString(overwrite));

		KuaipanURL url = OauthUtility.buildPostURL(host,
				"/1/fileops/upload_file", params, session.consumer,
				session.accessToken, false);
		KuaipanHTTPResponse resp = KuaipanHTTPUtility.doUpload(url, is, size,
				lr);

		return resp.fromJson(KuaipanFile.class);
	}

	/**
	 * 刷新AccessToken。
	 * 
	 * @throws KuaipanException
	 */
	public void refreshAccessToken() throws KuaipanException {
		String url = requestToken();
		authorize(url);
		accessToken();

		if (session.isAuth()) {
			config.oauth_token = session.accessToken.oauth_token;
			config.oauth_token_secret = session.accessToken.oauth_token_secret;
			config.save();
		}
	}

	public static enum HttpMethod {
		GET, POST;
		public String toString() {
			return this.name();
		}
	}

	public static enum ConvType {
		PDF, DOC, WPS, CSV, PRN, XLS, ET, PPT, DPS, TXT, RTF;
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	public static enum ConvView {
		NORMAL, ANDROID, IPAD, IPHONE;
		public String toString() {
			// this is a hack!
			if (this == IPAD)
				return "iPad";
			return this.name().toLowerCase();
		}
	}
}
