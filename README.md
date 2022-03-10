
# Maven plugin example

The functionality of this plugin is counting the number of lines of java source code in a project. 

## 1. How to set up the plugin 

To use it in a local project, install this plugin into your local repository with

```
mvn clean install
```

Then put the groupId of this plugin into your local `${user.home}/.m2/settings.xml`:

```
...
    <pluginGroups>
        <pluginGroup>com.example</pluginGroup>
    </pluginGroups>
</settings>
```

## 2. Use in a local project

Now you can use it in your local maven project as a maven plugin.
Put this into the `pom.xml` of the project:

```
...
    <build>
        <plugins>
            <plugin>
                <groupId>com.example</groupId>
                <artifactId>linecount-maven-plugin</artifactId>
                <version>1.0.0</version>
            </plugin>
        </plugins>
    </build>
</project>
```

To run the plugin in the project use:

```
mvn com.example:linecount-maven-plugin:1.0.0:linecount
```

or in short:

```
mvn linecount:linecount
```

## 3. Configure the plugin


To configure the output file name add a configuration to the `pom.xml`
attach the plugin to *compile* phase on the default livecycle like this:

```
...
    <build>
        <plugins>
            <plugin>
                <artifactId>linecount-maven-plugin</artifactId>
                <version>1.0.0</version>
                <configuration>
                    <outputFileName>numberOfJavaLines.txt</outputFileName>
                </configuration>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>linecount</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```
