# 🃏 Poker Game – Java Backend Application

## 📌 Overview

This project is a **Java-based Poker Game application** designed with a focus on **backend logic, data processing, and persistence**.
It simulates a real-world poker game while implementing core backend engineering principles such as **layered architecture, database integration, and structured data flow**.

The application evolved from a pure game logic implementation into a **data-driven backend system** using **SQLite and JDBC**.

---

## 🚀 Features

* 🎮 Poker game simulation with core game logic
* 🧠 Object-Oriented Design (Player, Game, Deck, etc.)
* 💾 Persistent data storage using **SQLite**
* 🗄️ Relational database schema for players, games, and results
* 🔄 Data access layer using **DAO (Repository Pattern)**
* 📊 SQL queries with joins and aggregations (e.g., leaderboard)
* 🧩 Clean separation of concerns (Service, Repository, Database layers)

---

## 🏗️ Project Structure

```
src/main/java/com/poker
│
├── model        # Domain models (Player, Game, Card, etc.)
├── service      # Business logic (GameService)
├── repository   # Data access layer (DAO classes)
├── database     # DB connection & initialization
└── Main         # Application entry point
```

---

## 🛠️ Technologies Used

* **Java**
* **Maven**
* **SQLite**
* **JDBC**
* **SQL**

---

## 🗄️ Database Design

The application uses a relational database with the following core tables:

* **players** → stores player data
* **games** → stores game sessions
* **game_results** → stores results per player per game

Example:

```sql
SELECT name, SUM(chips_change) as total
FROM players p
JOIN game_results gr ON p.id = gr.player_id
GROUP BY p.id
ORDER BY total DESC;
```

---

## 🧠 Key Concepts Demonstrated

* Backend system design fundamentals
* Data persistence with JDBC
* SQL query design (joins, aggregation)
* Layered architecture (Separation of Concerns)
* Object-Oriented Programming (OOP)
* Debugging and system flow control

---

## 📈 Future Improvements

* Convert to **Spring Boot REST API**
* Add endpoints for:

  * Player management
  * Game sessions
  * Leaderboard
* Implement transaction management
* Add unit and integration tests

---

## 👨‍💻 Author

**Anastasios Andreopoulos**

* LinkedIn: https://www.linkedin.com/in/anastasios-andreopoulos/
* GitHub: https://github.com/TasosAndreo

---

## 📄 License

This project is for educational and portfolio purposes.
