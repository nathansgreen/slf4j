# Java 8 Support

Following is an explanation of how Java 8 support can be safely added
to `slf4j-api`.

1. (Premise) Lambdas are a desirable and/or suitable alternative to
direct use of the various _isXEnabled_ methods.
2. (Premise) Default methods on interfaces provide a path forward for
_all_ implementors of `slf4j-api`.
3. In principle, only `org.slf4j.Logger` must be compiled for bytecode
version 52: all other members of `slf4j-api` can remain in version 49
format. Whether this is desirable can be debated. Ideally, attempts to
use the API in an older version of Java should fail-fast, perhaps even
be directly checked for in `LoggerFactory`.
4. The current Maven dependency system must remain intact. It should
not be possible to accidentally include multiple copies of `slf4j-api`
due to renaming, classifiers, etc.
5. Compatibility with pre-Java 8 systems (e.g. Android) is possible
using a tool like [Retrolambda](https://github.com/orfjackal/retrolambda).
6. The default implementation of these methods can be made fully safe
to ensure compatibility in the face of exceptions, though possibly with
a performance penalty.
7. It is possible that there will be source incompatibility for cases
where the `null` keyword is used to call the `Logger` API. This should
be rare in practice, and can simply be mentioned in the release notes.

## Syntax examples

I opted to go with this:

```java
  logger.info("{}", ()->do(some(expensive(thing))));
```

instead of this:

```java
  logger.info(()->make(some(expensive(string))));
```

because in practice, the latter would probably end up using
concatenation, `String.format` or some other less-than-ideal method of
constructing a string. The only case where the second version would
really make sense is when there is a need to log an expensive
`toString` call and nothing else, which I have never found to be
appropriate in production code. Doing a bunch of extra API work to let
users avoid typing `"{}",` in a logging call doesn't seem worthwhile.

Both single-result and array-result lambdas have been added to the API.
While an array-result lambda is not strictly necessary, there are
probably cases where it is desirable. Passing a single lambda that
returns an array is likely to be more efficient than passing a
significant number of lambdas in a varargs array.

```java
// traditional way of doing lazy evaluation
if (logger.isDebugEnabled() {
    logger.debug("Shipping status change to {} on order {} for customer {}",
        shipment.getStatus(), shipment.getOrderInfo().getId(),
        shipment.getOrderInfo().getCustomer().getName());
}

// one lambda per argument
logger.debug("Shipping status change to {} on order {} for customer {}",
    () -> shipment.getStatus(), () -> shipment.getOrderInfo().getId(),
    () -> shipment.getOrderInfo().getCustomer().getName());

// one lambda returning an array
logger.debug("Shipping status change to {} on order {} for customer {}",
    () -> new Object[]{ shipment.getStatus(), shipment.getOrderInfo().getId(),
        shipment.getOrderInfo().getCustomer().getName() });
```



## Source incompatibility

This use of the `null` keyword is now ambiguous:

```java
  logger.debug("", null);
```

So far, it appears that only the single-argument call suffers from
source incompatibility for this specific case. The two-arg and var-arg
calls seem to work without difficulty.


## Implementation notes

Even though the default methods in `Logger` use static methods, those
methods do not live in the `Logger` class. This was done both out of a
concern for method visibility and to ensure compatibility with tools
like Retrolambda. No built-in Java 8 functions are used; again, this
ensures compatibility with older systems and avoids any runtime
dependency on Java 8 libraries.
