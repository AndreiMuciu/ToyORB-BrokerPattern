# ToyORB – A Minimalist Object Request Broker 🌐

> A distributed system project built to demonstrate the **Broker architectural pattern** and core principles of middleware communication in multi-language environments.

## 📌 Project Overview

**ToyORB** is a custom-built Object Request Broker (ORB) that allows distributed object-oriented communication between clients and servers, without using existing middleware technologies like Java RMI or gRPC. It supports remote procedure calls (RPC) across different processes, languages, and platforms.

This project was developed as part of a distributed systems assignment, with a focus on understanding the internal workings of middleware and the Broker pattern.

---

## 🏗️ Architecture

The architecture is composed of three main layers:

### 🟨 Client Layer (JavaScript)
- **Client**: Reads user input and selects the target server.
- **ClientProxy**: Looks up server addresses using the Dispatcher and forwards requests to the appropriate ServerProxy.

### 🟦 Dispatcher (C++)
- Acts as a **NamingService** to map logical server names to IP:Port addresses.
- Accepts dynamic server registrations and handles client lookups.

### 🟥 Server Layer (Java)
- **ServerProxy**: Registers itself to the dispatcher and dynamically invokes server-side methods via Java Reflection.
- **MathServer** & **InfoServer**: Implement the actual logic exposed to clients, such as math operations and info retrieval.

---

## 🧪 Features

- ✅ Implements the **Broker pattern** with clear separation of concerns.
- ✅ Supports **dynamic server lookup and binding**.
- ✅ Uses **reflection** for method dispatching on the server side.
- ✅ Works across **JavaScript (Node.js), C++, and Java**.
- ✅ Two sample distributed applications:
  - **MathServer**: add, subtract, divide, sqrt, etc.
  - **InfoServer**: get temperature, road info, UCL winner, etc.

---

## 🚀 How to Run

### 🛠 Prerequisites
- Node.js (v14+)
- JDK 8+ (for Java servers)
- A C++ compiler (tested with MSVC on Windows)

---

### 📦 Step-by-Step Setup

1. **Start the Dispatcher (C++)**
   ```bash
   cd dispatcher/
   g++ dispatcher.cpp -o dispatcher.exe -lws2_32
   ./dispatcher.exe
