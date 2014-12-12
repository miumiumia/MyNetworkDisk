package kuaipan;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.mortbay.jetty.AbstractGenerator.OutputWriter;

import kuaipan.*;

public class Kuaipan {
	HashMap<String, KuaipanFile> files=new HashMap<>();
	KuaipanAPI api;
	public void uploadFile(String name,java.io.File file) throws KuaipanServerException, KuaipanAuthExpiredException, KuaipanException, IOException{
		FileInputStream fileInputStream=new FileInputStream(file);
		String path=name;
		
		KuaipanFile kuaipanFile=api.uploadFile(path, fileInputStream, file.length(),true,null);
		fileInputStream.close();
		files.put(name, kuaipanFile);
	}
	public byte[]downloadFile(String name) throws KuaipanIOException, KuaipanAuthExpiredException, KuaipanServerException, IOException{
		ByteArrayOutputStream os=new ByteArrayOutputStream();
		String path=name;
		KuaipanHTTPResponse resp=api.downloadFile(path, os, null);
		return os.toByteArray();
	}
	public void init(){
		api = KuaipanAPIFactory.getInstance();
	}
}
