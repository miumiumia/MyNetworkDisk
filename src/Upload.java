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
		Divide divide=new Divide();
		ArrayList<File> files=divide.divideFile(file);
		File googleFile=files.get(0);
		File kuaipanfile=files.get(1);
		name=name.substring(1);
		name=name.replaceAll("/.*/", "");
		Main.googleDrive.uploadFile(name, googleFile);
		Main.kuaipan.uploadFile(name, kuaipanfile);
	}
}
