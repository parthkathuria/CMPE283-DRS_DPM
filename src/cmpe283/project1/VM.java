package cmpe283.project1;

import com.vmware.vim25.AlarmState;

import com.vmware.vim25.VirtualMachineQuickStats;
import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;


public class VM {

	private VirtualMachine vm;

	public VM(VirtualMachine vm) {
		this.vm = vm;
	}

	public void powerOn() throws Exception {
		Task task = vm.powerOnVM_Task(null);
		System.out.println(vm.getName() + " is powering on...");
		
		if (task.waitForTask() == Task.SUCCESS)
			Thread.sleep(5000);
			System.out.println(vm.getName() + " is running now.");
	}

	public void powerOff() throws Exception {
		Task task = vm.powerOffVM_Task();
		System.out.println(vm.getName() + " is powering off...");
		if (task.waitForTask() == Task.SUCCESS)
			System.out.println(vm.getName() + " is genuine shut down.");
	}

	
	
	public boolean checkPowerOffAlarm(Alarm alarm) {
		AlarmState[] as = vm.getTriggeredAlarmState();
		if (as == null)
			return false;
		for (AlarmState state : as) {
			// if the vm has a poweroff alarm, return true;
			if (alarm.getMOR().getVal().equals(state.getAlarm().getVal()))
				return true;
		}
		return false;
	}

	public boolean ping() throws Exception {
		int time = 0;
		
		while (!Ping.pingIP(getIP())) {						
			time++;
			if (time >= Setting.NumPing)
				return false;
			else 
				Thread.sleep(Setting.PingInterval * 1000);
		}
		return true;
	}

	public VirtualMachine getVM() {
		return vm;
	}

	public String getIP() {
		return vm.getGuest().getIpAddress();
	}
	
	public String getName() {
		return vm.getName();
	}
	
	public void print() throws Exception {
		System.out.println("\nName: " + vm.getName());
		System.out.println("Guest OS: "
				+ vm.getSummary().getConfig().guestFullName);
		System.out.println("VM Version: " + vm.getConfig().version);
		System.out.println("CPU: " + vm.getConfig().getHardware().numCPU
				+ " vCPU");
		System.out.println("Memory: " + vm.getConfig().getHardware().memoryMB
				+ " MB");
		System.out.println("IP Addresses: " + vm.getGuest().getIpAddress());
		System.out.println("State: " + vm.getGuest().guestState);

		if (!vm.getGuest().guestState.equals("notRunning")) {
			System.out.println("Data from VirtualMachineQuickStats: ");
			VirtualMachineQuickStats qs = vm.getSummary().getQuickStats();
			System.out.println(String.format("%-25s%s", "OverallCpuUsage: ",
					qs.getOverallCpuUsage() + " MHz"));
			System.out.println(String.format("%-25s%s", "GuestMemoryUsage: ",
					qs.getGuestMemoryUsage() + " MB"));
			System.out.println(String.format("%-25s%s",
					"ConsumedOverheadMemory: ", qs.getConsumedOverheadMemory()
							+ " MB"));
			System.out.println(String.format("%-25s%s", "FtLatencyStatus: ",
					qs.getFtLatencyStatus()));
			System.out.println(String.format("%-25s%s",
					"GuestHeartbeatStatus: ", qs.getGuestHeartbeatStatus()));
		}
	}
}
