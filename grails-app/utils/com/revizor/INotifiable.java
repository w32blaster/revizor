package com.revizor;

/**
 * Domain objects implementing this interface are intended to be used in the notification message building.
 * To help build human readable message, each domain should provide name to be used in a notification and
 * the link how to quickly navigate to that object
 * 
 * @author ilja
 *
 */
public interface INotifiable {

	/**
	 * Returns an URL-link that could be used to navigate to this object directly
	 * 
	 * @return
	 */
	String getNotificationLink();
	
	/**
	 * Returns human readable name for the current object
	 * @return
	 */
	String getNotificationName();
}