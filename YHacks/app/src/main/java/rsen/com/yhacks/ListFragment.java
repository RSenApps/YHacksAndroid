package rsen.com.yhacks;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.*;
import com.microsoft.projectoxford.face.contract.Person;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    String groupId = "";
    RecyclerView rv;
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayout progress;
    Firebase firebase;
    List<FaceData> faceData;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(String groupId) {

        Bundle args = new Bundle();
        args.putString("groupId", groupId);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = new Firebase("https://socialeyes.firebaseio.com/");
        groupId = getArguments().getString("groupId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.rv);
        progress = (LinearLayout) rootView.findViewById(R.id.progress);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        if(groupId.equals("identified")) {
            firebase.child("data").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    progress.setVisibility(View.VISIBLE);
                    String[] faceIdDatas = snapshot.getValue(String.class).split("\\|");
                    faceData = new ArrayList<FaceData>();
                    List<UUID> faceIdList = new ArrayList<UUID>();
                    if(faceIdDatas.length > 1) {

                        for (String faceIdData : faceIdDatas) {
                            Log.d("arte", faceIdData);
                            String[] faceIdInfo = faceIdData.split("#");

                            FaceData fd = new FaceData(faceIdInfo[0], "http://i.imgur.com/" + faceIdInfo[1] + ".png", faceIdInfo[2], faceIdInfo[3], faceIdInfo[4]);
                            if (!fd.identified) {
                                faceData.add(fd);
                                faceIdList.add(UUID.fromString(fd.faceId));
                            }
                        }
                    }
                    UUID[] faceIdArray = new UUID[faceIdList.size()];

                    new GroupingTask().execute(faceIdList.toArray(faceIdArray));
                }
                @Override
                public void onCancelled(FirebaseError error) { }
            });

        }
        else {
            firebase.child("linked").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progress.setVisibility(View.VISIBLE);
                    ArrayList<rsen.com.yhacks.Person> listData = new ArrayList<>();
                    String[] peopleDatas = dataSnapshot.getValue(String.class).split("\\|");
                    if(peopleDatas.length > 1) {
                        for (String peopleData : peopleDatas) {
                            String[] peopleInfo = peopleData.split("#");
                            final LinkedPerson lp = new LinkedPerson(peopleInfo[0], peopleInfo[1], peopleInfo[2], peopleInfo[3], peopleInfo[4]);
                            listData.add(new rsen.com.yhacks.Person(lp.name, lp.thumbURL, -1, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(getActivity(), DetailActivity.class);
                                    i.putExtra("groupId", groupId);
                                    i.putExtra("personId", lp.personId);
                                    i.putExtra("name", lp.name);
                                    i.putExtra("userData", lp.userData);
                                    startActivity(i);
                                }
                            }));

                        }
                    }
                    RVAdapter adapter = new RVAdapter(listData);
                    rv.setAdapter(adapter);
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }



        //fetch data
        /*
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
        */
        return rootView;
    }
    class GroupingTask extends AsyncTask<UUID, String, List<List<UUID>>> {
        @Override
        protected List<List<UUID>> doInBackground(UUID... object) {
            FaceServiceClient faceServiceClient = new FaceServiceClient("15c325ffcb5240519f8d2bc1da1540ba");
            try{
                /*
                boolean containsIdentifyGroup = false;
                boolean containsIdentifiedGroup = false;
                boolean containsLinkedGroup = false;
                for (PersonGroup personGroup : faceServiceClient.getPersonGroups())
                {
                    if (personGroup.personGroupId.equals("identify"))
                    {
                        containsIdentifyGroup = true;
                    }
                    else if (personGroup.personGroupId.equals("identified"))
                    {
                        containsIdentifiedGroup = true;
                    }
                    else if (personGroup.personGroupId.equals("linked")) {
                        containsLinkedGroup = true;
                    }
                }
                if (!containsIdentifyGroup)
                {
                    publishProgress("No faces marked using Augmented Reality Headset2...");
                    return null;
                }
                if (!containsIdentifiedGroup)
                {
                    faceServiceClient.createPersonGroup("identified", "identified", "");
                }
                if (!containsLinkedGroup)
                {
                    faceServiceClient.createPersonGroup("linked", "linked", "");
                }
                */
                /*
                com.microsoft.projectoxford.face.contract.Person[] persons = faceServiceClient.getPersons("identify");
                UUID[] faces = new UUID[persons.length];
                int counter = 0;
                for (Person person : persons)
                {
                    faces[counter] = person.faceIds[0];
                    counter++;
                }
                // Start grouping, params are face IDs.
                if (faces.length < 1)
                {
                    publishProgress("No faces marked using Augmented Reality Headset...");
                    return null;
                }
                */
                GroupResult result = faceServiceClient.group(object);

                Log.d("ade", "messygroup length" + result.messyGroup.size() + " number groups:" + result.groups.size());
                /*
                for(Person person : persons)
                {
                    if (!result.messyGroup.contains(person.faceIds[0])) {
                        faceServiceClient.deletePerson("identify", person.personId);
                    }
                }

                for (List<UUID> faceIds : result.groups)
                {
                    if (faceIds.size()>0) {
                        for (Person person : persons) {
                            if (faceIds.contains(person.faceIds[0])) {
                                try {
                                    faceIds = faceIds.subList(32, faceIds.size());
                                }
                                catch (Exception e)
                                {}
                                UUID[] faceIdArray = new UUID[faceIds.size()];
                                faceIdArray = faceIds.toArray(faceIdArray);
                                faceServiceClient.createPerson("identified", faceIdArray, "Unidentified", person.name);

                            }
                        }
                    }
                }
                */
                if (result.groups.size() > 0) {
                    return result.groups;
                }
            }  catch (Exception e) {
                publishProgress("Sample set is small enough that grouping was not necessary");
            }
            List<List<UUID>> returnObj = new ArrayList<>();
            for (UUID uuid : object)
            {
                List<UUID> innerList = new ArrayList<>();
                innerList.add(uuid);
                returnObj.add(innerList);
            }
            return returnObj;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity(), values[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(List<List<UUID>> result) {
            if (result != null) {
                Log.d("ade", "finished group");
                ArrayList<rsen.com.yhacks.Person> listData = new ArrayList<>();
                for (List<UUID> faceIds : result)
                {
                    String imageUrl = "";
                    int imageCount = 0;
                    for (FaceData face : faceData) {
                        if (faceIds.contains(UUID.fromString(face.faceId))) {
                            imageUrl = face.imageUrl;
                            imageCount++;
                        }
                    }
                    listData.add(new rsen.com.yhacks.Person("Unidentified", imageUrl, imageCount, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), DetailActivity.class);
                            i.putExtra("groupId", groupId);
                            i.putExtra("name", "Unidentified");
                            i.putExtra("userData", "Link to Facebook to add info");
                            startActivity(i);
                        }
                    }));

                }
                RVAdapter adapter = new RVAdapter(listData);
                rv.setAdapter(adapter);
            }


            progress.setVisibility(View.GONE);

        }
    }
    /*
    private void fetchData() {
        new FetchDataTask().execute();

    }
    class FetchDataTask extends AsyncTask<Object, String, ArrayList<rsen.com.yhacks.Person>> {
        @Override
        protected ArrayList<rsen.com.yhacks.Person> doInBackground(Object... params) {
            FaceServiceClient faceServiceClient = new FaceServiceClient("15c325ffcb5240519f8d2bc1da1540ba");
            try {
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
                com.microsoft.projectoxford.face.contract.Person[] persons = faceServiceClient.getPersons(groupId);
                ArrayList<rsen.com.yhacks.Person> listData = new ArrayList<>();
                Log.d("ade", "fetched persons: " + groupId + persons.length);

                for (final Person person : persons)
                {
                    Log.d("ade", "added: " + listData.size());
                    //faceServiceClient.deletePerson("identified", person.personId);
                    listData.add(new rsen.com.yhacks.Person(person.name, "http://i.imgur.com/" + person.userData.split(",")[0] + ".png", person.faceIds.length, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), DetailActivity.class);
                            i.putExtra("groupId", groupId);
                            i.putExtra("personId", person.personId);
                            i.putExtra("name", person.name);
                            i.putExtra("userData", person.userData);
                            startActivity(i);
                        }
                    }));
                }
                return listData;
            } catch (Exception e) {
                publishProgress(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getActivity(), values[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(ArrayList<rsen.com.yhacks.Person> result) {
            if (result != null) {

                RVAdapter adapter = new RVAdapter(result);
                rv.setAdapter(adapter);


            }
            progress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

        }
    }
    */
}
