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
	 * At the end of the notification message, the actor may present detailed view. 
	 *
	 * For example, comments. A notification said that "user A left a comment" and after that goes
	 * formatted comment itself.
	 * 
	 * Optional.
	 *
	 * @return html detailed view 
	 */
	String getDetailsAsHtml();
	
	/**
	 * Returns human readable name for the current object
	 * @return
	 */
	String getNotificationName();
}