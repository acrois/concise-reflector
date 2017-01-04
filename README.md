# concise-reflector
A concise and easy-to-use wrapper for the Java Reflection API ([java.lang.reflect](https://docs.oracle.com/javase/tutorial/reflect/)) that allows for reflection operations in a very non-verbose manner. It also includes a class file manager that allows for you to quickly load and manage Class files from a jar or a directory.
## Examples:

```java
import static com.github.silk8192.reflector.Reflector.*;

ClassFileManager cfm = new ClassFileManager(classPath);

//Creation
String str = forClass(String.class).create("Hello, World!").get();

//Calling a method
forClass(String.class).create("Hello, World!").invoke("toString");

//Setting a field
//Person class
String firstName = forClass(cfm.getClass("foo.Person")).create("Foo", "Bar").get("firstName");

//Setting a field
forClass(cfm.getClass("foo.Person")).create("Foo", "Bar").set("firstName", "Bar2");
```

Using the class file manager:
```java
//For a regular directory
Path classPath = Paths.get("foo\\bar");
ClassFileManager cfm = new ClassFileManager(classPath);

//For a .jar
ClassFileManager cfm = new ClassFileManager(Paths.get("foo\\bar.jar").toFile());

//Accessing files
cfm.getClass("foo.Bar");
```

## Dependencies:
* SLF4j Logging
* Commons-io