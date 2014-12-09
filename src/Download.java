import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kuaipan.KuaipanAuthExpiredException;
import kuaipan.KuaipanIOException;
import kuaipan.KuaipanServerException;


public class Download {
	public void DownloadFile(String name) throws KuaipanIOException, KuaipanAuthExpiredException, KuaipanServerException, IOException{
		byte[] b1=Main.googleDrive.downloadFile(name);
		byte[] b2=Main.kuaipan.downloadFile(name);
		byte[] b=new byte[b1.length+b2.length];
		for (int i=0;i<b1.length;i++)
			b[i]=b1[i];
		for (int i=0;i<b2.length;i++)
			b[i+b1.length]=b2[i]; 
		FileOutputStream out=new FileOutputStream(new File("download/"+name));
		out.write(b);
		out.close();
	}
}
