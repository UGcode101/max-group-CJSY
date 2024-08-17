# ThreeMix Playlist Generator

ThreeMix is a web application that allows users to create custom playlists by selecting a mix of three genres from the Spotify database. Users can also block specific songs or artists from being included in their playlists, ensuring that their music preferences are fully respected.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Technologies Used](#technologies-used)
- [License](#license)
- [Disclaimer](#disclaimer)

## Features

- **Custom Playlist Generation**: Create playlists based on a mix of three genres.
- **Spotify Integration**: Utilizes the Spotify API to fetch songs and genres.
- **Blocked Content**: Users can block specific songs or artists to prevent them from being added to their playlists.
- **User Profiles**: Save and manage user preferences based on their Spotify ID.

## Installation

### Prerequisites

- Java 17 or higher
- Gradle 7.0 or higher
- MySQL database
- Spotify Developer Account

### Setup Instructions

1. **Clone the Repository**

   ```bash
   git clone https://github.com/ALLHUBS-Jan-2024-Liftoff/max-group-CJSY.git
   cd max-group-CJSY
   ```

2. **Set Up the Database**

   Create a new MySQL database and update the `application.properties` file with your database details:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your-database-name
   spring.datasource.username=your-username
   spring.datasource.password=your-password
   ```

3. **Configure Spotify API**

 Create a ClientConstants class and put the secret in the folder. This folder is not checked in to Github.

   ```properties
package org.launchcode.threemix.secret;

    public class ClientConstants {
    public static final String CLIENT_ID = "";
public static final String CLIENT_SECRET = "";
}
   ```

4. **Build and Run the Application**

   Use Gradle to build and run the application:
 
Use bootRun

 Open terminal and do `npm run dev`.

5. **Access the Application**

   Open your browser and navigate to `http://localhost:5173` to access the ThreeMix application.

## Usage

1. **Login with Spotify**: Users need to log in with their Spotify accounts to access their music preferences and playlists.

2. **Create a Playlist**: Select three genres to create a playlist.

3. **Block Songs/Artists**: Users can block specific songs or artists from appearing in their playlists via the settings.

4. **Manage Preferences**: User preferences are saved and can be managed through the profile section.

## Technologies Used

- **Java & Spring Boot**: Backend framework
- **MySQL**: Database
- **Spotify Web API**: Integration with Spotify
- **React**: Frontend framework (if applicable)
- **Grid: CSS**: Layout and design

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Disclaimer

This project is intended for educational and personal use only. It is not affiliated with or endorsed by Spotify. Usage of this application must comply with Spotify's [Developer Terms of Service](https://developer.spotify.com/terms/).

By using this application, you agree that you are solely responsible for your use of any Spotify content and data obtained through the Spotify API. The creators of this application are not liable for any misuse of the Spotify API or for any copyright infringement that may occur through the use of this application.

---

This version of the README focuses on the essential details about the project, setup, and usage without delving into the specifics of the API endpoints. It should be easier for end-users or developers to understand the project at a glance while still providing all necessary setup and contribution guidelines.