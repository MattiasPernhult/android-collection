package com.fam.fam.bullshitapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements RecyclerAdapter.ClickListener {

    private Controller controller;
    private boolean hasInternet;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new Controller();

        toast = new Toast(this);

        hasInternet = controller.isOnline(this);
        if (!hasInternet) {
            toast.makeText(this, "In order to use this app, you must turn on internet.", Toast.LENGTH_LONG).show();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        RecyclerAdapter adapter = new RecyclerAdapter(this);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        NetworkChangeReceiver receiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                intent = new Intent(this, SpaceActivity.class);
                break;
            case 1:
                intent = new Intent(this, YodaActivity.class);
                break;
            case 2:
                intent = new Intent(this, GiphyActivity.class);
                break;
        }
        if (intent != null) {
            intent.putExtra("controller", controller);
            if (hasInternet) {
                startActivity(intent);
            } else {
                toast.cancel();
                toast.makeText(this, "To perform this action, the device must be connected to the internet.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (controller.isOnline(MainActivity.this)) {
                hasInternet = true;
            } else {
                hasInternet = false;
            }
        }
    }
}
