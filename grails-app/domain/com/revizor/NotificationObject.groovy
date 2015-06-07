package com.revizor

/**
 * Notification object is an entity with whom an actor performs an action.
 *
 * Let's say, "User John Doe just created a new review".
 * Here:
 *     "John Doe" - main actor 
 *     "created" - action  
 *     "new review" - object.
 * 
 * This class allows to save any object in the database and to store it type-safely
 */
public class NotificationObject {

    ObjectType type;
    Long objectId;
    String value;
    
    /*
     * Index (idx) - is a position in a template message. For example, 
     * "{0} commented {1} in a review {2}" - here numbers 0, 1 and 2
     * are indexes.
     */
    int idx;

    static constraints = {
        value(nullable: true)
        objectId(nullable: true)
        type(nullable:false)
        idx(nullable: false)
    }

    public Object resoreInstance() {
        switch(this.type) {
            case ObjectType.REPO:
                return Repository.get(objectId);

            case ObjectType.COMMENT:
                return Comment.get(objectId);

            case ObjectType.REVIEW:
                return Review.get(objectId);

            case ObjectType.USER:
                return User.get(objectId);

            case ObjectType.STRING:
                return value;
        };
    }

    public static NotificationObject saveObject(objectToSave, index) {
        if (objectToSave.class == Repository.class) {
            return new NotificationObject(type: ObjectType.REPO, objectId: objectToSave.ident(), idx: index)
        }
        else if (objectToSave.class == Comment.class) {
            return new NotificationObject(type: ObjectType.COMMENT, objectId: objectToSave.ident(), idx: index)
        }
        else if (objectToSave.class == User.class) {
            return new NotificationObject(type: ObjectType.USER, objectId: objectToSave.ident(), idx: index)
        }
        else if (objectToSave.class == Review.class) {
            return new NotificationObject(type: ObjectType.REVIEW, objectId: objectToSave.ident(), idx: index)
        }
        else if (objectToSave.class == String.class) {
            return new NotificationObject(type: ObjectType.STRING, value: objectToSave, idx: index)
        }
        else {
            throw new RuntimeException("The object's type is not recognyzed! ${objectToSave.class.name}");
        }
    }
}

