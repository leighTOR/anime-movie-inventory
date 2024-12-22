# Anime Movie Purchases – Inventory System

A JavaFX + SQLite application for managing anime movie purchases.  
Users can **Add**, **Update**, **Delete**, and **View** (Title, Quantity, Price) in a table.

---

## Table of Contents
1. [System Requirements](#1-system-requirements)
2. [Folder Structure](#2-folder-structure)
3. [Running the Application](#3-running-the-application)
4. [Using the Application](#4-using-the-application)
5. [Database File](#5-database-file)
6. [Troubleshooting & Tips](#6-troubleshooting--tips)
7. [Contact / Support](#7-contact--support)

---

## 1. System Requirements

- **Operating System**: Windows 10 or later (64-bit).
- **Java 17 or Newer**:
  - Install from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [Adoptium](https://adoptium.net/).
  - Confirm installation by running `java -version` in a command prompt.
- **JavaFX 17+ SDK**:
  - Download from [openjfx.io](https://openjfx.io/).
  - Unzip to a folder, for example: `C:\javafx-sdk-17.0.13\`.
- **SQLite JDBC Library**:
  - Provided as `sqlite-jdbc-3.47.x.jar` in the `lib` folder, no separate SQLite install required.

---

## 2. Folder Structure

A typical project folder (e.g., `inventory-java`) might look like this:


```plaintext
inventory-java/
├─ .vscode/
│  ├─ launch.json
│  └─ settings.json
├─ classes/
│  ├─ AnimePurchase.class
│  ├─ DBConnection.class
│  └─ MainApp.class
├─ lib/
│  └─ sqlite-jdbc-3.47.1.0.jar
├─ src/
│  ├─ AnimePurchase.java
│  ├─ DBConnection.java
│  ├─ MainApp.java
│  └─ style.css
├─ MyInventoryApp.jar
├─ manifest.txt
├─ mydatabase.db
└─ README.md
```

Where:
- **`.vscode/`** contains VS Code config files for running/debugging.
- **`classes/`** holds the compiled `.class` files.
- **`lib/`** has the `sqlite-jdbc` driver.
- **`src/`** has Java source files and `style.css`.
- **`MyInventoryApp.jar`** is the runnable JAR.
- **`mydatabase.db`** is the SQLite database file.
- **`README.md`** is this documentation.

---

## 3. Running the Application

### 3.1. Check Java & JavaFX

1. **Verify Java Installation**  
   - Open a command prompt and run:
     ```powershell
     java -version
     ```
   - You should see `java version "17.x"`.

2. **Ensure JavaFX SDK Is Unzipped**  
   - Example folder: `C:\javafx-sdk-17.0.13\`.
   - It should contain a `lib` subfolder with the JavaFX libraries.

### 3.2. Run via Command Prompt

In the directory where `MyInventoryApp.jar` is located, run:


java --module-path "C:\javafx-sdk-17.0.13\lib" --add-modules javafx.controls,javafx.fxml ^
     -cp "MyInventoryApp.jar;lib\sqlite-jdbc-3.47.1.0.jar" MainApp


- **`--module-path`** points to the JavaFX SDK’s `lib` folder.  
- **`--add-modules javafx.controls,javafx.fxml`** activates the needed JavaFX modules.  
- **`-cp "MyInventoryApp.jar;lib\sqlite-jdbc-3.47.1.0.jar"`** sets the classpath to include your JAR and the SQLite driver.  
- **`MainApp`** is the main class that launches the GUI.  

> **Note**: On Windows `cmd.exe`, use `^` for line continuation; in PowerShell, use backticks `` ` ``.

### 3.3. (Optional) Run in VS Code

1. Open `inventory-java` in VS Code.  
2. Go to **Run and Debug**.  
3. Select **“Launch JavaFX App”** in the dropdown.  
4. Press **F5** or **Run**.  
5. Ensure the `--module-path` in `.vscode/launch.json` matches your JavaFX SDK folder.

---

## 4. Using the Application

Once launched, the JavaFX window provides:

- **Table** with columns: **ID**, **Title**, **Quantity**, **Price**  
- **Text fields** for Title, Quantity, and Price  
- **Buttons**:  
  1. **Add** – Insert a new record into the database.  
  2. **Update** – Update the currently selected row in the table.  
  3. **Delete** – Remove the selected row from the database.  
  4. **Refresh** – Re-query the database and refresh the table.

### 4.1. Add a Purchase
1. Enter a **Title** (e.g., “Your Name”).  
2. Enter **Quantity** (e.g., 3).  
3. Enter **Price** (e.g., 19.99).  
4. Click **Add**; a new record appears with an auto-incremented ID.

### 4.2. Update a Purchase
1. Select a row in the table.  
2. Edit Title, Quantity, or Price in the text fields.  
3. Click **Update**; changes are saved in the database.

### 4.3. Delete a Purchase
1. Select the row to remove.  
2. Click **Delete**; the record is removed from the database.

### 4.4. Refresh
- Click **Refresh** to re-load data from `mydatabase.db`.

---

## 5. Database File

- **`mydatabase.db`** is the default SQLite file, created automatically if missing.  
- If you want a custom path, open **DBConnection.java** and change:
  ```java
  private static final String DB_URL = "jdbc:sqlite:mydatabase.db";
  ```
  to something like:
  ```java
  private static final String DB_URL = "jdbc:sqlite:C:/MyDatabases/animeInventory.db";
  ```
- Make sure the folder you choose is writable.

---

## 6. Troubleshooting & Tips

1. **No suitable driver found**  
   - Check that `sqlite-jdbc-3.47.1.0.jar` is on the runtime classpath (`-cp` argument).

2. **JavaFX classes not found**  
   - Verify the `--module-path` is correct and includes `javafx.controls, javafx.fxml`.

3. **Cannot write to DB**  
   - Ensure you have write permissions where `mydatabase.db` resides.

4. **Antivirus blocking**  
   - If the app closes immediately or you see warnings, confirm your antivirus isn’t blocking the JAR/JavaFX libraries.

---

## 7. Contact / Support

For questions or assistance:

- **Developer**: Leigh Jamolin  
- **Email**: [leighinna.jamolin@lspu.edu.ph](mailto:leighinna.jamolin@lspu.edu.ph)  
- **Facebook**: [facebook.com/le1ghdev](https://www.facebook.com/le1ghdev/)

Enjoy managing your anime movie collection with the **Anime Movie Purchases** inventory system!