package cmpe283.project1;

import java.util.ArrayList;
import java.util.List;


import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;


public class VCenter {
	private ServiceInstance vCenter;
	private List<VHost> vHosts;
	private Alarm powerOffAlarm;

	public VCenter(ServiceInstance vCenter) throws Exception {
		this.vCenter = vCenter;
		setHosts();
		powerOffAlarm = AlarmMgr.createPowerAlarm(vCenter);
	}
	
	public void printStat() throws Exception {
		while (true) {
			for (VHost host : vHosts)
				host.print();
			System.out.println(String.format("Sleeping %d seconds until next print...", Setting.PrintInterval));
			System.out.println();
			Thread.sleep(Setting.PrintInterval * 1000);
		}
	}

	public void heartbeat() throws Exception {
		while (true) {
			System.out.println();
			for (int i = 0; i < vHosts.size(); i++) {
				
				if (!checkAvailability(vHosts.get(i))) {
					// solution: if vhost is unavailable, recover from snapshot
					if (!vHosts.get(i).recover()) {
						System.out.println("Cannot be recovered from the snapshot");
					}
					
					i = -1;
				}
				
			} 
			System.out.println(String.format("Sleeping %d seconds until next heartbeat...", Setting.HeartbeatInterval));
			System.out.println();
			Thread.sleep(Setting.HeartbeatInterval * 1000);
		}
	}

	public void createSnapshot() throws Exception {
		while(true) {
			System.out.println();
			for (VHost host : vHosts) {
				host.createSnapshot();
			}
			System.out.println(String.format("Sleeping %d seconds until next snapshot...", Setting.BackupInterval));
			System.out.println();
			Thread.sleep(Setting.BackupInterval * 1000);
		}
	}

	public Alarm getPowerOffAlarm() {
		return powerOffAlarm;
	}
	
	private void setHosts() throws Exception {
		this.vHosts = new ArrayList<VHost>();
		Folder vCenterFolder = vCenter.getRootFolder();
		ManagedEntity[] vHosts = new InventoryNavigator(vCenterFolder)
				.searchManagedEntities("HostSystem");
		if (vHosts.length != 0) {
			for (int i = 0; i < vHosts.length; i++) {
				this.vHosts.add(new VHost((HostSystem) vHosts[i]));
			}
			System.out.println("All connected hosts retrieved.");
		} else {
			System.out.println("No host connected.");
		}
	}

	
	private boolean checkAvailability(VHost vhost) throws Exception {
		vhost.setVMs();
		List<VM> vms = vhost.getVMs();
		if (vms == null) return true;
		
		for (int i = 0; i < vms.size(); i++) {
			VM vm = vms.get(i);
			
			if (vm.checkPowerOffAlarm(powerOffAlarm)) {
				System.out.println(vm.getVM().getName() + " is genuine shut down by user.");
				continue;
			}
			
			if (vm.ping()) {
				System.out.println(vm.getVM().getName() + " is available.");
				continue;
			}

			System.out.println(vm.getVM().getName() + " is unreachable.");

			// vm failure, vhost ping success, recover vm to last state
			if (vhost.ping()) {
				System.out.println(vhost.getIP() + ": host is available.");
				// recover vm by snapshot
				SnapShotMgr.revert2LastSnapshot(vm);
				vm.powerOn();
				while(vm.getIP() == null);
				i--;
			} else {
				System.out.println(vhost.getIP() + ": host ping failure.");
				return false;
			}
		}
		return true;
	}
}
