package com.microee.traditex.inbox.rmi;

import org.springframework.cloud.netflix.feign.FeignClient;
import com.microee.traditex.inbox.interfaces.ITradiTexConnectorRMi;

@FeignClient(name = "microee-traditex-inbox-app",
        path = "/traditex-ws-conns",
        configuration = TraditexClientConfiguration.class)
public interface TradiTexConnectorRMi extends ITradiTexConnectorRMi {

}
