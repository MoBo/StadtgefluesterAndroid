package de.unibremen.controller;

public class ActivityController implements IActivityController{
	
	private static ActivityController activityController = new ActivityController();
	private ActivityController(){};
	public static IActivityController getInstance(){
		return activityController;		
	}
	
	public String HelloWorld(){
		return "Hello World";		
	}

}
