# MontiArcAutomaton Generics Demo
This is a minimal running example to show my problems with MAA and Generics

## How to run this example:
`mvn clean compile` will work. `mvn test` will show the errors. In IntelliJ, you can also right-click > Run 'test()' on `demo.DemoTest` in src/test.

## Problem description
I have a component with a variable of type `Pair<integer,boolean>`. When the typecheck tries to determine the type of this variable, it fails.
