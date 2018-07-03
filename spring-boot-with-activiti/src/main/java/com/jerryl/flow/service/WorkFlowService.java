package com.jerryl.flow.service;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;

import java.util.Map;

public interface WorkFlowService {

    /**
     * 根据任务ID终止任务（特权操作人员，直接完成整个流程）
     * @param taskId
     * @return
     */
    public abstract boolean endProcess(String taskId) throws Exception;

    /**
     * 根据任务ID和节点ID获取活动节点 <br>
     * @param taskId
     *            任务ID
     * @param activityId
     *            活动节点ID <br>
     *            如果为null或""，则默认查询当前活动节点 <br>
     *            如果为"end"，则查询结束节点 <br>
     *
     * @return
     * @throws Exception
     */
    public ActivityImpl findActivitiImpl(String taskId, String activityId) throws Exception;



    /**
     * 根据任务ID获取流程定义
     *
     * @param taskId
     *            任务ID
     * @return
     * @throws Exception
     */
    public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(
            String taskId) throws Exception;



    /**
     * 通过任务ID获取任务对象
     * @param taskId
     * @return
     */
    public Task findTaskById(String taskId);


    /**
     *
     * @param taskId
     *            当前任务ID
     * @param variables
     *            流程变量
     * @param activityId
     *            流程转向执行任务节点ID<br>
     *            此参数为空，默认为提交操作
     * @throws Exception
     */
    public void commitProcess(String taskId, Map<String, Object> variables,
                              String activityId) throws Exception;



    /**
     * 流程转向操作
     *
     * @param taskId
     *            当前任务ID
     * @param activityId
     *            目标节点任务ID
     * @param variables
     *            流程变量
     * @throws Exception
     */
    public void turnTransition(String taskId, String activityId,
                               Map<String, Object> variables) throws Exception;

}
