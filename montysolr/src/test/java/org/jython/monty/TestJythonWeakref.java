package org.jython.monty;

import com.carrotsearch.randomizedtesting.JUnit4MethodProvider;
import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.carrotsearch.randomizedtesting.annotations.*;
import com.carrotsearch.randomizedtesting.annotations.ThreadLeakAction.Action;
import com.carrotsearch.randomizedtesting.annotations.ThreadLeakGroup.Group;
import com.carrotsearch.randomizedtesting.annotations.ThreadLeakScope.Scope;
import com.carrotsearch.randomizedtesting.annotations.ThreadLeakZombies.Consequence;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.python.util.PythonInterpreter;


@RunWith(RandomizedRunner.class)
@TestMethodProviders({
        JUnit4MethodProvider.class
})
@ThreadLeakScope(Scope.SUITE)
@ThreadLeakGroup(Group.MAIN)
@ThreadLeakAction({Action.WARN, Action.INTERRUPT})
@ThreadLeakLingering(linger = 2000) // Wait long for leaked threads to complete before failure. zk needs this.
@ThreadLeakZombies(Consequence.CONTINUE)
@TimeoutSuite(millis = 2 * 3600)
public class TestJythonWeakref extends Assert {

    @Ignore
    @Test
    public void test() {
        PythonInterpreter interp = new PythonInterpreter();
        interp.exec("import weakref");

    }

}
