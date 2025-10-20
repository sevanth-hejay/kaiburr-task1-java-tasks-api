package com.kaiburr.tasksapi.controller;

import com.kaiburr.tasksapi.model.Task;
import com.kaiburr.tasksapi.model.TaskExecution;
import com.kaiburr.tasksapi.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Get all tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAll();
    }

    // Get task by ID
    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable String id) {
        return taskService.getById(id);
    }

    // Create or update task
    @PostMapping
    public Task saveTask(@RequestBody Task task) {
        return taskService.save(task);
    }

    // Delete task
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id) {
        taskService.delete(id);
    }

    // Run a command for a task
    @PostMapping("/{id}/run")
    public TaskExecution runTaskCommand(@PathVariable String id, @RequestParam String command) throws Exception {
        return taskService.runCommand(id, command);
    }
}
