# CodeU - Nemo

Welcome to Nemo, It's complete and functional, but leaves
plenty of room for improvement.

Changes we've made since we started:

* Nemo has profile pages for users where they can update their about Me's and view their
sent messages.
* Nemo has private and public conversations.
* Nemo uses filters to determine if a user has access to a specific page of the app.
Logged in users no longer have access to login/register page and users that aren't logged
in have access to only those two pages.
* Nemo has an Activity Feed that allows users to see when new users join, create
 conversations, send messages, and update their About Me's.
* Nemo allows you to create conversations with titles that have multiple words.
* Nemo has an dynamic search feature that allows users to search for other users. 
* Nemo has a Bot Framework that allows developers to contribute their own Bots. 
* Nemo has NemoBot, a bot that functions as an assistant.
* Nemo has ConversationStatBot, a bot that provides conversation statistics.

## Step 1: Download Java

Check whether you already have Java installed by opening a console and typing:

```
javac -version
```

If this prints out a version number, then you already have Java and can skip to
step 2. If the version number is less than `javac_1.8`, then you have an old
version of Java and should probably upgrade by following these instructions.

Download the JDK (not the JRE) from [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520.html).

Retry the `javac -version` command **in a new console window** to check the
installation. If you still don't see a version number, then update your `PATH`
environment variable so it contains Java's `bin` directory. Follow [these
directions](https://www.java.com/en/download/help/path.xml) to do so.

## Step 2: Download Maven

This project uses [Maven](https://maven.apache.org/) to compile and run our
code. Maven also manages dependencies, runs the dev server, and deploys to App
Engine.

Download Maven from [here](https://maven.apache.org/download.cgi). Unzip the
folder wherever you want.

Make sure you have a `JAVA_HOME` environment variable that points to your Java
installation, and then add Maven's `bin` directory to your `PATH` environment
variable. Instructions for both can be found
[here](https://maven.apache.org/install.html).

Open a console window and execute `mvn -v` to confirm that Maven is correctly
installed.

## Step 3: Install Git

This project uses [Git](https://git-scm.com/) for source version control and
[GitHub](https://github.com/) to host our repository.

Download Git from [here](https://git-scm.com/downloads).

Make sure Git is on your `PATH` by executing this command:

```
git --version
```

If you don't see a version number, then make sure Git is on your `PATH`.

## Step 4: Setup your repository

Follow the instructions in the first project to get your repository setup.

## Step 5: Run a development server

In order to test changes locally, you'll want to run the server locally, on your
own computer.

To do this, open a console to your `codeu_project_2018` directory and execute this command:

```
mvn clean appengine:devserver
```

This tells Maven to clean (delete old compiled files) and then run a local
App Engine server.

You should now be able to use a local version of the chat app by opening your
browser to [http://localhost:8080](http://localhost:8080).

## Step 6: Make a change!

- Bring down the existing server by pressing `ctrl+c` in the console running the
App Engine devserver.
- Modify a `.java` or `.jsp` file. (Try updating the homepage by editing the
`index.jsp` file.)
- Bring the devserver back up by executing `mvn clean appengine:devserver`
again.
- Refresh your browser to see your changes!
