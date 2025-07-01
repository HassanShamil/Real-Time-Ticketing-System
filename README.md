# 🎟️ Real-Time Multi-Threaded Ticketing Simulator

Ever wondered how high-demand ticketing systems work behind the scenes?  
This project is a **Java-based, real-time simulation** of a ticketing platform where **vendors issue tickets** and **customers fight to grab them** all powered by **threads, locks, and smart configuration**.

It’s not just code. It’s **a live event in your terminal**.

---

## 🧠 What's Inside?

| Component        | Description                                                                          |
|------------------|--------------------------------------------------------------------------------------|
| `Main.java`      | User input, config setup, thread bootstrapping, and logging                          |
| `Configuration`  | Stores user-defined settings and handles persistence via serialization               |
| `Vendor`         | Releases tickets into the system on a timed loop (threaded)                          |
| `Customer`       | Attempts to purchase tickets periodically (threaded)                                 |
| `TicketPool`     | Shared resource pool with thread-safe access                                         |
| `Ticket`         | Lightweight class representing a single ticket with ID and metadata                  |

---

## ⚙️ Features That Rock

✅ Interactive CLI to load or define ticket system configuration  
✅ Thread-safe ticket release & purchase system using `ReentrantLock`  
✅ Smart ticket allocation logic for fair vendor/customer distribution  
✅ Automatic logging to `app.log` for debugging and record-keeping  
✅ Fully configurable run-time simulation with real-time console output  
✅ Persistent config support via `.ser` and `.txt` files  

---

## 🚀 Getting Started

### 🧰 Prerequisites

- Java 8 or higher installed
- Terminal / IDE (IntelliJ, Eclipse, or VS Code with Java support)

### 🔨 Compile the Project

```bash
javac *.java
```

### ▶️ Run the Simulation

```bash
java Main
```

You’ll be asked to:
- Load previous configuration or create new one
- Set the total tickets, release/retrieval rates, vendor/customer count, cooldowns
- Sit back and watch the chaos unfold in your console

---

## ⚙️ Configuration Parameters

| Option                      | Description                                                      |
|-----------------------------|------------------------------------------------------------------|
| `Total Tickets`             | Number of tickets initially in the system                        |
| `Ticket Release Rate`       | How many tickets vendors release per cycle                       |
| `Customer Retrieval Rate`   | How many tickets customers try to buy per cycle                  |
| `Max Ticket Capacity`       | Maximum tickets allowed in the pool at a time                    |
| `Simulation Time`           | How long the simulation should run (in milliseconds)             |
| `Vendor/Customer Cool Time` | Delay between each vendor/customer action (in milliseconds)      |

All this can be saved and loaded from `configDetails.txt` and `Config.ser`.

---

## 📊 Sample Output

```
Customer-1 successfully purchased [Ticket#1, Ticket#2]
Available tickets: 18

Vendor-1 releasing 3 tickets.
Available tickets: 21

Simulation Completed
Total available tickets: 5
Total sold tickets: 95
```
---

## 📦 Running via JAR (No Compilation Needed)

Want to try it out instantly without compiling?

1. Visit the [Releases](https://github.com/HassanShamil/Real-Time-Ticketing-System/releases) section.
2. Download `TicketSystem.jar`.
3. Open your terminal and navigate to the downloaded file's location.
4. Run the app with:

```bash
java -jar TicketSystem.jar
```

---

## 🧾 Files That Matter

- `Config.ser` – Serialized configuration object for quick reloading
- `configDetails.txt` – Human-readable config summary
- `app.log` – System log of all operations and warnings

---

## 🛡️ Design Philosophy

- 💡 **Concurrency First**: Every part of the system simulates a real-time environment using threads
- 🔐 **Thread Safety**: The `TicketPool` uses `ReentrantLock` to avoid race conditions
- 🎲 **Dynamic Simulation**: Every run can be different thanks to randomized ticket allocations
- 🧪 **Testable & Extendable**: Modular class structure makes it easy to plug in analytics, GUIs, or databases

---

## 💡 Ideas for Extension

- Add GUI using JavaFX or Swing
- Hook it up to a database for historical tracking
- Add priority queues for VIP customers
- Introduce ticket cancellation/refund logic

---

**🎉 Ready to simulate a box office in your terminal? Let's go.**
