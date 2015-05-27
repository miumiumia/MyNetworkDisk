import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.uni_postdam.*;
import de.uni_postdam.hpi.jerasure.Encoder;
import de.uni_postdam.hpi.utils.FileUtils;
public class Encoders {
	Encoder encoder;
	public Encoders() {
		encoder=new Encoder(2, 1, 2);
	}
	public void EncodeFile(File file){
		encoder.encode(file);
		Filename(file.getName());
	}
	public void Filename(String file){
		String suffix="k";
		String[] filename=new String[3];
		String filePath=".\\kuaipan\\";
		for (int i = 0; i < 2; i++) {
			String partName = FileUtils.generatePartPath(filePath+file, suffix, i+1);
			filename[i]=partName;
		}
		filePath=".\\google\\";
		suffix="m";
		for (int i=0;i<1;i++){
			String partName = FileUtils.generatePartPath(filePath+file, suffix, i+1);
			filename[i+2]=partName;
		}
		for (int i=0;i<3;i++)
		{
			System.out.println(i+" "+filename[i]);
		}
		System.out.println("file "+file);
		Main.location.put(file, filename);
	}
}
