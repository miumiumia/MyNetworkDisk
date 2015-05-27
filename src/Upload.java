import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import kuaipan.KuaipanAuthExpiredException;
import kuaipan.KuaipanException;
import kuaipan.KuaipanServerException;


public class Upload {
	public void UploadFile(String name) throws IOException, KuaipanServerException, KuaipanAuthExpiredException, KuaipanException{
		java.io.File file=new java.io.File(name);
		
		new Encoders().EncodeFile(file);
		name=name.substring(1);
		name=name.replaceAll("/.*/", "");
		System.out.println("name "+name);
		Main.size.put(name, (int) file.length());
		String[] tempname=Main.location.get(name);
		System.out.println(tempname[1]);
		
		if (Main.KuaiPanUpload){
			File file1=new File(tempname[0]);
			String name1=tempname[0];
			name1=name1.substring(2);
			name1=name1.replaceAll("\\\\.*\\\\", "");
			System.out.println(name1);
			Main.kuaipan.uploadFile(name1, file1);
			File file2=new File(tempname[1]);
			String name2=tempname[1];
			name2=name2.substring(2);
			name2=name2.replaceAll("\\\\.*\\\\", "");
			System.out.println(name2);
			Main.kuaipan.uploadFile(name2, file2);
			file1.delete();
			file2.delete();
		}
		if (Main.GoogleUpload)
		{
			File file3=new File(tempname[2]);
			String name3=tempname[2];
			name3=name3.substring(2);
			name3=name3.replaceAll("\\\\.*\\\\", "");
			System.out.println(name3);
			Main.kuaipan.uploadFile(name3, file3);
			file3.delete();
			Main.googleDrive.uploadFile(name3, file3);
		}
		
		
	}
}
