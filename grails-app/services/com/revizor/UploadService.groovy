package com.revizor

import grails.transaction.Transactional

@Transactional
class UploadService {

    private static final ALLOWED_META_TYPES = ['image/png', 'image/jpeg', 'image/gif']

    /**
     * Retrieves a binary image data from a request and saves it to the DB
     */
    def saveUploadedImage(HasImage object, request) {
        
        // Get the logo file from the multi-part request
        def f = request.getFile('image')

        // List of OK mime-types
        if (!ALLOWED_META_TYPES.contains(f.getContentType())) {
            flash.message = "Logo must be one of: ${ALLOWED_META_TYPES}"
            render(view:'_select_avatar', model:[id : object.ident()])
            return
        }

        // Save the image and mime type
        object.image = f.bytes
        object.imageType = f.contentType
        log.info("File uploaded: $object.imageType")

        return object
    }

    /**
     * Retrieves the image from a user having HasImage parent
     */
    def getImage(HasImage object, response) {
        if (!object || !object.image || !object.imageType) {
            response.sendError(404)
            return
        }
        response.contentType = object.imageType
        response.contentLength = object.image.size()
        OutputStream out = response.outputStream
        out.write(object.image)
        out.close()
    }
}
