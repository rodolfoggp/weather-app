package rodolfogusson.weatherapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearcheableActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<String> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searcheable);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        list.add("Vitoria, BR");
        list.add("Prague, CZ");
        list.add("New York, US");

        adapter = new RecyclerViewAdapter(this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        /*// Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<String> filteredList = filter(list, newText);
                adapter = new RecyclerViewAdapter(SearcheableActivity.this,filteredList);
                recyclerView.swapAdapter(adapter, false);
                recyclerView.scrollToPosition(0);
                return false;
            }
        });
        return true;
    }

    private static List<String> filter(List<String> dataList, String query) {
        final String lowerCaseQuery = query.toLowerCase();
        final List<String> filteredList = new ArrayList<>();
        for (String text : dataList) {
            String lowerCaseText = text.toLowerCase();
            if (lowerCaseText.contains(lowerCaseQuery)) {
                filteredList.add(text);
            }
        }
        return filteredList;
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
        List<String> values;
        Context context;
        View view1;
        ViewHolder viewHolder1;

        RecyclerViewAdapter(Context context1, List<String> values1){
            values = values1;
            context = context1;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            ViewHolder(View v){
                super(v);
                textView = (TextView)v.findViewById(R.id.text1);
            }
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            view1 = LayoutInflater.from(context).inflate(R.layout.simple_search_item,parent,false);
            viewHolder1 = new ViewHolder(view1);
            return viewHolder1;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position){
            holder.textView.setText(values.get(position));
            holder.textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SearcheableActivity.this);
                    prefs.edit().putString(getString(R.string.key_location),
                            ((TextView)v).getText().toString()).apply();
                }
            });
        }

        @Override
        public int getItemCount(){
            return values.size();
        }
    }
}
