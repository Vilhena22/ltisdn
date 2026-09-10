# LTISDN — SDN Controller for MikroTik Devices

A Java desktop application that acts as an **SDN (Software-Defined Networking) controller** for MikroTik routers, communicating exclusively through the **RouterOS REST API**. It centralizes and visualizes network management tasks that would normally be configured via Winbox or the command line.

Developed as part of the *Information Technology Laboratory* course (TL1), Computer Engineering degree — Polytechnic Institute of Leiria.

## Features

- **Authentication and connection** to one or more MikroTik routers via HTTPS, with locally stored connection history.
- **Interfaces**: listing, enabling/disabling, and removing physical and virtual interfaces.
- **Bridges**: creation and management of bridges and their ports.
- **WiFi**: wireless network configuration and security profile creation.
- **IP Addressing**: assignment, enabling/disabling, and removal of IP addresses.
- **Static Routes**: creation, enabling/disabling, and removal.
- **DHCP**: management of servers, pools, leases, and DHCP clients.
- **DNS**: DNS server configuration, static record management, and cache handling.
- **WireGuard VPN**: interface and peer creation, client configuration generation with **QR code** for quick provisioning on mobile devices (Windows, Linux, Android).
- **Dashboard**: system resource visualization (CPU, memory, etc.) with charts (JFreeChart).
- **Light/dark theme** with automatic OS theme detection.

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Build | Maven |
| GUI | Java Swing + [FlatLaf](https://www.formdev.com/flatlaf/) |
| Router communication | RouterOS REST API (HTTPS) |
| JSON serialization | Jackson, Gson |
| Local persistence | SQLite (JDBC) |
| Charts | JFreeChart |
| QR Code | ZXing |
| Logging | SLF4J |

## Architecture

The application follows a simple layered structure:

- **`ApiClient`** — HTTP client responsible for all communication with the RouterOS REST API (basic authentication, GET/POST requests, HTTPS certificate handling).
- **`Models`** — representation of RouterOS resources (interfaces, DHCP, DNS, routes, WireGuard, etc.) and local database access (`RouterDAO`, `Database`) for storing previously configured routers.
- **`Forms` / `Dialogs`** — graphical interface (Swing), including login, the main page (dashboard), and dialogs for creating/editing resources.
- **`endpoints/Mikrotik-Endpoints`** — collection of RouterOS REST API endpoints used by the application, documented as YAML files (Insomnia-compatible), organized by category (addresses, dhcp, dns, interfaces, route, system).

## Prerequisites

- Java 21+
- Maven
- A MikroTik router running RouterOS with the **REST API enabled** (`/ip/service` → `www-ssl` / `api-ssl`)
- Network access to the router (connects via HTTPS by default)

## Configuration

The test router's credentials and address used during development are set in:

```
src/main/java/ApiClient/MikrotikConfig.java
```

When launching the application, you can also authenticate directly from the login screen with the host, username, and password of the target router — previously used routers are stored locally in a SQLite database (`data/app.db`) for faster reconnection.

## Running the project

```bash
git clone https://github.com/Vilhena22/ltisdn.git
cd ltisdn
mvn clean install
mvn exec:java -Dexec.mainClass="Main.Main"
```

Alternatively, run the `Main.Main` class directly from an IDE (IntelliJ IDEA).

## Project structure

```
ltisdn/
├── endpoints/Mikrotik-Endpoints/   # RouterOS REST API endpoint collection (YAML)
├── data/                           # Local database (SQLite)
├── src/main/java/
│   ├── ApiClient/                  # RouterOS REST API client
│   ├── Models/                     # MikroTik resource models + local persistence
│   ├── Forms/                      # Main screens (Login, Home/Dashboard)
│   ├── Dialogs/                    # Resource creation/editing dialogs
│   └── Main/                       # Application entry point
└── pom.xml
```

## Authors

- Francisco Vilhena — [@Vilhena22](https://github.com/Vilhena22)
- Pedro Gomes - (https://github.com/PedroGomesReis)

## Academic context

Laboratory Assignment No. 1 (TL1) — Information Technology Laboratory, EI 2025/26, Polytechnic Institute of Leiria.
