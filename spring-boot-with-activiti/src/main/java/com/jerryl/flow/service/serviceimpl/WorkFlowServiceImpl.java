package com.jerryl.flow.service.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.jerryl.flow.service.WorkFlowService;
import com.jerryl.util.CommonUtil;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

@Service("workFlowService")
public class WorkFlowServiceImpl implements WorkFlowService {

	/**
	 * 与流程定义和部署相关的service
	 */
	@Resource
	private RepositoryService repositoryService;

	/**
	 * 与正在执行的流程实例和执行对象相关的service
	 */
	@Resource
	private RuntimeService runtimeService;

	/**
	 * 与任务相关的service比如任务查询
	 */
	@Resource
	private TaskService taskService;

	/**
	 * 流程历史相关数据service
	 */
	@Resource
	private HistoryService historyService;

	/**
	 * 流程管理相关service
	 */
	@Resource
	private ManagementService managementService;

	/**
	 * 自定义表单相关service
	 */
	@Resource
	private FormService formService;
		
	/**
	 * 查询流程状态（判断流程正在执行，还是结束）
	 * @param processInstanceId 流程实例
	 */
	public boolean isProcessEnd(String processInstanceId){
		//创建流程实例查询
		ProcessInstance  processInstance = runtimeService.createProcessInstanceQuery()
						//流程实例id为查询条件
						.processInstanceId(processInstanceId)
						//返回流程查询的简单结果（一个实例id对应一个实例对象）
						.singleResult();
		
		if(processInstance==null){
			//流程已经结束
			return false;
		}
		else{
			//流程未结束
			return true;
		}
	}

	/**
	 * 根据任务id停止任务
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean endProcess(String taskId) throws Exception{
	
			ActivityImpl endActivity = findActivitiImpl(taskId, "end");
			commitProcess(taskId, null, endActivity.getId());
		return true;
	}

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
	@Override
	public ActivityImpl findActivitiImpl(String taskId, String activityId)
			throws Exception {
		
		// 取得流程定义  
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);  
  
        // 获取当前活动节点ID  
        if (CommonUtil.isObjNullOrEmp(activityId)) {
            activityId = findTaskById(taskId).getTaskDefinitionKey();  
        }  
  
        // 根据流程定义，获取该流程实例的结束节点  
        if (activityId.toUpperCase().equals("END")) {  
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {  
                List<PvmTransition> pvmTransitionList = activityImpl  
                        .getOutgoingTransitions();  
                if (pvmTransitionList.isEmpty()) {  
                    return activityImpl;  
                }  
            }  
        }  
  
        // 根据节点ID，获取对应的活动节点  
        ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition)  
                .findActivity(activityId);  
        return activityImpl;  
	}

	/**
	 * 根据任务ID获取流程定义
	 * @param taskId
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	@Override
	public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(
			String taskId) throws Exception {
		
		// 取得流程定义  
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)  
                .getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());  
  
        if (processDefinition == null) {  
            throw new Exception("流程定义未找到!");  
        }  
  
        return processDefinition; 
	}

	/**
	 * 通过任务id获取任务对象
	 * @param taskId
	 * @return
	 */
	@Override
	public Task findTaskById(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		return task;
	}

	/**
	 * 逐步提交任务过程（终止任务的中间方法）
	 * @param taskId
	 *            当前任务ID
	 * @param variables
	 *            流程变量
	 * @param activityId
	 *            流程转向执行任务节点ID<br>
	 *            此参数为空，默认为提交操作
	 * @throws Exception
	 */
	@Override
	public void commitProcess(String taskId, Map<String, Object> variables,
			String activityId) throws Exception {
		
		 if (variables == null) {  
	            variables = new HashMap<String, Object>();  
	        }  
	        // 跳转节点为空，默认提交操作  
	        if (CommonUtil.isObjNullOrEmp(activityId)) {
	            taskService.complete(taskId, variables);  
	        } else {// 流程转向操作  
	            turnTransition(taskId, activityId, variables);  
	        }  
		
	}

	/**
	 * 流程转向操作
	 * @param taskId
	 *            当前任务ID
	 * @param activityId
	 *            目标节点任务ID
	 * @param variables
	 *            流程变量
	 * @throws Exception
	 */
	@Override
	public void turnTransition(String taskId, String activityId,
			Map<String, Object> variables) throws Exception {
		
		 // 当前节点  
        ActivityImpl currActivity = findActivitiImpl(taskId, null);  
        // 清空当前流向  
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);  
  
        // 创建新流向  
        TransitionImpl newTransition = currActivity.createOutgoingTransition();  
        // 目标节点  
        ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);  
        // 设置新流向的目标节点  
        newTransition.setDestination(pointActivity);  
  
        // 执行转向任务  
        taskService.complete(taskId, variables);  
        // 删除目标节点新流入  
        pointActivity.getIncomingTransitions().remove(newTransition);  
  
        // 还原以前流向  
        restoreTransition(currActivity, oriPvmTransitionList); 
		
	}

	/** 
     * 清空指定活动节点流向 
     *  
     * @param activityImpl 
     *            活动节点 
     * @return 节点流向集合 
     */  
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {  
        // 存储当前节点所有流向临时变量  
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();  
        // 获取当前节点所有流向，存储到临时变量，然后清空  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        for (PvmTransition pvmTransition : pvmTransitionList) {  
            oriPvmTransitionList.add(pvmTransition);  
        }  
        pvmTransitionList.clear();  
  
        return oriPvmTransitionList;  
    } 
	
	
    /** 
     * 还原指定活动节点流向 
     *  
     * @param activityImpl 
     *            活动节点 
     * @param oriPvmTransitionList 
     *            原有节点流向集合 
     */  
    private void restoreTransition(ActivityImpl activityImpl,  
            List<PvmTransition> oriPvmTransitionList) {  
        // 清空现有流向  
        List<PvmTransition> pvmTransitionList = activityImpl  
                .getOutgoingTransitions();  
        pvmTransitionList.clear();  
        // 还原以前流向  
        for (PvmTransition pvmTransition : oriPvmTransitionList) {  
            pvmTransitionList.add(pvmTransition);  
        }  
    }  
	
}
