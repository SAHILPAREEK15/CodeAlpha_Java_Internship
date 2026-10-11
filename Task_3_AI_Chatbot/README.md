# Task 3: Artificial Intelligence Chatbot - JavaAI Assistant

## Project Overview
A Java-based educational chatbot that uses rule-based Natural Language Processing (NLP) to answer questions about Java, OOP, Data Structures, Algorithms, internships, and placements. No external APIs required - completely self-contained.

## IMPORTANT NOTE
This chatbot uses a **rule-based NLP engine**, NOT machine learning. It performs keyword matching to detect user intent and retrieves pre-defined knowledge base responses. This is clearly documented in the application.

## Features
- **Chat Interface**: Modern chatbot UI with message bubbles
- **Multiple Chat Sessions**: Create, manage, and access chat history
- **Knowledge Base**: Comprehensive answers on:
  - Java Basics
  - OOP Concepts (Encapsulation, Inheritance, Polymorphism, Abstraction)
  - Data Structures & Algorithms
  - Internship Preparation
  - Interview Preparation & Placement Tips
- **Quick Questions**: Pre-defined quick question buttons
- **Chat History**: Persistent storage of all conversations
- **Search Chat**: Find previous conversations
- **Export/Clear**: Clear chats and manage conversations

## Knowledge Base Topics
1. **Java Basics**: JVM, bytecode, class structure, main method
2. **OOP**: Four pillars, classes vs objects, interfaces
3. **DSA**: Arrays, LinkedLists, Stacks, Queues, Trees, Graphs, Sorting, Time Complexity
4. **Internship**: CodeAlpha, practical experience, project work
5. **Placement**: Interview prep, behavioral questions, package negotiation

## Technologies Used
- Java 17+
- Java Swing for GUI
- Object serialization for chat persistence
- Collections Framework (HashMap, ArrayList, List)
- Rule-based NLP (keyword matching)

## OOP Concepts Used
- **Encapsulation**: Message, ChatBot, ChatManager classes
- **Inheritance**: Panel classes extending JPanel
- **Polymorphism**: Message handling and responses
- **Abstraction**: ChatBot logic hidden from UI
- **Collections**: HashMap for knowledge base storage

## Project Structure
```
Task_3_AI_Chatbot/
├── src/
│   ├── AIChatBotApp.java                (Main application GUI)
│   ├── Message.java                     (Message model)
│   ├── ChatBot.java                     (ChatBot engine with NLP)
│   ├── ChatManager.java                 (Chat history management)
├── data/
│   └── chats/
│       └── [chat_session_files].txt    (Conversation history)
└── README.md
```

## Compilation & Execution

### Compile:
```bash
cd Task_3_AI_Chatbot/src
javac *.java
```

### Run:
```bash
java AIChatBotApp
```

## How to Use

1. **Start Chatting**: Type a question in the message box and press Enter or click Send
2. **Quick Questions**: Click pre-defined questions for instant answers
3. **New Chat**: Create new conversation sessions from sidebar
4. **Chat History**: Access previous conversations from left sidebar
5. **Clear Chat**: Remove all messages from current session
6. **Delete Chat**: Delete entire chat session

## How NLP Works (Rule-Based)
The chatbot uses keyword matching to detect user intent:

```
User: "What is inheritance?"
↓
Detect keywords: ["inheritance"]
↓
Match to category: OOP
↓
Retrieve random response from knowledge base
↓
Display: "Inheritance allows a class to inherit..."
```

## Sample Conversations

### Example 1:
```
User: What is OOP?
Bot: OOP (Object-Oriented Programming) is a programming paradigm based on objects and classes.
     The four pillars of OOP are: Encapsulation, Inheritance, Polymorphism, Abstraction.
```

### Example 2:
```
User: Tell me about DSA
Bot: Data Structure is a way of organizing data efficiently. Common ones: Array, LinkedList, 
     Stack, Queue, Tree, Graph, HashMap.
     Algorithm is a step-by-step procedure for solving a problem...
```

## Quick Questions Available
- What is Java?
- What is OOP?
- What is DSA?
- Difference between Array and LinkedList
- What is an Internship?
- How to prepare for interviews?

## Data Storage
- Chat history stored in: `data/chats/[chat_id].txt`
- Each session has unique ID: `chat_[timestamp]`
- Messages are serialized Java objects
- Persistent across sessions

## Input Validation
- Empty messages are ignored
- Timestamp automatically added to each message
- Invalid input gracefully handled

## DISCLAIMER
This chatbot is for **EDUCATIONAL PURPOSES** and uses a rule-based NLP system, NOT actual AI/ML. It provides general information based on a knowledge base, not real-time intelligence.

## Limitations
- Rule-based system with limited flexibility
- Cannot understand complex linguistic variations
- Knowledge base is static (predefined answers)
- No continuous learning
- Limited to predefined topics
- No integration with real AI APIs

## Future Improvements
- Integration with ChatGPT/Claude API for intelligent responses
- Machine learning-based intent classification
- Custom training on internship-specific data
- Voice input/output support
- Multi-language support
- Context-aware responses
- Performance recommendation engine
- Real-time job listings integration
- Mock interview simulation
- Portfolio building assistance
