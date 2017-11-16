package com.pmb.activiti.demo.job;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author: he.feng
 * @date: 17:01 2017/10/30
 * @desc:
 **/
public class JobExceptionDelegate implements JavaDelegate {


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("this is job exception delegate");
        throw new RuntimeException("JobExceptionDelegate message");
    }
}
