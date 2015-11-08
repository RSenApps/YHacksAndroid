package rsen.com.yhacks;

import android.view.View;

/**
 * Created by rsen on 11/7/15.
 */
public class Person {
    String name;
    String image_url;
    int countPhotos;
    View.OnClickListener onClickListener;
    public Person(String name, String image_url, int countPhotos, View.OnClickListener onClickListener)
    {
        this.name = name;
        this.image_url = image_url;
        this.countPhotos = countPhotos;
        this.onClickListener = onClickListener;
    }

}
