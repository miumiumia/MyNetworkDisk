import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
public class Divide {
	public ArrayList<File> divideFile(File file) throws IOException{
		int nowdisk=Main.disknumber;
		ArrayList<File> ans=new ArrayList<>();
		FileInputStream fileInputStream=new FileInputStream(file);
		byte[]b=new byte[(int) file.length()];
		fileInputStream.read(b);
		int s1=b.length/2;int s2=b.length-s1;
		byte[] b1=new byte[s1];
		byte[] b2=new byte[s2];
		for (int i=0;i<s1;i++) b1[i]=b[i];
		for (int i=0;i<s2;i++) b2[i]=b[i+s1]; 
		File file1=new File("google/"+file.getName());
		FileOutputStream out1=new FileOutputStream(file1);
		out1.write(b1);
		out1.close();
		ans.add(file1);
		File file2=new File("kuaipan/"+file.getName());
		FileOutputStream out2=new FileOutputStream(file2);
		out2.write(b2);
		ans.add(file2);
		out2.close();
		return ans;
	}
}
