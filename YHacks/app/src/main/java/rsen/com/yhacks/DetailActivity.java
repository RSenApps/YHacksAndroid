package rsen.com.yhacks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.PersonFace;
import com.microsoft.projectoxford.face.contract.PersonGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity {
    String personId;
    String groupId;
    RecyclerView rv;
    View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        groupId = getIntent().getStringExtra("groupId");
        personId = getIntent().getStringExtra("personId");
        final String[] imageURLs = getIntent().getStringArrayExtra("imageURLs");
        String[] imageTimes = getIntent().getStringArrayExtra("imageTimes");
        final String[] faceIds = getIntent().getStringArrayExtra("faceIds");
        final String name = getIntent().getStringExtra("name");
        String userData = getIntent().getStringExtra("userData");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);
        ((TextView) findViewById(R.id.info)).setText(userData);
        rv = (RecyclerView) findViewById(R.id.rv);
        progress = findViewById(R.id.progress);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        findViewById(R.id.fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, SearchUser.class);
                i.putExtra("thumbURL", imageURLs[0]);
                i.putExtra("name", name);
                i.putExtra("faceIds", faceIds);
                i.putExtra("personId", personId);
                startActivity(i);
            }
        });


        ArrayList<rsen.com.yhacks.Person> listData = new ArrayList<>();
        int counter = 0;
        for (String imageURL : imageURLs)
        {
            listData.add(new Person(imageTimes[counter], imageURL, "", null));
            counter++;
        }

        RVAdapter adapter = new RVAdapter(listData);
        rv.setAdapter(adapter);
        progress.setVisibility(View.GONE);
    }
}
