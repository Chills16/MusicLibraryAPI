# MusicLibraryAPI

Welcome to the MusicLibraryAPI, an interactive command-line application that allows users to manage a digital music library with integrations to Spotify for real-time song data fetching. This application supports various functionalities such as adding songs, managing playlists, and searching for specific songs or playlists.

## Features

- **Add Songs:** Add songs to your library with real-time data from Spotify.
- **List Songs:** View all songs currently in the library.
- **Remove Songs:** Delete songs from your library.
- **Create Playlists:** Organize songs into customizable playlists.
- **Add Songs to Playlists:** Add library songs to different playlists.
- **Remove Songs from Playlists:** Remove songs from specific playlists.
- **View Playlists:** Display all songs within a specific playlist.
- **Delete Playlists:** Completely remove playlists.
- **Search Songs:** Find songs in the library by name.
- **Search Playlists:** Find playlists by name.

## Prerequisites

Before you begin, ensure you have the following installed on your system:
- Java JDK 11 or newer
- Apache Maven 3.6.0 or newer

You can check your Java version by running:
```bash
java -version
```

And your Maven version with:
```bash
mvn -version
```

## Installation

Cloning the Repository
To get started with the MusicLibraryAPI, clone this repository to your local machine using the following command:
```bash
git clone https://github.com/Chills16/MusicLibraryAPI.git
cd MusicLibraryAPI
```

Building the Project
Navigate to the project directory where the pom.xml file is located and run the following Maven command to build the project and download all necessary dependencies:
```bash
mvn clean install
```

Running the Application
After building the project, you can run the application directly using Maven by executing:
```bash
mvn exec:java -Dexec.mainClass="main.Main"
```
Make sure to replace "main.Main" with the correct path to your main class if it differs.


Packaging the Application as a JAR
If you prefer to run the application as a JAR file, ensure your pom.xml includes the necessary configuration for the Maven JAR Plugin as shown in the project documentation. Then, execute:
```bash
mvn clean package
```

This command will generate a JAR file in the target directory. Run the JAR file using:
```bash
java -jar target/MusicLibraryAPI-1.0-SNAPSHOT.jar
```
Replace MusicLibraryAPI-1.0-SNAPSHOT.jar with your generated JAR file name.

## Configuration
Before running the application, make sure to configure the Spotify API credentials. Place your clientId and clientSecret in the config.properties file located under src/main/resources. This file should not be pushed to public repositories to keep your credentials secure.

## Contribution
Contributions to the MusicLibraryAPI are welcome! Please fork the repository, make your changes, and submit a pull request. For major changes, please open an issue first to discuss what you would like to change.

## License
This project is licensed under the MIT License - see the LICENSE.md file for details.

## Support
If you encounter any problems or have any queries regarding the MusicLibraryAPI, please open an issue through the GitHub issue tracker, or contact me directly at david.buko16@gmail.com.

Thank you for using MusicLibraryAPI!