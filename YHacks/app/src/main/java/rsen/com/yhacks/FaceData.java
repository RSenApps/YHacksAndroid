package rsen.com.yhacks;

/**
 * Created by rsen on 11/8/15.
 */
public class FaceData {
    String faceId;
    String imageUrl;
    String time;
    boolean identified;
    String personName;
    String personId;
    public FaceData(String faceId, String imageUrl, String time, String personName, String personId)
    {
        this.faceId = faceId;
        this.imageUrl = imageUrl;
        this.time = time;
        this.identified = personName.length()>1;
        this.personName = personName;
        this.personId = personId;
    }
}
