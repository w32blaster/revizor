package com.revizor

import com.revizor.chats.IChat
import com.revizor.chats.impl.KatoImChat
import com.revizor.chats.impl.SlackChat
import org.springframework.context.i18n.LocaleContextHolder as LCH

class Chat {

    ChatType type
    String name
    String channel // used in Slack
    String url
    String username
    String password

    static constraints = {
        url(nullable: false)
        name(nullable: false)
        username(nullable: true)
        password(nullable: true)
        channel(nullable: true)
    }

    /**
     * Returns implementation regarding the chat type
     *
     * @return
     */
    public IChat initImplementation() {
        def grailsApplication = this.domainClass.grailsApplication
        //def ctx = grailsApplication.mainContext
        def currentLocale = LCH.getLocale()

        switch(this.type) {
            case ChatType.KATO_IM:
                return new KatoImChat(this, grailsApplication, currentLocale);

            case ChatType.SLACK:
                return new SlackChat(this, grailsApplication, currentLocale);

            default:
                throw new RuntimeException("the type of current chat is not detected. " +
                        "Supported values: ${ChatType.values()}")
        }
    }
}

enum ChatType {
    KATO_IM("kato.im.gif"),
    SLACK("slack.png")

    private String imgUrl;

    public ChatType(String url) {
        this.imgUrl = url;
    }

    public String getImageUrl() {
        return this.imgUrl;
    }
}