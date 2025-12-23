package com.example.calculator;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CalculatorUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testBackspaceFunctionality() {
        onView(withId(R.id.btn1)).perform(click());
        onView(withId(R.id.btn2)).perform(click());
        onView(withId(R.id.btn3)).perform(click());

        onView(withId(R.id.btnBackspace)).perform(click());
        onView(withId(R.id.txtDisplay)).check(matches(withText("12")));

        onView(withId(R.id.btnBackspace)).perform(click());
        onView(withId(R.id.txtDisplay)).check(matches(withText("1")));
    }

    @Test
    public void testSignToggle() {
        onView(withId(R.id.btn5)).perform(click());
        onView(withId(R.id.btnSign)).perform(click());
        onView(withId(R.id.txtDisplay)).check(matches(withText("-5")));

        onView(withId(R.id.btnSign)).perform(click());
        onView(withId(R.id.txtDisplay)).check(matches(withText("5")));
    }

    @Test
    public void testDivisionByZeroErrorDisplay() {
        onView(withId(R.id.btn5)).perform(click());
        onView(withId(R.id.btnDivide)).perform(click());
        onView(withId(R.id.btn0)).perform(click());
        onView(withId(R.id.btnEquals)).perform(click());

        onView(withId(R.id.txtDisplay)).check(matches(withText("Error")));
    }

    @Test
    public void testChainedCalculation() {
        onView(withId(R.id.btn2)).perform(click());
        onView(withId(R.id.btnPlus)).perform(click());
        onView(withId(R.id.btn2)).perform(click());
        onView(withId(R.id.btnEquals)).perform(click());

        onView(withId(R.id.btnPlus)).perform(click());
        onView(withId(R.id.btn3)).perform(click());
        onView(withId(R.id.btnEquals)).perform(click());

        onView(withId(R.id.txtDisplay)).check(matches(withText("7")));
    }
}