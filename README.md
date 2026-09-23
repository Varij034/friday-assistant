# F.R.I.D.A.Y. — Local AI Voice Assistant 🎙️🤖

**F.R.I.D.A.Y.** is a 100% free, fully local, privacy-focused voice assistant built with **Java** and **Spring Boot**.

---

## ✨ Features

- 🔒 **100% Local & Offline First**: Zero cloud APIs and full data privacy.
- 🗣️ **Real-Time Speech Recognition**: Powered by `vosk-model-small-en-us`.
- 🧠 **Local LLM Intelligence**: Connects to **Ollama** (`llama3.2`) via **Spring AI**.
- 💻 **Real System Diagnostics**: Reports actual CPU load, RAM utilization, and disk storage.
- 🛠️ **Desktop Application Execution**: Voice-trigger apps like VS Code, Notepad, and Shutdown.

---

## 📁 Project Structure

```text
friday-assistant/
├── libs/
│   └── vosk-0.3.38.jar
├── models/
│   └── vosk-model-small-en-us/
├── src/
│   └── main/
│       ├── java/com/stark/friday/
│       │   ├── runner/
│       │   │   └── VoiceAssistantRunner.java
│       │   ├── service/
│       │   │   ├── FridayBrainService.java
│       │   │   ├── SystemControlService.java
│       │   │   └── TextToSpeechService.java
│       │   └── FridayApplication.java
│       └── resources/
│           └── application.yml
└── pom.xml
```

## 🚀 Getting Started
Prerequisites
Java 17 or higher installed.

Maven 3.8+ configured.

Ollama installed with `llama3.2`:
```text
Bash
ollama pull llama3.2
```
Setup & Execution
Clone the Repository:
```text
Bash

git clone [https://github.com/YOUR_USERNAME/friday-assistant.git](https://github.com/YOUR_USERNAME/friday-assistant.git)
cd friday-assistant
```
Add Vosk Model:
Unzip `vosk-model-small-en-us` into the `models/` directory in the root of your project.

Run Application:

Bash
```text
mvn clean compile
mvn spring-boot:run
```

## 🗣️ Supported Commands
General Conversation: "Hey F.R.I.D.A.Y., what are we building today?"

System Diagnostics: "Give me a system check." / "Diagnostics."

Open Applications: "Open Visual Studio Code" / "Open Notepad"

System Control: "Turn off my PC" / "Shutdown"

## 📄 License
Distributed under the MIT License.