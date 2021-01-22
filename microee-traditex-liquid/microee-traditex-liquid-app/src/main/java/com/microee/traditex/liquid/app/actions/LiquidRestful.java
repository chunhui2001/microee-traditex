package com.microee.traditex.liquid.app.actions;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.microee.plugin.response.R;
import com.microee.stacks.es.supports.ElasticSearchIndexSupport;
import com.microee.stacks.es.supports.ElasticSearchSupport;

@RestController
@RequestMapping("/liquid")
public class LiquidRestful {

    @Autowired
    private ElasticSearchIndexSupport indexSupport;

    @Autowired
    private ElasticSearchSupport searchSupport;
    
    @RequestMapping(value = "/removeIndex", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Boolean> removeIndex(@RequestParam("indexName") String indexName) throws IOException {
        return R.ok(indexSupport.deleteIndex(indexName));
    }
    

    @RequestMapping(value = "/sla", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<List<Map<String, Object>>> sla(@RequestParam(value="size", required=false, defaultValue="10") Integer size) throws IOException {
        String type = "api-monitor-sla";
        return R.ok(searchSupport.search(type + "*", "id", String.format("{\n" + 
                "  \"size\": %s,\n" + 
                "  \"timeout\": \"1s\",\n" + 
                "  \"sort\": [\n" + 
                "        {\n" + 
                "            \"start\": {\n" + 
                "                \"order\": \"desc\"\n" + 
                "            }\n" + 
                "        }\n" + 
                " ]}", size)));
    }
}
