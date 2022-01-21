package com.zugazagoitia.spotifystalker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.zugazagoitia.spotifystalker.data.LoginRepository;
import com.zugazagoitia.spotifystalker.data.model.FriendList;
import com.zugazagoitia.spotifystalker.data.model.Profile;
import com.zugazagoitia.spotifystalker.data.model.RichProfile;
import com.zugazagoitia.spotifystalker.data.model.UserPlayingInfo;
import com.zugazagoitia.spotifystalker.ui.login.LoginActivity;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    private FirebaseAnalytics mFirebaseAnalytics;

    private List<UserPlayingInfo> beans;
    private ListbaseAdapter listbaseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        if(!LoginRepository.isLoggedIn()){
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }

        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);

        //listView.setDivider(AppCompatResources.getDrawable(this,R.drawable.divider));

        beans = new ArrayList<UserPlayingInfo>();

        updateFriendList();


        RichProfile user = LoginRepository.getUserProfile();

        TextView followers = findViewById(R.id.followers_value);
        TextView following = findViewById(R.id.following_value);
        TextView playlists = findViewById(R.id.playlists_value);
        TextView name = findViewById(R.id.main_name);
        TextView username = findViewById(R.id.main_username);

        ImageView profilePic = findViewById(R.id.main_profilepic);

        name.setText(user.getName());
        followers.setText(String.valueOf(user.getFollowersCount()));
        following.setText(String.valueOf(user.getFollowingCount()));
        playlists.setText(String.valueOf(user.getTotalPublicPlaylistsCount()));
        username.setText(LoginRepository.getUsername());

        Glide.with(this)
            .load(user.getImageUrl())
            .placeholder(R.drawable.resource_default)
            .into(profilePic);
    }


    public void openMainProfile(View v){
        openSpotifyUri(LoginRepository.getUserProfile().getUri());
    }

    /**
     * Open a spotify uri in the spotify app
     *
     * @param uri URL to open
     */
    void openSpotifyUri(String uri) {
        PackageManager pm = getPackageManager();

        boolean isSpotifyInstalled;
        try {
            pm.getPackageInfo("com.spotify.music", 0);
            isSpotifyInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isSpotifyInstalled = false;
        }
        if(isSpotifyInstalled) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Spotify is not installed!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void updateFriendList(View v){updateFriendList();}

    public void updateFriendList() {
        new Thread(() -> {

            String user = LoginRepository.getUsername();
            String token = LoginRepository.getToken();

            String url = MessageFormat.format("https://spclient.wg.spotify.com/user-profile-view/v3/profile/{0}/following/", user);

            OkHttpClient client = new OkHttpClient();

            //Call api using okhttp
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            try (Response response = client.newCall(request).execute()){


                //NEW CODE

                ObjectMapper objMapper = new ObjectMapper();

                String responseBody = response.body().string();
                //System.out.println(responseBody);
                FriendList friends = objMapper.readValue(responseBody, FriendList.class);


                ArrayList<UserPlayingInfo> friendsSongs = new ArrayList<>();

                Predicate<Profile> predicate = profile -> (profile.getFollowingCount() == 0 && profile.getFollowersCount() >= 10); //Verdadero si es "famoso"
                friends.setProfiles(friends.getProfiles()
                                            .stream()
                                            .filter(predicate.negate())
                                            .collect(Collectors.toList()));
                String url2 = "https://spclient.wg.spotify.com/presence-view/v1/user/{0}";
                CountDownLatch countDownLatch = new CountDownLatch(friends.getProfiles().size());
                for (Profile p :friends.getProfiles()) {

                    //System.out.println(p.getUri().split(":")[2]);
                    Request request2 = new Request.Builder()
                            .url(MessageFormat.format(url2, p.getUri().split(":")[2]))
                            .addHeader("Authorization", "Bearer " + token)
                            .build();

                    //Asynchronous call
                    client.newCall(request2).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            countDownLatch.countDown();
                        }
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseBody2 = response.body().string();
                            System.out.println(responseBody2);

                            try {
                                friendsSongs.add(objMapper.readValue(responseBody2, UserPlayingInfo.class));
                            } catch (MismatchedInputException e){
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }
                            countDownLatch.countDown();
                        }
                    });

                }

                countDownLatch.await();

                friendsSongs.sort((o1, o2) -> (int) (o2.getTimestamp()-o1.getTimestamp()));

                beans = friendsSongs;

                new Handler(Looper.getMainLooper()).post(() -> {
                    listbaseAdapter = new ListbaseAdapter(MainActivity.this, beans);
                    listView.setAdapter(listbaseAdapter);
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        ListbaseAdapter.ViewHolder vh = (ListbaseAdapter.ViewHolder) view.getTag();

                        // setup the alert builder
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                        String[] animals = {"User", "Album", "Track", "Context"};
                        builder.setItems(animals, (dialog, which) -> {
                            switch (which) {
                                case 0: // User
                                    openSpotifyUri(vh.userUri);
                                    break;
                                case 1: // Album
                                    openSpotifyUri(vh.albumUri);
                                    break;
                                case 2: // Track
                                    openSpotifyUri(vh.trackUri);
                                    break;
                                case 3: // Context
                                    openSpotifyUri(vh.contextUri);
                                    break;
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();


                    });
                });
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }


        }).start();
    }

    public static <T> List<T> parseJsonArray(String json,
                                             Class<T> classOnWhichArrayIsDefined)
            throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + classOnWhichArrayIsDefined.getName() + ";");
        T[] objects = mapper.readValue(json, arrayClass);
        return Arrays.asList(objects);
    }

}
