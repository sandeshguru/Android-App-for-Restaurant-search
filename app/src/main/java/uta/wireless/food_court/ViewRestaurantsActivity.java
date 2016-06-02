package uta.wireless.food_court;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.ViewFlipper;


public class ViewRestaurantsActivity extends ActionBarActivity {

    ListView listview;
    ViewFlipper viewFlipper;
    float lastX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(uta.wireless.food_court.R.layout.activity_view_restaurants);
        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabSpec spec1=tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Suggestions");

        TabSpec spec2=tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("Favourites");
        spec2.setContent(R.id.tab2);


        tabHost.addTab(spec1);
        tabHost.addTab(spec2);



        String deviceUID = getIntent().getStringExtra("sid");
        String[] fav = new String[]{"Original RecipeChicken",
                "Double Down",
                "Boneless Wings",
                "PopCorn",
                "Extra Cripsy Strips",
                "Bone-in-wings",
                "Corn on the cob",
                "Rice Bowl"

        };
        String[] fav1 = new String[]{"Orange chicken",
                "Sweet fire chicken Breast",
                "Mushroom Chicken",
                "Beijing Beef",
                "Chow mein Fried Rice",
                "Veggie Spring Roll",
                "Chicken PotSticker",
                "Crispy Shrimp"

        };
        String[] fav2 = new String[]{"French Fries",
                "Big Mac",
                "Snack Wrap",
                "Happy Meal",
                "Egg Mcmuffins",
                "Apple dippers",
                "Chicken Mcnuggets",
                "Premium salads",
                "Double Cheese Burger"

        };
//
        listview= (ListView)findViewById(uta.wireless.food_court.R.id.viewRestaurants);
//
        ImageView image = (ImageView) findViewById(R.id.imageView);
        ImageView image1 = (ImageView) findViewById(R.id.imageView5);
        ImageView image2 = (ImageView) findViewById(R.id.imageView2);
        ImageView image3 = (ImageView) findViewById(R.id.imageView3);
        ImageView image4 = (ImageView) findViewById(R.id.imageView4);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        if (deviceUID.equals("00:00:00:00:00:00")) {
            image.setImageResource(uta.wireless.food_court.R.drawable.kfc1);
            image1.setImageResource(uta.wireless.food_court.R.drawable.kfc2);
            image2.setImageResource(uta.wireless.food_court.R.drawable.kfc3);
            image3.setImageResource(uta.wireless.food_court.R.drawable.kfc4);
            image4.setImageResource(uta.wireless.food_court.R.drawable.kfc5);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, fav);
            listview.setAdapter(adapter);
        }
        if (deviceUID.equals("00:00:00:00:00:00")) {
            image.setImageResource(uta.wireless.food_court.R.drawable.panda1);
            image1.setImageResource(uta.wireless.food_court.R.drawable.panda2);
            image2.setImageResource(uta.wireless.food_court.R.drawable.panda3);
            image3.setImageResource(uta.wireless.food_court.R.drawable.panda4);
            image4.setImageResource(uta.wireless.food_court.R.drawable.panda5);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, fav1);
            listview.setAdapter(adapter);
        }
        if (deviceUID.equals("00:00:00:00:00:00")) {
            image.setImageResource(uta.wireless.food_court.R.drawable.mcd1);
            image1.setImageResource(uta.wireless.food_court.R.drawable.mcd2);
            image2.setImageResource(uta.wireless.food_court.R.drawable.mcd3);
            image3.setImageResource(uta.wireless.food_court.R.drawable.mcd4);
            image4.setImageResource(uta.wireless.food_court.R.drawable.mcd5);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, fav2);
            listview.setAdapter(adapter);
        }

    }

    public boolean onTouchEvent(MotionEvent touchevent) {

        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = touchevent.getX();
                break;

            case MotionEvent.ACTION_UP:
                float currentX = touchevent.getX();
                // Handling left to right screen swap.

                if (lastX < currentX) {

                    // If there aren't any other children, just break.
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;
                    // Next screen comes in from left.
                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
                    // Current screen goes out from right.
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);
                    // Display next screen.
                    viewFlipper.showNext();
                }

                // Handling right to left screen swap.

                if (lastX > currentX) {
                    // If there is a child (to the left), kust break.
                    if (viewFlipper.getDisplayedChild() == 1)
                        break;
                    // Next screen comes in from right.
                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
                    // Current screen goes out from left.
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
                    // Display previous screen.
                    viewFlipper.showPrevious();
                }
                break;

        }

        return false;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(uta.wireless.food_court.R.menu.menu_view_restaurants, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case uta.wireless.food_court.R.id.home:
                Intent intent = new Intent(this, FoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }
}

