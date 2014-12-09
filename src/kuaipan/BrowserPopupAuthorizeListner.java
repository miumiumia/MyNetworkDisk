package kuaipan;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class BrowserPopupAuthorizeListner implements AuthorizeListner {

	Browser browser;
	private final Configuration config;
	
	public BrowserPopupAuthorizeListner(Configuration config) {
		this.config = config;
	}
	
	@Override
	public void authorize(String url) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("授权金山快盘");
		
		initializeBrowser(shell, url);
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}
	
	void initializeBrowser(Shell parent, String url) {
		try {
			browser = new Browser(parent, SWT.NONE);
		} catch (SWTError e) {
			/* Browser widget could not be instantiated */
			Label label = new Label(parent, SWT.CENTER | SWT.WRAP);
			label.setText("BrowserNotCreated");
			new DefaultAuthorizeListner().authorize(url);
		}
		
		if (browser != null) {
			browser.addProgressListener(new ProgressListener() {
				public void changed(ProgressEvent event) {
				}

				public void completed(ProgressEvent event) {
					String adb = 
							"document.getElementById(\"username\").value=\"" + config.userName + "\";\n" +
							"document.getElementById(\"userpwd\").value=\"" + config.password + "\";";
					browser.execute(adb);
				}
			});
		}
		
		browser.setUrl(url);
	}
}
