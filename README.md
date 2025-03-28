# Java Network Intrusion Detection System (NIDS)

## Overview
The **Java Network Intrusion Detection System (NIDS)** is a real-time traffic analysis and monitoring tool designed to detect suspicious activities within a network. It utilizes **Pcap4J** for packet capturing and **JavaFX** for a user-friendly interface, enabling security analysts to identify potential threats effectively.

## Features
- **Real-time packet capturing** using **Pcap4J**.
- **Traffic analysis and visualization** with JavaFX.
- **Detection of malicious patterns** based on predefined rules.
- **Logging of suspicious activities** for further investigation.
- **User-friendly interface** for monitoring network traffic.

## Installation
### Prerequisites
Ensure you have the following installed on your system:
- **Java Development Kit (JDK) 11+**
- **Pcap4J library**
- **JavaFX library**
- **WinPcap/libpcap** (depending on your OS)

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/AbdrrahimDahmani/Java-Network-Intrusion-Detection-System.git
   cd Java-Network-Intrusion-Detection-System
   ```
2. Install dependencies:
   - Add **Pcap4J** and **JavaFX** to your project's classpath.
   - Ensure your system has **WinPcap** (Windows) or **libpcap** (Linux/Mac) installed.
3. Compile and run the project:
   ```bash
   javac -cp ".;lib/*" src/Main.java
   java -cp ".;lib/*" src.Main
   ```

## Usage
1. Launch the application.
2. Select a network interface for packet capturing.
3. Start monitoring network traffic in real-time.
4. View detected anomalies and logs for analysis.

## Contributing
Feel free to submit issues, suggestions, or pull requests to improve this project.

## License
This project is licensed under the **MIT License**.

## Author
Abderrahim Dahmani – Cybersecurity & Digital Trust Engineering Student @ ENSET Mohammedia

