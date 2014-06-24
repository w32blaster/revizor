package com.revizor

abstract class HasImage {

	byte[] image
	String imageType // <-- MIME type of uploaded image

    static constraints = {
		image(nullable:true, maxSize: 16384 /* 16K */)
		imageType(nullable:true)
    }

    static mapping = {
        tablePerHierarchy false
    }
}