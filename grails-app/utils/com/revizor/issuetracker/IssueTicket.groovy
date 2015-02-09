package com.revizor.issuetracker

/**
 * Created on 24/11/14.
 *
 * @author w32blaster
 */
class IssueTicket {

    String title
    String[] tags
    String status // <-- just string name of status
    String statusImgUrl // <-- tracker specific icon Url for an issue status
    boolean isClosed //
    String issueUrl
    String authorImgUrl
    String authorName
    String trackerLogoUrl

}
