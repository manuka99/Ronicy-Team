package com.adeasy.advertise;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.adeasy.advertise.ui.getintouch.AboutUsActivity;
import com.adeasy.advertise.ui.getintouch.GetInTouchActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GetInTouchIntentTesting {
    private static final String PACKAGE_NAME  ="com.adeasy.advertise.ui.getintouch";

    @Rule
    public ActivityScenarioRule<GetInTouchActivity> activityRule = new ActivityScenarioRule<>(GetInTouchActivity.class);

    //@Rule
    //public IntentsTestRule<GetInTouchActivity> intentsTestRule = new IntentsTestRule<>(GetInTouchActivity.class);
    @Before
    public void setup()
    {
        Intents.init();
    }



    @Test
    public void verifyIntent()
    {
        onView(withId(R.id.contactUsBtn)).perform(click());

        /*intended(allOf(
                hasComponent(hasShortClassName(AboutUsActivity.class.getName())),toPackage(PACKAGE_NAME)
        ));*/

        intended(hasComponent(AboutUsActivity.class.getName()));


        //activityRule.launchActivity(new Intent());
        //intended(hasComponent(AboutUsActivity.class.getName()));
    }

    @After
    public void tearDown()
    {
        Intents.release();
    }

}
