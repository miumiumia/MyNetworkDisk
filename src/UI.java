import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import kuaipan.KuaipanAuthExpiredException;
import kuaipan.KuaipanException;
import kuaipan.KuaipanIOException;
import kuaipan.KuaipanServerException;

public class UI extends JFrame{
	
	private String baiduID;
	private String baiduPassword;
	private String googleID;
	private String googlePassword;
	protected JLabel title =new JLabel("MY CLOUD STORAGE");
	private String root="./files";
	private String []kinds ={"图片","文档","视频","音乐","其它"};
	private String []location={"./picture","./document","./video","./music","./other"};
	//protected ImageIcon cloud =new ImageIcon("cloud.jpg");
	public JList Leftlist=new JList(kinds);               
	private DefaultListModel dlm=new DefaultListModel<>();
	public JList Rightlist =new JList(dlm); 
	private static final Font font1 = new Font("Calibri",Font.BOLD,15);
	
	public UI() {
		Leftlist.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				String lo=location[Leftlist.getSelectedIndex()];
				File file=new File(lo);
				File[] list=file.listFiles();
				dlm.clear();
				for (File temp:list){
					dlm.addElement(temp.getName());
				}
				
			}
		});
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		JButton jbtUpload = new JButton("上传到云端");
		JButton jbtDownload = new JButton("下载到本地");
		p1.add(jbtUpload);
		p1.add(jbtDownload);
		add(p1,BorderLayout.NORTH);
		
		//		
		JPanel jptitle = new JPanel();
		JPanel Selectandfile =new JPanel();
		JPanel Select = new JPanel();
		JPanel file =new JPanel();
		
		TitledBorder titleBorder1 = BorderFactory.createTitledBorder(null,//Border border,
                "Category",//String title,
                TitledBorder.LEFT,//int titleJustification,
                TitledBorder.TOP,//int titlePosition,
                font1,//Font titleFont,
                Color.BLACK);//Color
		TitledBorder titleBorder2 = BorderFactory.createTitledBorder(null,//Border border,
                "File List",//String title,
                TitledBorder.LEFT,//int titleJustification,
                TitledBorder.TOP,//int titlePosition,
                font1,//Font titleFont,
                Color.BLACK);//Color

		Leftlist.setBackground(Color.WHITE);
		Leftlist.setSelectionForeground(Color.pink);
		Leftlist.setFont(new Font("Courier",Font.BOLD,18));
		//Leftlist.setVisibleRowCount(10);	
		Leftlist.setFixedCellWidth(160);
		Rightlist.setBackground(Color.WHITE);
		Rightlist.setSelectionForeground(Color.pink);
		//Rightlist.setVisibleRowCount(20);
		Rightlist.setFont(new Font("Courier",Font.BOLD,18));	
		Rightlist.setFixedCellWidth(450);
		Select.add(new JScrollPane(Leftlist));
		Select.setBorder(titleBorder1);	
		
		file.add(new JScrollPane(Rightlist));
		file.setBorder(titleBorder2);	
		
		Selectandfile.setLayout(new BorderLayout());
		Selectandfile.add(Select,BorderLayout.WEST);
		Selectandfile.add(file,BorderLayout.CENTER);
		jbtUpload.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String lo=location[Leftlist.getSelectedIndex()];
				String name=lo+"/"+(String)Rightlist.getSelectedValue();
				Upload upload=new Upload();
				try {
					upload.UploadFile(name);
				} catch (IOException | KuaipanException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		jbtDownload.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String name=(String) Rightlist.getSelectedValue();
				Download download=new Download();
				try {
					download.DownloadFile(name);
				} catch (KuaipanIOException | KuaipanAuthExpiredException
						| KuaipanServerException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		title.setFont(new Font("Courier",Font.BOLD,40));
		jptitle.add(title,BorderLayout.NORTH);	 
		jptitle.add(Selectandfile,BorderLayout.CENTER);

		add(jptitle, BorderLayout.CENTER);
		
	}

}
