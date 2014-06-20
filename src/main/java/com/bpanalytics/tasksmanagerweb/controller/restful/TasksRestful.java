package com.bpanalytics.tasksmanagerweb.controller.restful;

import static com.bpanalytics.tasksmanagerweb.util.WebUtil.validateResult;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bpanalytics.tasksmanager.business.TasksBusiness;
import com.bpanalytics.tasksmanager.business.UserBusiness;
import com.bpanalytics.tasksmanager.framework.exception.BusinessException;
import com.bpanalytics.tasksmanager.persistence.model.Task;
import com.bpanalytics.tasksmanager.persistence.model.User;
import com.bpanalytics.tasksmanagerweb.authentication.TokenUtils;
import com.bpanalytics.tasksmanagerweb.framework.exception.ResourceNotFoundException;

@RestController
public class TasksRestful {
	
	@Autowired
	private TasksBusiness tasksBusiness;
	
	@Autowired
	private UserBusiness userBusiness;
	
	@Autowired
	@Qualifier("userBusiness")
	private UserDetailsService userService;
	
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	@RequestMapping(value="/user", method=RequestMethod.POST)
	@ResponseStatus( HttpStatus.CREATED )
    public User user(@RequestBody User user) {
		return userBusiness.newUser(user);
    }
	
	@RequestMapping(value="/sign-in", method=RequestMethod.POST)
	public String authenticate(@RequestParam String username, @RequestParam String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		Authentication authentication = this.authManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		UserDetails userDetails = this.userService.loadUserByUsername(username);

		return TokenUtils.createToken(userDetails);
	}
	
	@RequestMapping(value="/tasks/user/{id}", method=RequestMethod.GET)
    public List<Task> task(User user) {
		List<Task> tasks = tasksBusiness.getTasks(user);
		if (!validateResult(tasks)) {
			throw new ResourceNotFoundException();
		} 
		
		return tasks;
    }
	
	@RequestMapping(value="/tasks", method=RequestMethod.POST)
	@ResponseStatus( HttpStatus.CREATED )
    public Task newTask(@RequestBody Task task) {
        return tasksBusiness.newTask(task);
    }
	
	@RequestMapping(value="/tasks", method=RequestMethod.PUT)
    public void updateTask(@RequestBody Task task) {
		tasksBusiness.updateTask(task);
    }
	
	@RequestMapping(value="/tasks", method=RequestMethod.DELETE)
    public void deleteTask(@RequestBody Task task) {
		tasksBusiness.deleteTask(task);
    }
	
	@RequestMapping(value="/tasks", method=RequestMethod.GET)
    public List<Task> task() {
		List<Task> tasks = tasksBusiness.getUnfinishedTasks();
		if (!validateResult(tasks)) {
			throw new ResourceNotFoundException();
		} 
		
		return tasks;
    }
	
	@ExceptionHandler(BusinessException.class)
    public void handleBusinessException(HttpServletResponse response, Exception ex) throws IOException{
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }
}
