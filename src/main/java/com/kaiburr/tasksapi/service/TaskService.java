package com.kaiburr.tasksapi.service;

import com.kaiburr.tasksapi.model.Task;
import com.kaiburr.tasksapi.model.TaskExecution;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TaskService {

    private final Map<String, Task> store = new LinkedHashMap<>();
    private final AtomicInteger execId = new AtomicInteger(1);  // now used

    public List<Task> getAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Task> getById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public Task save(Task t) {
        if (t.getId() == null || t.getId().isBlank()) {
            t.setId(UUID.randomUUID().toString());
        }
        if (t.getTaskExecutions() == null) {
            t.setTaskExecutions(new ArrayList<>());
        }
        store.put(t.getId(), t);
        return t;
    }

    public void delete(String id) {
        store.remove(id);
    }

    private boolean isAllowed(String command) {
        if (command == null) return false;
        String lower = command.trim().toLowerCase();

        String[] banned = {";", "&&", "|", ">", "<", "wget", "curl", "nc", "ncat", "rm ", "shutdown", "reboot", "mkfs", "dd ", "chmod 777", "forkbomb"};
        for (String b : banned) {
            if (lower.contains(b)) return false;
        }

        String[] allowStart = {"echo", "date", "uname", "ls", "pwd", "cat", "whoami"};
        for (String a : allowStart) {
            if (lower.startsWith(a)) return true;
        }
        return false;
    }

    public TaskExecution runCommand(String taskId, String command) throws Exception {
        Task task = store.get(taskId);
        if (task == null) throw new RuntimeException("Task not found: " + taskId);
        if (!isAllowed(command)) throw new RuntimeException("Command not allowed by policy");

        TaskExecution exec = new TaskExecution();
        exec.setId("exec-" + execId.getAndIncrement());  // assign unique ID
        exec.setStartTime(new Date());

        // Cross-platform command execution
        ProcessBuilder pb;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            pb = new ProcessBuilder("cmd.exe", "/c", command);
        } else {
            pb = new ProcessBuilder("/bin/sh", "-c", command);
        }

        pb.redirectErrorStream(true);
        Process p = pb.start();

        StringBuilder out = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.append(line).append("\n");
            }
        }

        p.waitFor();
        exec.setEndTime(new Date());
        exec.setOutput(out.toString());

        task.getTaskExecutions().add(exec);
        return exec;
    }
}
