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