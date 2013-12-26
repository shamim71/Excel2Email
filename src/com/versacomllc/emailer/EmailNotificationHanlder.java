package com.versacomllc.emailer;

import java.util.Observable;
import java.util.Observer;

public class EmailNotificationHanlder implements Observer{

	private ConfigurationData conf;

	@Override
	public void update(Observable arg0, Object obj) {
		if(obj instanceof EmployeeInfo){
			EmployeeInfo info = (EmployeeInfo) obj;
			//System.out.println("Sending email notification for: "+ info.getLastName());
			EmailerData data = new EmailerData();
			data.setConfig(conf);
			data.setReceiver(info);
			if(info.isSent()){
				EmailNotifyWorker worker = new EmailNotifyWorker(data);
				TaskExecutionPool.getInstance().addTaskToPool(worker);
			}

		}
		
	}

	public EmailNotificationHanlder(ConfigurationData conf) {
		super();
		this.conf = conf;
	}


}
