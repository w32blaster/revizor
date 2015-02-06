package com.revizor.chats

import com.revizor.Review

/**
 * Created on 06/02/15.
 *
 * @author w32blaster
 */
interface IChat {

    /**
     * Perform some optional actions before making any queries.
     * Typically, it is authentication.
     *
     */
    def before();

    /**
     * Notify chat that a review was created
     *
     * @param review
     * @return
     */
    def notifyReviewStarted(Review review);

    /**
     * Notify chat that a review was closed
     *
     * @param review
     * @return
     */
    def notifyReviewClosed(Review review);
}