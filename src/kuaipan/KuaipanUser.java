package kuaipan;

import kuaipan.*;

public class KuaipanUser {
	public long user_id;
	public String user_name;
	public long max_file_size;
	public long quota_total;
	public long quota_used;
	public long quota_recycled = -1;

	public String toString() {
		return JsonUtil.toJson(this);
	}
}
