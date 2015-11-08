package rsen.com.yhacks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.GroupResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchUser extends AppCompatActivity {

    String groupId;
    RecyclerView rv;
    View progress;
    String[] faceIds;
    String name;
    String thumbURL;
    String userData;
    Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://socialeyes.firebaseio.com/");

        thumbURL = getIntent().getStringExtra("thumbURL");
        name = getIntent().getStringExtra("name");
        faceIds = getIntent().getStringArrayExtra("faceIds");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = (RecyclerView) findViewById(R.id.rv);
        progress = findViewById(R.id.progress);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        FacebookSdk.sdkInitialize(getApplicationContext());
        final AccessToken token = AccessToken.getCurrentAccessToken();
        progress.setVisibility(View.GONE);

        if (token == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {

            findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progress.setVisibility(View.VISIBLE);
                    final String search = ((EditText)findViewById(R.id.search)).getText().toString();
                    GraphRequest request =GraphRequest.newMyFriendsRequest(
                            token,
                            new GraphRequest.GraphJSONArrayCallback() {
                                @Override
                                public void onCompleted(
                                        JSONArray jsonArray,
                                        GraphResponse response) {
                                    Log.d("afef", response.getRawResponse());
                                    try {
                                        final JSONArray users = response.getJSONObject().getJSONArray("data");
                                        ArrayList<rsen.com.yhacks.Person> listData = new ArrayList<>();
                                        for (int i = 0; i < users.length(); i++) {
                                            final String name = users.getJSONObject(i).getString("name");
                                            String id = users.getJSONObject(i).getString("id");
                                            //String link = users.getJSONObject(i).getString("link");
                                            final String birthday = users.getJSONObject(i).getString("birthday");
                                            final String hometown = users.getJSONObject(i).getJSONObject("hometown").getString("name");
                                            SearchUser.this.name = name;
                                            if (name.startsWith(search)) {
                                                listData.add(new Person(name, "", hometown, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        userData= birthday + "; " + hometown;
                                                        new SaveUser().execute(birthday + "; " + hometown);

                                                    }
                                                }));



                                            }

                                        }
                                        RVAdapter adapter = new RVAdapter(listData);
                                        rv.setAdapter(adapter);
                                        progress.setVisibility(View.GONE);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,link,birthday,hometown,work,education");
                    request.setParameters(parameters);
                    request.executeAsync();
                }


                        /*
                        GraphRequest request = GraphRequest.newGraphPathRequest(token, "search", new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                try {
                                    Log.d("afef", response.getRawResponse());

                                    JSONArray users = response.getJSONObject().getJSONArray("data");
                                    for (int i = 0; i < users.length(); i++) {
                                        String name = users.getJSONObject(i).getString("name");
                                        String id = users.getJSONObject(i).getString("id");
                                        new GraphRequest(
                                                token,
                                                "/" + id + "/picture?redirect=0",
                                                null,
                                                HttpMethod.GET,
                                                new GraphRequest.Callback() {
                                                    public void onCompleted(GraphResponse response) {
                                                        Log.d("afef", response.getRawResponse());

                                                    }
                                                }
                                        ).executeAsync();
                                        Log.d("afef", name + id);

                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("q", );
                        parameters.putString("type", "user");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }
                    */
            });


        }
            /*
            ArrayList<Person> listData = new ArrayList<>();
            int counter = 0;
            for (String imageURL : imageURLs)
            {
                listData.add(new Person(imageTimes[counter], imageURL, -1, null));
                counter++;
            }

            RVAdapter adapter = new RVAdapter(listData);
            rv.setAdapter(adapter);
            progress.setVisibility(View.GONE);
            */
    }

    class SaveUser extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... object) {
            FaceServiceClient faceServiceClient = new FaceServiceClient("15c325ffcb5240519f8d2bc1da1540ba");
            UUID[] uuids = new UUID[faceIds.length];
            int counter = 0;
            for (String faceId : faceIds) {
                uuids[counter] = UUID.fromString(faceId);
                counter++;
            }
            try {
                String personId = faceServiceClient.createPerson("linked", uuids, name, object[0]).personId.toString(); //add userdata
                faceServiceClient.trainPersonGroup("linked");
                return personId;
            } catch (Exception e) {
                publishProgress("Sample set is small enough that grouping was not necessary");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }

        @Override
        protected void onPostExecute(final String personId) {
            final ValueEventListener dataListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    firebase.child("linked").setValue(dataSnapshot.getValue(String.class)
                            + "|" + personId + "#" + name + "#" + " " + "#" + userData + "#" + thumbURL);
                    finish();

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };
            firebase.child("linked").addListenerForSingleValueEvent(dataListener);
        }
    }
}


