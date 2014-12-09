package kuaipan;

/**
 * <p>
 * 该接口用于处理 {@code KuaipanAPI}#{@code requestToken} 返回的url。
 * 可以通过实现该接口，根据不同的情况调用不同的授权方法：
 * </p>
 * 
 * <ul>
 * <li>提示用户到使用该网址进行授权。</li>
 * <li>自动打开浏览器让用户进行授权。</li>
 * <li>使用预先设置的用户名密码自动进行授权。</li>
 * <li>...</li>
 * </ul>
 * 
 * @author alex
 * 
 */
public interface AuthorizeListner {
	public void authorize(String url);
}
