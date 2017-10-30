package com.zc.service;

import com.zc.bean.User;
import org.activiti.engine.task.Task;

import java.io.InputStream;
import java.util.List;

public interface ProcessService {

    void save(Process process);
    List<Process> listAll();

    void deploy(String processName);
    void startProcess(User user, String processKey);
    List<Task> listAssigneeTasks(User user);
    List<Task> listCandidateUserTasks(User user);
    List<Task> listCandidateGroupTasks(User user);
    <T> void executeTask(String taskId, String dateName, T t);

    InputStream getStaticProcDiagram(String processName);

    InputStream getDynamicProcDiagram(User user, String processName);

}
