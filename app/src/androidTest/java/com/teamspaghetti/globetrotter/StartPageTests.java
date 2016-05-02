package com.teamspaghetti.globetrotter;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.teamspaghetti.globetrotter.userprocesses.WelcomePage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Salih on 2.05.2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class StartPageTests {

    //Espresso testing
    @Rule
    public ActivityTestRule<WelcomePage> welcomepage = new ActivityTestRule<>(WelcomePage.class);
    /*
    * Opens register activity and tries to register
    * */
    @Test
    public void welcomepageregister(){
            onView(withId(R.id.toregister)).perform(click());
            onView(withId(R.id.input_email)).perform(typeText("username@username.com"),closeSoftKeyboard());
            onView(withId(R.id.input_username)).perform(typeText("username"),closeSoftKeyboard());
            onView(withId(R.id.input_password)).perform(typeText("username"),closeSoftKeyboard());
            onView(withId(R.id.input_password2)).perform(typeText("username"),closeSoftKeyboard());
            onView(withId(R.id.input_nameandsurname)).perform(typeText("username"),closeSoftKeyboard());
            onView(withId(R.id.registerrequest)).perform(click());
    }
    /*
    * Opens login activity and tries to login
    * */
    @Test
    public void welcomepagelogin(){
        onView(withId(R.id.tologin)).perform(click());
        onView(withId(R.id.input_email)).perform(typeText("username@username.com"),closeSoftKeyboard());
        onView(withId(R.id.input_password)).perform(typeText("username"),closeSoftKeyboard());
        onView(withId(R.id.loginrequest)).perform(click());
    }
}
