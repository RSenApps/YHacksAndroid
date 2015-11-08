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
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.PersonFace;
import com.microsoft.projectoxford.face.contract.PersonGroup;

import java.util.ArrayList;
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
        String name = getIntent().getStringExtra("name");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(name);
        rv = (RecyclerView) findViewById(R.id.rv);
        progress = findViewById(R.id.progress);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        new FetchDataTask().execute();

    }
    class FetchDataTask extends AsyncTask<Object, String, ArrayList<Person>> {
        @Override
        protected ArrayList<rsen.com.yhacks.Person> doInBackground(Object... params) {
            FaceServiceClient faceServiceClient = new FaceServiceClient("15c325ffcb5240519f8d2bc1da1540ba");
            try {
                /*
                boolean containsIdentifyGroup = false;
                boolean containsIdentifiedGroup = false;
                boolean containsLinkedGroup = false;
                for (PersonGroup personGroup : faceServiceClient.getPersonGroups()) {
                    if (personGroup.personGroupId.equals("identify")) {
                        containsIdentifyGroup = true;
                    } else if (personGroup.personGroupId.equals("identified")) {
                        containsIdentifiedGroup = true;
                    } else if (personGroup.personGroupId.equals("linked")) {
                        containsLinkedGroup = true;
                    }
                }
                if (!containsIdentifyGroup) {
                    publishProgress("No faces marked using Augmented Reality Headset...");
                    return null;
                }
                if (!containsIdentifiedGroup) {
                    faceServiceClient.createPersonGroup("identified", "identified", "");
                }
                if (!containsLinkedGroup) {
                    faceServiceClient.createPersonGroup("linked", "linked", "");
                }

                com.microsoft.projectoxford.face.contract.Person person = faceServiceClient.getPerson(groupId, personId);
                UUID[] faceIds = person.faceIds;
*/
                ArrayList<rsen.com.yhacks.Person> listData = new ArrayList<>();
/*
                for (UUID faceId : faceIds)
                {
                    PersonFace face = faceServiceClient.getPersonFace(groupId, personId, faceId);
                    Log.d("ade", "added: " + listData.size());
                    listData.add(new rsen.com.yhacks.Person(face.userData.split(",")[1], "http://i.imgur.com/" + face.userData.split(",")[0] + ".png", -1, null));
                }
                */
                return listData;
            } catch (Exception e) {
                publishProgress(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(DetailActivity.this, values[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(ArrayList<rsen.com.yhacks.Person> result) {
            if (result != null) {

                RVAdapter adapter = new RVAdapter(result);
                rv.setAdapter(adapter);


            }
            progress.setVisibility(View.GONE);
        }
    }
}
