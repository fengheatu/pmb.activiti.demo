package com.pmb.activiti.demo.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author: he.feng
 * @date: 10:43 2017/11/1
 * @desc:
 **/
public class UserTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("使用任务监听器设置任务权限");
        delegateTask.setAssignee("user1");
        delegateTask.addCandidateGroup("group1");
        delegateTask.addCandidateUser("user1");
    }
}
