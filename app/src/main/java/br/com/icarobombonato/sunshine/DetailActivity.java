package br.com.icarobombonato.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
        String mForecastMessage;
        ShareActionProvider mShareActionProvider;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();

            if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
                mForecastMessage = intent.getStringExtra(Intent.EXTRA_TEXT);

                TextView weatherTextView = (TextView) rootView.findViewById(R.id.weather_extra_textview);

                weatherTextView.setText(mForecastMessage);
            }

            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
            inflater.inflate(R.menu.detailfragment, menu);

            // Get the menu item.
            MenuItem menuItem = menu.findItem(R.id.action_share);
            // Get the provider and hold onto it to set/change the share intent.
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            if (mShareActionProvider != null ) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }

        // Somewhere in the application.
        public Intent createShareForecastIntent() {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastMessage + FORECAST_SHARE_HASHTAG);
            
            // When you want to share set the share intent.
            return shareIntent;
        }
    }
}
