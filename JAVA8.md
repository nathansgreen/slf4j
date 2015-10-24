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


