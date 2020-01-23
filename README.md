# Fastfield image getter
## Getting image from fastfield without using their api
> requires JDK 8
1. install maven - https://mkyong.com/maven/how-to-install-maven-in-windows/
2. git clone
3. edit file \src\main\java\open\elmerhd\fastfield\FastfieldApp.java insert your username and password and save
```
System.setProperty("fastfield-username", "");
System.setProperty("fastfield-password", "");
```
4. mvn install
5. once done installation go to your browser then enter http://localhost:8080/fastfield/api/image/?file=file.jpg
