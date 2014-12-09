package kuaipan;

import java.util.List;

import kuaipan.*;


public class KuaipanFile {
	public String file_id;
	public String name;
	public String hash;
	public String root;
	public String path;
	public String rev;
	public String create_time = null;
	public String modify_time = null;
	public boolean is_deleted = false;
	public String type = "file";
	public int size = 0;
	public List<KuaipanFile> files = null;
	
	public String toString() {
		return JsonUtil.toJson(this);
	}
}
