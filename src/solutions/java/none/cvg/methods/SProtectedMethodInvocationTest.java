package none.cvg.methods;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import none.cvg.DemoClass;
import org.junit.Test;

import static none.cvg.ErrorMessages.REFLECTION_FAILURE;
import static none.cvg.ErrorMessages.TEST_FAILURE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/*
 * DONE:
 *  This test aims at using MethodHandles to invoke a protected method on a class.
 *  Each solved test shows how this can be achieved with the traditional reflection calls.
 *  Each unsolved test provides a few hints that will allow the kata-taker to manually solve
 *  the exercise to achieve the same goal with MethodHandles.
 */
public class SProtectedMethodInvocationTest {

    @Test
    public void reflectionProtectedMethod() {

        String expectedOutput = "[DemoClass] - Protected method via reflection";

        try {

            // Cannot call getMethod(), use getDeclaredMethod() to get private and protected methods
            Method protectedMethod =
                    DemoClass.class.getDeclaredMethod("protectedMethod",
                            String.class);

            /*
             * Method has to be made accessible.
             * Not setting this will cause IllegalAccessException
             * Setting accessible to true causes the JVM to skip access control checks
             *
             * This is needed to access non-public content
             */
            protectedMethod.setAccessible(true);

            DemoClass demoClass = new DemoClass();

            assertEquals("Reflection invocation failed",
                    expectedOutput,
                    protectedMethod.invoke(demoClass, "via reflection"));

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {

            fail(REFLECTION_FAILURE.getValue() + e.getMessage());
        }
    }

    @Test
    public void methodHandleProtectedMethod() {

        String expectedOutput = "[DemoClass] - Protected method via Method Handles";

        /*
         * DONE:
         *  Get a non-public lookup from java.lang.invoke.MethodHandles
         *  Non-public methods are looked up via "lookup"
         *  Find the differences between lookup() and publicLookup() on MethodHandles.
         *  Check API: java.lang.invoke.MethodHandles.lookup()
         */
        MethodHandles.Lookup protectedMethodHandlesLookup = MethodHandles.lookup();

        try {

            /*
             * DONE:
             *  Replace the "null"s valid values to create a protectedMethod instance.
             *  Create a method instance that matches the name "protectedMethod()"
             *  See java.lang.class for a getDeclaredMethod()
             *  Look for the declared method on the DemoClass.class
             *  The protectedMethod() accepts a String input parameter.
             *  Same call as is used in vanilla reflection.
             *  Check API: java.lang.Class.getDeclaredMethod(?, ?)
             */
            Method protectedMethod =
                    DemoClass.class.getDeclaredMethod("protectedMethod",
                            String.class); // Method name, Class ... = Parameter types

            /*
             * Method has to be made accessible.
             * Not setting this will cause IllegalAccessException
             * Setting accessible to true causes the JVM to skip access control checks
             *
             * This is needed to access non-public content
             */
            protectedMethod.setAccessible(true);

            /*
             * DONE:
             *  Replace the null with the method instance to produce a protectedMethod handle.
             *  Unreflect to create a method handle from a method instance
             *  Generate a MethodHandle from the method instance created earlier.
             *  See unreflect method in the innerclass Lookup of MethodHandles.
             *  java.lang.invoke.MethodHandles.Lookup.unreflect()
             *  We can unreflect the method since we bypassed the access control checks above.
             *  Check API: java.lang.invoke.MethodHandles.Lookup.unreflect(?)
             */
            MethodHandle protectedMethodHandle =
                    protectedMethodHandlesLookup.unreflect(protectedMethod);

            DemoClass demoClass = new DemoClass();

            assertEquals("Method handles invocation failed",
                    expectedOutput,
                    protectedMethodHandle.invoke(demoClass,
                            "via Method Handles"));

        } catch (NoSuchMethodException | IllegalAccessException e) {

            fail("Failed to execute a protected method invocation via Method Handles: "
                    + e.getMessage());
        } catch (Throwable t) {

            // invoke throws a Throwable (hence catching Throwable separately).
            fail(TEST_FAILURE.getValue() + t.getMessage());
        }
    }
}