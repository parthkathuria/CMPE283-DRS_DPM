package cmpe283.project1;

import java.net.URL;

import com.vmware.vim25.mo.ServiceInstance;

public class AvlMgr {

	public static void main(String[] args) throws Exception {

				
		ServiceInstance si = new ServiceInstance(new URL(Setting.VcenterUrl),
				Setting.UserName, Setting.Password, true);
		VCenter vCenter = new VCenter(si);
		
		// print vHosts and VMs performance statistics
		ThreadMgr printStat = new ThreadMgr(vCenter, "Statistics");
		printStat.start();

		// Remove old snapshots and create new snapshot periodically for both vms and vhosts
		ThreadMgr backup = new ThreadMgr(vCenter, "backup");
		backup.start();				
		
		// check the heartbeat at regular intervals
		ThreadMgr heartbeat = new ThreadMgr(vCenter, "heartbeat");
		heartbeat.start();
	
			

	}
}
