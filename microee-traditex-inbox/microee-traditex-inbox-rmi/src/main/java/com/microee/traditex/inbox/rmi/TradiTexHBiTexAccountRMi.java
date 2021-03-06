package com.microee.traditex.inbox.rmi;

import org.springframework.cloud.netflix.feign.FeignClient;
import com.microee.traditex.inbox.interfaces.ITradiTexHBiTexAccountRMi;

@FeignClient(name = "microee-traditex-inbox-app", path = "/hbitex-account", configuration = TraditexClientConfiguration.class)
public interface TradiTexHBiTexAccountRMi extends ITradiTexHBiTexAccountRMi {

}
