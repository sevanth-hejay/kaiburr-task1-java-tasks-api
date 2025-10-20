

---

````markdown
# Kaiburr Task1 Java Tasks API

A simple **Spring Boot** REST API to manage tasks and run safe shell commands. Each task can have multiple executions with output and timestamps.

---

## **Features**

- Create, view, and delete tasks.
- Run predefined safe shell commands for a task.
- Track each task execution with start/end time and output.
- In-memory storage (no database needed for this version).

---

## **Task Model**

| Field            | Type   | Description                   |
|-----------------|--------|-------------------------------|
| `id`            | String | Unique task identifier         |
| `name`          | String | Task name                     |
| `owner`         | String | Task owner                    |
| `command`       | String | Default command for the task  |
| `taskExecutions`| List<TaskExecution> | List of executions |

**TaskExecution Model:**

| Field       | Type   | Description                       |
|------------|--------|-----------------------------------|
| `id`       | String | Execution ID                      |
| `startTime`| Date   | Execution start time               |
| `endTime`  | Date   | Execution end time                 |
| `output`   | String | Command output                     |

---

## **Prerequisites**

- Java 17  
- Maven 3.x  
- Git (optional for version control)  

---

## **Running the API**

1. Clone the repository:

```bash
git clone <your-repo-url>
cd kaiburr-task1-java-tasks-api
````

2. Run the API using Maven:

```bash
mvn spring-boot:run
```

3. The API will be available at:

```
http://localhost:8080
```

---

## **API Endpoints**

### **1. Create a Task**

* **URL:** `POST /tasks`
* **Headers:** `Content-Type: application/json`
* **Body:**

```json
{
    "name": "Test Task",
    "owner": "Sevanth",
    "command": "echo Hello World"
}
```

* **Response:**

```json
{
    "id": "generated-uuid",
    "name": "Test Task",
    "owner": "Sevanth",
    "command": "echo Hello World",
    "taskExecutions": []
}
```

---

### **2. Run a Command for a Task**

* **URL:** `POST /tasks/{id}/run?command=<command>`

* Replace `{id}` with the task ID from the create response.

* Allowed commands: `echo`, `ls`, `pwd`, `date`, `cat`, `uname`, `whoami`

* **Response:**

```json
{
    "id": "exec-1",
    "startTime": "2025-10-20T06:00:00.000+00:00",
    "endTime": "2025-10-20T06:00:01.000+00:00",
    "output": "Hello World\n"
}
```

---

### **3. Get All Tasks**

* **URL:** `GET /tasks`
* **Response:** List of tasks with all executions.

---

### **4. Get Single Task**

* **URL:** `GET /tasks/{id}`
* **Response:** Task with its executions.

---

### **5. Delete Task**

* **URL:** `DELETE /tasks/{id}`
* **Response:** `204 No Content`

---

 **Notes**

* **In-memory storage**: All tasks are lost when the server stops.
* Only safe shell commands are allowed to prevent security risks.
* Server runs on **port 8080** by default.

---

## **License**

This project is open-source and free to use.

```

---

If you want, I can also create a **Postman collection link** and **add example requests in README** so anyone can test the API with one click.  

Do you want me to do that?
```
