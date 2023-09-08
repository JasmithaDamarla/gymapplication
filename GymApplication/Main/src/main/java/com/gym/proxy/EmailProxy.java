package com.gym.proxy;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gym.dto.EmailDTO;


@FeignClient(name="email-service/email")
@LoadBalancerClient(name = "email-service")
public interface EmailProxy {

	@PostMapping("/sendMail")
	public void sendEmail(@RequestBody EmailDTO emailDTO);
}
