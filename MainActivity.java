package com.example.user.listviewcontacts;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    UserListAdapter adapter;
    ListView listView;
    ArrayList<User> myusers = new ArrayList<>();
    ArrayList<User> sorted = new ArrayList<>();

    protected ArrayList<User> OnlyMen(ArrayList<User> users){
        ArrayList<User> men = new ArrayList<>();
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).sex == Sex.MAN)
                men.add(users.get(i));
        }
        return men;
    }

    protected ArrayList<User> OnlyWomen(ArrayList<User> users){
        ArrayList<User> women = new ArrayList<>();
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).sex == Sex.WOMAN)
                women.add(users.get(i));
        }
        return women;
    }

    protected ArrayList<User> OnlyUfo(ArrayList<User> users){
        ArrayList<User> ufo = new ArrayList<>();
        for (int i = 0; i < users.size(); i++){
            if (users.get(i).sex == Sex.UFO)
                ufo.add(users.get(i));
        }
        return ufo;
    }

    protected ArrayList<User> sortByInc(ArrayList<User> users){
        ArrayList<User> sorted_by_inc = new ArrayList<>();
        HashMap<String, Integer> names_users = new HashMap<>();
        for (int i = 0; i < users.size(); i++){
            names_users.put(users.get(i).name, i);
        }
        TreeMap<String, Integer> sorted = new TreeMap<>();

        sorted.putAll(names_users);
        for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            sorted_by_inc.add(users.get(entry.getValue()));
        }

        return sorted_by_inc;
    }

    protected ArrayList<User> sortByDes(ArrayList<User> users){
        ArrayList<User> sorted_by_des = new ArrayList<>();
        ArrayList<User> sorted_by_inc = new ArrayList<>();
        sorted_by_inc = sortByInc(users);
        for (int i = sorted_by_inc.size() - 1; i >= 0; i--){
            sorted_by_des.add(sorted_by_inc.get(i));
        }
        return sorted_by_des;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);

        AssetManager resources = getResources().getAssets();
        try(InputStreamReader book = new InputStreamReader(resources.open("book.json"))){
            BufferedReader mybook = new BufferedReader(book);
            String text_in_book = "";
            String line = "";
            while ((line = mybook.readLine()) != null){
                text_in_book += line;
            }
            String users[] = text_in_book.split("\"user\":");
            for (int i = 1; i < users.length; i++){
                Gson contacts = new Gson();
                users[i] = users[i].trim();
                String user = users[i].substring(0, users[i].length() - 1);
                Contact contact = contacts.fromJson(user, Contact.class);
                Sex sex;
                switch (contact.sex){
                    case "MAN":
                        sex = Sex.MAN;
                        break;
                    case "WOMAN":
                        sex = Sex.WOMAN;
                        break;
                    default:
                        sex = Sex.UFO;
                }
                myusers.add(new User(contact.name, contact.phone, sex));
                sorted.add(new User(contact.name, contact.phone, sex));
            }
            adapter = new UserListAdapter(this, myusers);
            listView.setAdapter(adapter);
        }
        catch (IOException e){ }
    }

    public void onClick(View v){
        if (v.getId() == R.id.findsexbutton) {
            Spinner spinner = findViewById(R.id.findsex);
            String selected = spinner.getSelectedItem().toString();
            switch (selected) {
                case "Men":
                    myusers.clear();
                    myusers.addAll(OnlyMen(sorted));
                    break;

                case "Women":
                    myusers.clear();
                    myusers.addAll(OnlyWomen(sorted));
                    break;

                case "UFO":
                    myusers.clear();
                    myusers.addAll(OnlyUfo(sorted));
                    break;

                default:
                    myusers.clear();
                    myusers.addAll(sorted);
            }
        }

        if (v.getId() == R.id.sortByInc) {
            ArrayList<User> tus = new ArrayList<>();
            tus.addAll(myusers);
            myusers.clear();
            myusers.addAll(sortByInc(tus));
        }

        if (v.getId() == R.id.sortByDes) {
            ArrayList<User> tus = new ArrayList<>();
            tus.addAll(myusers);
            myusers.clear();
            myusers.addAll(sortByDes(tus));
        }

        adapter.notifyDataSetChanged();
    }
}

