package rsen.com.yhacks;

import java.util.UUID;

/**
 * Created by rsen on 11/8/15.
 */
public class LinkedPerson {
    String personId;
    String name;
    String fbLink;
    String userData;
    String thumbURL;
    String[] faceIDs;
    public LinkedPerson(String personId, String name, String fbLink, String userData, String thumbURL)
    {
        this.personId = personId;
        this.name = name;
        this.fbLink = fbLink;
        this.userData = userData;
        this.thumbURL = thumbURL;
    }
}
