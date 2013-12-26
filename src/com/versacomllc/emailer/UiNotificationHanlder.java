package com.versacomllc.emailer;

import java.util.Observable;
import java.util.Observer;

public class UiNotificationHanlder implements Observer{

	@Override
	public void update(Observable observable, Object obj) {
		
		if(obj instanceof EmployeeInfo){
			EmployeeInfo info = (EmployeeInfo) obj;
			System.out.println(info.toString());
		}
	}

}
