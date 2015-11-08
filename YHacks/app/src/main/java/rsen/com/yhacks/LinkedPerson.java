package rsen.com.yhacks;

/**
 * Created by rsen on 11/8/15.
 */
public class LinkedPerson {
    String personId;
    String name;
    String fbLink;
    String userData;
    String thumbURL;
    public LinkedPerson(String personId, String name, String fbLink, String userData, String thumbURL)
    {
        this.personId = personId;
        this.name = name;
        this.fbLink = fbLink;
        this.userData = userData;
        this.thumbURL = thumbURL;
    }
}
