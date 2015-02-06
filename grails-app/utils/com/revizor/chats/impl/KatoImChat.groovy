package com.revizor.chats.impl;

import com.revizor.Chat;
import com.revizor.Review;
import com.revizor.chats.IChat;
import org.springframework.context.ApplicationContext;

import java.util.Locale;

/**
 * Created on 06/02/15.
 *
 * @author w32blaster
 */
public class KatoImChat implements IChat {

    private ApplicationContext context;
    private Locale locale;
    private Chat chat;

    public KatoImChat(Chat chat, ApplicationContext ctx, Locale locale) {
        this.chat = chat
        this.context = ctx
        this.locale = locale
    }

    /**
     * Perform some optional actions before making any queries.
     * Typically, it is authentication.
     */
    @Override
    public Object before() {
        return null;
    }

    /**
     * Notify chat that a review was created
     *
     * @param review
     * @return
     */
    @Override
    public Object notifyReviewStarted(Review review) {
        return null;
    }

    /**
     * Notify chat that a review was closed
     *
     * @param review
     * @return
     */
    @Override
    public Object notifyReviewClosed(Review review) {
        return null;
    }
}
