package com.zc.service.impl;

import com.zc.bean.User;
import com.zc.common.FileUtils;
import com.zc.dao.ProcessDAO;
import com.zc.service.ProcessService;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessDAO processDAO;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void save(Process process) {
        processDAO.save(process);
    }

    @Override
    public List<Process> listAll() {
        return processDAO.listAll();
    }

    @Override
    public void deploy(String processName) {
        try {
            repositoryService.createDeployment().addZipInputStream(
                    new ZipInputStream(
                            new FileInputStream(
                                    new File(FileUtils.getBpmnDir() + "/" + processName + ".zip")
                            )
                    )).deploy();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startProcess(User user, String processKey) {
        identityService.setAuthenticatedUserId(user.getName());
        runtimeService.startProcessInstanceByKey(processKey);
    }

    @Override
    public List<Task> listAssigneeTasks(User user) {
        return taskService.createTaskQuery().taskAssignee(user.getName()).list();
    }

    @Override
    public List<Task> listCandidateUserTasks(User user) {
        return taskService.createTaskQuery().taskCandidateUser(user.getName()).list();
    }

    @Override
    public List<Task> listCandidateGroupTasks(User user) {
        return taskService.createTaskQuery().taskCandidateGroup(user.getRoleName()).list();
    }

    @Override
    public <T> void executeTask(String taskId, String dateName, T t) {
        Map<String, Object> data = new HashMap<>();
        if (t != null) {
            data.put(dateName, t);
        }
        taskService.complete(taskId, data);
    }

    @Override
    public InputStream getStaticProcDiagram(String processName) {
        String deploymentId = repositoryService.createDeploymentQuery()
                .processDefinitionKey(processName).orderByDeploymenTime().desc().list().get(0).getId();
        List<String> resourceNames = repositoryService.getDeploymentResourceNames(deploymentId);
        for (String name : resourceNames) {
            if (name.endsWith(".png")) {
                return repositoryService.getResourceAsStream(deploymentId, name);
            }
        }
        return null;
    }

    /**
     * 由activiti读取xml文件动态地生成png图片
     * @param processName
     * @return
     */
    @Override
    public InputStream getDynamicProcDiagram(User user, String processName) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionName(processName).latestVersion().singleResult();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processName).startedBy(user.getName()).list().get(0);
        //得到流程执行对象, execution包含了事件及任务
        List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list();
        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<String>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId()); // 获取正在执行的活动节点
            activityIds.addAll(ids);
        }
        // DefaultProcessDiagramGenerator 流程图片的动态生成器
        return new DefaultProcessDiagramGenerator()
                .generateDiagram(repositoryService.getBpmnModel(processDefinition.getId()),
                        "jpg",
                        activityIds, // 哪些活动节点
                        Collections.emptyList(), // 哪些活动的顺序流
                        processEngineConfiguration.getActivityFontName(), // 所有节点的字体
                        processEngineConfiguration.getLabelFontName(), // 标签的字体
                        processEngineConfiguration.getAnnotationFontName(), // 注释的字体
                        processEngineConfiguration.getClassLoader(), // 类加载器
                        1.0 // 缩放比例
                        );
    }

}
