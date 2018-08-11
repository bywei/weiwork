package cn.bywei.weixin.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/healthcheck")
public class HealthCheckController {

	@RequestMapping("/ping")
	public String ping() {
		return "pong";
	}

}
