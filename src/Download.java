import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.eclipse.swt.internal.win32.SIZE;

import de.uni_postdam.hpi.jerasure.Decoder;
import kuaipan.KuaipanAuthExpiredException;
import kuaipan.KuaipanIOException;
import kuaipan.KuaipanServerException;


public class Download {
	public void DownloadFile(String name) throws KuaipanIOException, KuaipanAuthExpiredException, KuaipanServerException, IOException, InterruptedException{
		String [] names=Main.location.get(name);
		if (Main.KuaiPanUpload){
			String name1=names[0];
			name1=name1.substring(2);
			name1=name1.replaceAll("\\\\.*\\\\", "");
			System.out.println(name1);
			byte[] b1=Main.kuaipan.downloadFile(name1);
			FileOutputStream out=new FileOutputStream(new File(".\\downloadtemp\\"+name1));
			out.write(b1);
			out.close();
			Thread.sleep(3000);
			String name2=names[1];
			name2=name2.substring(2);
			name2=name2.replaceAll("\\\\.*\\\\", "");
			System.out.println(name2);
			byte[] b2=Main.kuaipan.downloadFile(name2);
			out=new FileOutputStream(new File(".\\downloadtemp\\"+name2));
			out.write(b2);
			out.close();
		}
		if (Main.GoogleUpload){
			String name1=names[2];
			byte[] b1=Main.kuaipan.downloadFile(name1);
			FileOutputStream out=new FileOutputStream(new File(".\\downloadtemp\\"+name1));
			out.write(b1);
			out.close();
		}
		else {
			String name1=names[2];
			name1=name1.substring(2);
			name1=name1.replaceAll("\\\\.*\\\\", "");
			FileInputStream fi=new FileInputStream(new File(".\\google\\"+name1));
			FileOutputStream fout=new FileOutputStream(new File(".\\downloadtemp\\"+name1));
			FileChannel in=fi.getChannel();
			FileChannel out=fout.getChannel();
			//size+=in.size();
			in.transferTo(0, in.size(), out);
			fi.close();
			fout.close();
			in.close();
			out.close();
		}
		Decoder decoder=new Decoder(new File("download/"+name), 2, 1, 2);
		System.out.println(Main.size.get(name));
		decoder.decode(Main.size.get(name));
	}
}
