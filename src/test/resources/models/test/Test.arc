package test;

import java.lang.Pair;

component Test {

  port
    in boolean i,
    out int o;

  Pair<int,boolean> c;

  automaton TestAut {
    state A;
    initial A;

    // These break
    A [c.fst > 1];
    A [c.snd];

    // This works
    A [i];
  }
}