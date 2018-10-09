# MontiArcAutomaton Generics Demo
This is a minimal running example to show the bug I am encountering in MontiArcAutomaton with generics

## How to run this example:
`mvn clean compile` will work. `mvn test` will show the errors. In IntelliJ, you can also right-click > Run 'test()' on `demo.DemoTest` in src/test.

## Problem description
I have a component with a variable of type `Pair<int,boolean>`. When the typecheck tries to determine the type of this variable, it fails.

This is the model:
```
package test;

import java.lang.Pair;

component Test {

  port
    in boolean i,
    out int o;

  automaton TestAut {
    variable Pair<int,boolean> c;

    state A;
    initial A;

    // These brake
    A [c.fst > 1];
    A [c.snd];

    // This works
    A [i];
  }
}
```

And this is `Pair.java`:
```
package java.lang;
public class Pair<T,K> extends Object {
    public T fst;
    public K snd;
}
```
