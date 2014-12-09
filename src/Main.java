import java.util.*;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.CertificateException;

import javax.net.ssl.*;
import javax.net.*;
import javax.swing.JFrame;

import kuaipan.Kuaipan;
import googledisk.*;
public class Main {
	static GoogleDrive googleDrive;
	static Kuaipan kuaipan;
	static int disknumber=2;
	public static void main(String[] args) throws Exception {
		UI ui=new UI();
		ui.setTitle("Online Distributed Disk");
		ui.setSize(700,450);
		ui.setLocationRelativeTo(null);
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setVisible(true);
		googleDrive=new GoogleDrive();
		googleDrive.init();
		kuaipan=new Kuaipan();
		kuaipan.init();
	}
}
