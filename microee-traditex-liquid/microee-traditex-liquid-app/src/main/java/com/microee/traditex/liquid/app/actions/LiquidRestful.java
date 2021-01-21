package com.microee.traditex.liquid.app.actions;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.microee.plugin.response.R;
import com.microee.stacks.es6.supports.ElasticSearchIndexSupport;

@RestController
@RequestMapping("/liquid")
public class LiquidRestful {

    @Autowired
    private ElasticSearchIndexSupport indexSupport;
    
    @RequestMapping(value = "/removeIndex", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public R<Boolean> removeIndex(@RequestParam("indexName") String indexName) throws IOException {
        return R.ok(indexSupport.deleteIndex(indexName));
    }
}
