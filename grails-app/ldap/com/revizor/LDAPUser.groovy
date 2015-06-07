package com.revizor

import gldapo.schema.annotation.GldapoNamingAttribute
import gldapo.schema.annotation.GldapoSynonymFor

/**
 * Created by ilja on 01/05/15.
 */
class LDAPUser {

    @GldapoNamingAttribute
    @GldapoSynonymFor("uid")
    String uid

    @GldapoSynonymFor("cn")
    String cn

    @GldapoSynonymFor("sn")
    String sn

    @GldapoSynonymFor("mail")
    String email
}
