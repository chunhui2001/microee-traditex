package com.microee.traditex.inbox.rmi;

import org.springframework.cloud.netflix.feign.FeignClient;
import com.microee.traditex.inbox.interfaces.ITradiTexHBiTexOrderRMi;

@FeignClient(name = "microee-traditex-inbox-app", path = "/hbitex-order", configuration = TraditexClientConfiguration.class)
public interface TradiTexHBiTexOrderRMi extends ITradiTexHBiTexOrderRMi {

}
