# Algo: Implementation comparer

[![Build Status][1]][2]
[![Coverage Status][3]][4]

This project provides an easy way to compare the performances of several implementations of a
method.

## What will I get from it?

When using the comparison algorithm and logger, you will obtain this kind of output:

```
o.k.c.c.ImplComparer - Beginning performance comparison for method <hello>, (3 check(s), 10000 iteration(s) per check
o.k.c.c.ImplComparisonLogger - +--------+--------------------+--------+
o.k.c.c.ImplComparisonLogger - | Method | Avg time (ms)      | Result |
o.k.c.c.ImplComparisonLogger - +--------+--------------------+--------+
o.k.c.c.ImplComparisonLogger - | hello  | 3.6887             |    REF |
o.k.c.c.ImplComparisonLogger - | hello1 | 2.7880666666666665 | == REF |
o.k.c.c.ImplComparisonLogger - | hello2 | 1.9368             | == REF |
o.k.c.c.ImplComparisonLogger - +--------+--------------------+--------+
```

The columns are the following:

1. **Method**: the name of the method being evaluated;
2. **Avg time (ms)**: the average execution time during the verification, in milliseconds;
3. **Result**: a comparison of results:
  * ``REF``: for the reference (first) result, either if results both point to the same sector or both are ``void`` or ``null``;
  * ``== REF``: for a result equal to the reference result (using the ``.equals(Object)`` method);
  * ``!= REF``: for a result not equal to the reference result (using the ``.equals(Object)`` method).
  
## How to use?

### Create a test project

This is a suggestion of course, but it seems ill-advised to add the comparer as a dependency to
your project: it is only a programming tool.

You should add the project containing the class you wish to test as a dependency to your project.

### Adding the comparer to your dependencies

This project is _not_ available on any Maven repository. You must therefore:

1. clone this project in your own workspace;
2. install it to your local repository using the ``mvn install`` command;
3. add it as a dependency to your project:

  ```xml
  <dependency>
    <groupId>org.keyboardplaying</groupId>
    <artifactId>java-implementation-comparer</artifactId>
    <version>1.0.0</version>
  </dependency>
  ```

### Comparing and logging

#### Preparing variants

For instance, if I want to compare several implementations of the method
``public static String hello(String name)``, you will have to write methods with the
_exact same_ signature (return type, parameters) but adding a number at the end of the name of the
method, e.g. ``public static String hello1(String name)``.

The algorithm will search all methods with the same signature and increment the suffix until no more
variant is found.

### Perform the comparison

I personally tend to create a ``TestClass`` and make a ``main`` which execute the comparison.
Below is an example:

```java
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException,
            IllegalArgumentException {
        /* Prepare the comparer */
        ImplComparer cmp = new ImplComparer();
        /* Uncomment to override default settings */
        // cmp.setChecks(3);
        // cmp.setIterations(10000);

        List<ImplCheckResult> results = cmp.compareStatic(ToBeTested.class, "hello",
                new Class<?>[] { String.class }, new Object[] { "Chop" });
        /* Non-static method variant */
        // List<ImplCheckResult> results = cmp.compare(new ToBeTested(), "hello",
        // new Class<?>[] { String.class }, new Object[] { "Chop" });

        /* Print the result */
        ImplComparisonLogger logger = new ImplComparisonLogger();
        logger.log(results);
    }
```

[1]: http://img.shields.io/travis/cyChop/java-implementation-comparer/master.svg
[2]: https://travis-ci.org/cyChop/java-implementation-comparer
[3]: http://img.shields.io/coveralls/cyChop/java-implementation-comparer/master.svg
[4]: https://coveralls.io/r/cyChop/java-implementation-comparer?branch=master
