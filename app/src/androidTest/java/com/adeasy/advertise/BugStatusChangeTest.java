package com.adeasy.advertise;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.adeasy.advertise.ui.getintouch.ViewBugAdmin;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BugStatusChangeTest {

    private String stringToBechanged;

    @Rule
    public ActivityScenarioRule<ViewBugAdmin> activityRule = new ActivityScenarioRule<>(ViewBugAdmin.class);

    @Before
    public void initValidString()
    {
        stringToBechanged = "Acknowledged";
    }

    @Test
    public void changeText()
    {
        onView(withId(R.id.btnAcknowledge)).perform(click());
        onView(withId(R.id.TVstatus)).check(matches(withText(stringToBechanged)));
    }

}
