package cmpe283.project1;

import java.util.HashMap;

public class Setting {
	public final static String UserName = "administrator";
	public final static String Password = "12!@qwQW";
	public final static String VcenterUrl = "https://130.65.132.105/sdk";
	public final static String SuperVcenterUrl = "https://130.65.132.14/sdk";
	public final static int NumConnectHost = 5;
	// No. of attempts to reconnect a host
	public final static int BackupInterval = 600;  //seconds  
	// To create snapshots and initiate recovery if failed 
	public final static int HeartbeatInterval = 20;  //seconds
	// To check the heartbeat interval
	public final static int PrintInterval = 1200;  //seconds
	// To print the information of VMs available
	public final static int NumPing = 3; 
	// No. of ping attempts if not responding
	public final static int PingInterval = 1; //seconds
	
	
	
	public final static HashMap<String, String> VMs = new HashMap<String, String>() {
		
		private static final long serialVersionUID = 1L;

		{
			put("130.65.132.171", "T05-vHost01-cum2_IP=.132.171");
			put("130.65.132.172", "T05-vHost02-cum2_IP=.132.172");
					}
	};
	
//	public final static String[] PerfCounters = { "cpu.usage.average",
//			"cpu.usagemhz.average", "cpu.used.summation", "cpu.wait.summation",
//			"mem.usage.average", "mem.overhead.average",
//			"mem.consumed.average", "net.usage.average",
//			"net.received.average", "net.transmitted.average",
//			"disk.commands.summation", "disk.usage.average",
//			"datastore.datastoreReadBytes.latest",
//			"virtualDisk.readOIO.latest", "virtualDisk.writeOIO.latest" };
}
