package com.aiddoru.dev;

import com.monitoring.easysimplemonitering.EasySimpleMonitoringModuleApplication;
import com.monitoring.easysimplemonitering.monitor.ServerStatusMonitor;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@AllArgsConstructor
@EnableScheduling // 추가
@Import(EasySimpleMonitoringModuleApplication.class)
public class DevApplication {
	public static void main(String[] args) {
		SpringApplication.run(DevApplication.class, args);
	}

}