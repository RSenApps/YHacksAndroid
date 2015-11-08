package rsen.com.yhacks;

import android.view.View;

/**
 * Created by rsen on 11/7/15.
 */
public class Person {
    String name;
    String image_url;
    String description;
    View.OnClickListener onClickListener;
    public Person(String name, String image_url, String description, View.OnClickListener onClickListener)
    {
        this.name = name;
        this.image_url = image_url;
        this.description = description;
        this.onClickListener = onClickListener;
    }

}
