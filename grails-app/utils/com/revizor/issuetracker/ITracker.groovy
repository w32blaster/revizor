package com.revizor.issuetracker

/**
 * Created on 19/11/14.
 *
 * @author w32blaster
 */
interface ITracker {

    def before();

    IssueTicket getIssueByKey(String key);
}