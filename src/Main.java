import java.util.*;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.CertificateException;

import javax.net.ssl.*;
import javax.net.*;
import javax.swing.JFrame;

import com.google.api.services.drive.model.File;

import de.uni_postdam.hpi.utils.FileUtils;
import kuaipan.Kuaipan;
import googledisk.*;

public class Main {
	static GoogleDrive googleDrive;
	static Kuaipan kuaipan;
	static int disknumber=2;
	static boolean KuaiPanUpload;
	static boolean GoogleUpload;
	public static Map<String, String[]> location=new HashMap<String, String[]>();
	public static Map<String, Integer> size=new HashMap<String, Integer>();
	public static void main(String[] args) throws Exception {
		UI ui=new UI();
		ui.setTitle("My Online Distributed Disk");
		ui.setSize(700,450);
		ui.setLocationRelativeTo(null);
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setVisible(true);
		setUpload();
		if (KuaiPanUpload){
			kuaipan=new Kuaipan();
			kuaipan.init();
		}
		if (GoogleUpload){
			googleDrive=new GoogleDrive();
			googleDrive.init();
		}
		
		
	}
	public static void setUpload() throws FileNotFoundException{
		java.io.File file=new java.io.File("network.json");
		if (!file.exists()) System.exit(1);
		Scanner scanner=new Scanner(file);
		System.out.println(file.exists());
		String kuaipan=scanner.nextLine().split(" ")[2];
		if (kuaipan.equals("true"))
			KuaiPanUpload=true;
		else KuaiPanUpload=false;
		String Google=scanner.nextLine().split(" ")[2];
		if (Google.equals("true"))
			GoogleUpload=true;
		else GoogleUpload=false;
	}
}
