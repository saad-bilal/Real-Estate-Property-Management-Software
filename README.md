# Real Estate Property Management Software

Welcome to the RealEstatePropertyManagementSoftware project. This project uses a `.env` file to securely manage database connection details.

## Getting Started

1. Clone the repository.
2. Create a `.env` file in the root directory of the project.
3. Add your database connection details to the `.env` file.

## Folder Structure

The workspace contains the following folders:

- `src`: Contains the source code.
- `lib`: Contains dependencies.
- `bin`: Contains the compiled output files.

## Dependency Management

Dependencies are managed through the `JAVA PROJECTS` view. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Database Connection

The `DBUtils` class in the `src` directory uses the environment variables from the `.env` file to establish a database connection. The `.env` file should contain the following keys:

- `DB_URL`: The URL of your database.
- `DB_USERNAME`: The username for your database.
- `DB_PASSWORD`: The password for your database.

Ensure that the `.env` file is correctly set up before running the application.

### Example `.env` File

Here's an example of what your `.env` file should look like:

DB_URL=jdbc:mysql://localhost:3306/DatabaseName
DB_USERNAME=UserName
DB_PASSWORD=Password

### Using the Environment Variables

In your Java code, you can access these environment variables using `System.getenv("DB_URL")`, `System.getenv("DB_USERNAME")`, and `System.getenv("DB_PASSWORD")`.

### IDE Configuration

If you're running your application from an IDE like Eclipse or IntelliJ IDEA, you might need to configure the IDE to load the environment variables from the `.env` file. This step varies depending on the IDE you're using.

## Running the Application

After setting up the `.env` file and configuring your IDE, you can run the `App` class to establish a database connection and perform operations.

## Security Considerations

Remember to keep the `.env` file secure and do not commit it to version control. It should be added to your `.gitignore` file to prevent it from being tracked by Git.