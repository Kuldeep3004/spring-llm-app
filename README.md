\# 🛡️ Spring LLM App — AI-Powered Java Code Vulnerability Scanner



> An end-to-end DevSecOps project that automatically scans Java code for security vulnerabilities using Large Language Models (LLM), with a fully automated CI/CD pipeline that deploys to the cloud.



\[!\[CI/CD Pipeline](https://github.com/Kuldeep3004/spring-llm-app/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/Kuldeep3004/spring-llm-app/actions)

\[!\[Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.org/)

\[!\[Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)](https://spring.io/projects/spring-boot)

\[!\[Python](https://img.shields.io/badge/Python-3.10-blue)](https://python.org)

\[!\[License](https://img.shields.io/badge/License-Educational-yellow)]()



\---



\## 🌐 Live Demo



| Service | URL | Status |

|---------|-----|--------|

| Spring Boot API | https://spring-llm-app.onrender.com/api/health | 🟢 Live |

| CI/CD Pipeline | \[GitHub Actions](https://github.com/Kuldeep3004/spring-llm-app/actions) | 🟢 Active |



\---



\## 💡 What is this project?



This project solves a real-world problem in software development:



> \*\*"How do we automatically detect security vulnerabilities in code before it goes to production?"\*\*



Most developers write code without knowing if it contains dangerous security flaws like SQL Injection or XSS attacks. This project uses \*\*Artificial Intelligence\*\* to automatically scan every piece of code that gets pushed to GitHub, detect vulnerabilities, and post the results as comments on Pull Requests — all without any manual effort.



\---



\## 🏗️ System Architecture



┌─────────────────────────────────────────────────────────────┐

│                     DEVELOPER'S LAPTOP                       │

│                                                             │

│   Writes Java Code  →  git push  →  GitHub Repository      │

└─────────────────────────────┬───────────────────────────────┘

│

▼

┌─────────────────────────────────────────────────────────────┐

│                   GITHUB ACTIONS (CI/CD)                     │

│                                                             │

│  Step 1: Build \& Test (Maven)                               │

│  Step 2: LLM Code Review → Post PR Comment                  │

│  Step 3: Deploy to Render (Primary)                         │

│  Step 4: Deploy to Railway (Fallback if Render fails)       │

│  Step 5: Deploy via Docker + VPS (Last resort)              │

└──────────────┬──────────────────────────────────────────────┘

│

▼

┌──────────────────────────┐      ┌──────────────────────────┐

│   SPRING BOOT APP        │      │  PYTHON FASTAPI SERVICE   │

│   (Java Backend)         │◄────►│  (AI Vulnerability        │

│                          │      │   Detection Engine)        │

│   Port: 8080             │      │   Port: 8000              │

│   Hosted on: Render      │      │   Model: DeepSeek 1.3B    │

└──────────────────────────┘      └──────────────────────────┘     



\---



\## ⚙️ Tech Stack — Explained



| Technology | Why we used it |

|------------|---------------|

| \*\*Java 17 + Spring Boot 3.2\*\* | Industry-standard backend framework for building REST APIs |

| \*\*Maven\*\* | Automates building, testing, and packaging Java applications |

| \*\*Docker\*\* | Packages the app into a container so it runs the same everywhere |

| \*\*GitHub Actions\*\* | Automates the entire build → test → deploy process on every code push |

| \*\*Render\*\* | Cloud platform to host the Spring Boot app for free |

| \*\*Railway\*\* | Backup cloud platform in case Render fails |

| \*\*Python + FastAPI\*\* | Lightweight, fast framework to serve our AI model as a REST API |

| \*\*PyTorch + HuggingFace\*\* | Industry-standard libraries for loading and running AI models |

| \*\*DeepSeek Coder 1.3B\*\* | A Large Language Model specifically trained on code |

| \*\*PEFT (LoRA)\*\* | Technique to fine-tune large models efficiently with less compute |

| \*\*Zimbra Fine-tuned Model\*\* | Custom trained model for detecting Java vulnerabilities |



\---



\## 🔍 Key Features



\### 1. 🤖 Automated CI/CD Pipeline

Every time code is pushed to GitHub, the pipeline automatically:

\- Compiles and tests the Java code using Maven

\- If it's a Pull Request, runs AI code review and posts comments

\- Deploys the application to the cloud

\- Falls back to alternative platforms if deployment fails



\### 2. 🛡️ AI-Powered Vulnerability Detection

The system uses a fine-tuned LLM to detect 5 types of security vulnerabilities with high accuracy:



| Vulnerability | What it means | Example |

|---------------|---------------|---------|

| \*\*SQL Injection\*\* | Attacker inserts malicious SQL code | `"SELECT \* FROM users WHERE id=" + userId` |

| \*\*Cross-Site Scripting (XSS)\*\* | Attacker injects malicious scripts into web pages | `out.println("<h1>" + userInput + "</h1>")` |

| \*\*Insecure Deserialization\*\* | Unsafe conversion of data that can execute malicious code | Reading untrusted serialized objects |

| \*\*Command Injection\*\* | Attacker executes system commands through the app | `Runtime.exec("ping " + userInput)` |

| \*\*Path Traversal\*\* | Attacker accesses files outside intended directory | `new File("/uploads/" + fileName)` |



\### 3. 🔄 Smart Deployment Strategy

The pipeline tries 3 different deployment methods in order:    

&#x20;     

Try Render → If fails, try Railway → If fails, use Docker + VPS       



This ensures the application is always deployed, no matter what.



\### 4. 💬 Automatic PR Code Review

When a developer opens a Pull Request, the AI automatically:

\- Analyzes the changed code

\- Identifies potential issues (bugs, security, performance)

\- Posts a detailed review comment on the PR



\---



\## 📡 API Endpoints



\### Spring Boot Service (Java)                  

GET  /api/health

Response: {"status": "UP", "service": "spring-llm-app"}

POST /api/review

Body: {"code": "your java code here"}

Response: {"review": "AI review comments", "status": "success"}



\### Python Vulnerability Scanner 



GET  /health

Response: {"status": "UP", "service": "vulnerability-detection"}

POST /scan

Body: {"code": "String query = 'SELECT \* FROM users WHERE id=' + userId;"}

Response:

{

"vulnerability": "SQL Injection",

"confidence": 94.53,

"is\_vulnerable": true

}                                



\---



\## 🚀 CI/CD Pipeline — Deep Dive



```yaml

Trigger: Push to main branch OR Pull Request opened



Jobs:

├── 1. Build \& Test

│       └── mvn clean package (compile + run tests)

│

├── 2. LLM Code Review (PR only)

│       ├── Get changed files diff

│       ├── Send to Ollama LLM

│       └── Post review as PR comment

│

├── 3. Deploy to Render

│       ├── Trigger Render deploy hook

│       └── Wait and verify health check

│

├── 4. Deploy to Railway (if Render fails)

│       ├── Install Railway CLI

│       └── Deploy and verify

│

├── 5. Manual Deploy via Docker (if Railway fails)

│       ├── Build Docker image

│       ├── Push to GitHub Container Registry

│       └── SSH into VPS and run container

│

└── 6. Notify Status

&#x20;       └── Post deploy summary table

```



\---



\## 🧠 How the AI Model Works



The vulnerability detection model is built using a technique called \*\*PEFT (Parameter Efficient Fine-Tuning)\*\* with \*\*LoRA (Low-Rank Adaptation)\*\*: 



Base Model: DeepSeek Coder 1.3B (pretrained on millions of code files)

↓

Fine-tuned with: checkpoint-800 (Java vulnerability patterns)

↓

Further fine-tuned with: checkpoint-297 (Zimbra-specific vulnerabilities)

↓

Result: A specialized model that understands Java security vulnerabilities



\*\*Why this approach?\*\*

\- Training a model from scratch requires months and millions of dollars

\- PEFT allows us to adapt an existing powerful model with minimal compute

\- The result is a small, fast, accurate vulnerability detector



\*\*Model Performance:\*\*

\- SQL Injection detection: \*\*94.53% confidence\*\*

\- Runs entirely on CPU (no GPU needed)



\---



\## 📁 Project Structure 



spring-llm-app/

├── .github/

│   └── workflows/

│       └── ci-cd.yml          ← Complete CI/CD pipeline

├── src/

│   └── main/java/com/myapp/

│       ├── App.java            ← Spring Boot entry point

│       └── OllamaController.java ← REST API endpoints

├── Dockerfile                 ← Container configuration

├── docker-compose.yml         ← Multi-service setup

├── render.yaml                ← Render deployment config

└── pom.xml                    ← Maven dependencies

vulnerability-service/         ← Python AI Service

├── main.py                    ← FastAPI server + model loading

└── requirements.txt           ← Python dependencies         

\---



\## 🛠️ How to Run Locally



\### Spring Boot App

```bash

\# Clone the repo

git clone https://github.com/Kuldeep3004/spring-llm-app.git

cd spring-llm-app



\# Build and run

mvn clean package -DskipTests

java -jar target/spring-llm-app-1.0-SNAPSHOT.jar



\# Test

curl http://localhost:8080/api/health

```



\### Python Vulnerability Scanner

```bash

cd vulnerability-service

python -m venv venv

venv\\Scripts\\activate       # Windows

pip install -r requirements.txt

uvicorn main:app --host 0.0.0.0 --port 8000



\# Test

curl -X POST http://localhost:8000/scan \\

&#x20; -H "Content-Type: application/json" \\

&#x20; -d '{"code": "SELECT \* FROM users WHERE id=" + userId"}'

```

\## 📊 Results \& Demo



\### Vulnerability Detection Example     

Input Code:

String query = "SELECT \* FROM users WHERE id=" + userId;

stmt.executeQuery(query);

AI Output:

{

"vulnerability": "SQL Injection",

"confidence": 94.53,

"is\_vulnerable": true

}        



\### CI/CD Pipeline Status   

✅ Build \& Test     → PASSED

✅ Deploy to Render → SUCCESS

✅ Notify Status    → DONE

🟡 LLM Review      → Runs on PR only 



\---



\## 🎯 What I Learned



Through this project, I learned and implemented:



\- \*\*DevSecOps\*\* — integrating security into the development pipeline

\- \*\*CI/CD\*\* — automating build, test, and deploy with GitHub Actions

\- \*\*Containerization\*\* — packaging apps with Docker

\- \*\*Cloud Deployment\*\* — deploying to Render and Railway

\- \*\*LLM Fine-tuning\*\* — using PEFT/LoRA to adapt large language models

\- \*\*REST API Design\*\* — building APIs with Spring Boot and FastAPI

\- \*\*Multi-service Architecture\*\* — connecting Java and Python services



\---



\## 👨‍💻 Developer



\*\*Kuldeep Lahare\*\*

\- 🎓 AI \& Data Science Student

\- 🏛️ Intern at \*\*NIT Rourkela\*\* (National Institute of Technology, Rourkela)

\- 💻 GitHub: \[@Kuldeep3004](https://github.com/Kuldeep3004)

\- 📧 kuldeep.lahare1124@gmail.com



\---



\## 🙏 Acknowledgements



\- 🏛️ This project was developed during my internship at \*\*NIT Rourkela\*\*

\- Project guided by my professor at NIT Rourkela

\- DeepSeek AI for the base coder model

\- HuggingFace for model hosting and libraries

\- Render \& Railway for free cloud hosting



\---



\*This project was built as part of an academic assignment to demonstrate real-world DevSecOps practices.\*                                            

