package kuaipan;

import java.io.IOException;

public class DefaultAuthorizeListner implements AuthorizeListner {

	@Override
	public void authorize(String url) {
		System.out.println("到以下网址中使用你的快盘帐号授权，完成后按下ENTER键：\n" + url);

		try {
			System.in.read();
		} catch (IOException e) {
		}
	}

}
