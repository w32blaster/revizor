package com.revizor

abstract class HasImage {

    boolean hasImage
	byte[] image
	String imageType // <-- MIME type of uploaded image

    static constraints = {
		image(nullable:true, maxSize: 20 * 1024 * 1024)
		imageType(nullable:true)
    }

    static mapping = {
        tablePerHierarchy false
        hasImage defaultValue: false
        image sqlType: "blob"
    }
}