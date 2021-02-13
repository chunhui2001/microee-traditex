
### API kraken
https://www.kraken.com/features/api#get-ticker-info

### Kraken
https://trade.kraken.com/charts/KRAKEN:USDT-USD

### USDT/USD 实时价格
https://api.kraken.com/0/public/Ticker?pair=USDTUSD



### 交易微服务 -- traditex parent
$ mvn archetype:generate -DgroupId=com.microee.traditex -DartifactId=microee-traditex -DarchetypeArtifactId=pom-root -DinteractiveMode=false -DarchetypeGroupId=org.codehaus.mojo.archetypes -DarchetypeVersion=RELEASE -DarchetypeCatalog=local

### 交易微服务 -- traditex-inbox 长连接服务
mvn archetype:generate -DgroupId=com.microee.traditex.inbox -DartifactId=microee-traditex-inbox -DarchetypeArtifactId=pom-root -DinteractiveMode=false -DarchetypeGroupId=org.codehaus.mojo.archetypes -DarchetypeVersion=RELEASE -DarchetypeCatalog=local
### 交易微服务 -- traditex-inbox interfaces/服务对外接口
$ mvn archetype:generate -DgroupId=com.microee.traditex.inbox.interfaces -DartifactId=microee-traditex-inbox-interfaces -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local
### 交易微服务 -- traditex-inbox app 长连接服务, 封装第三方api
$ mvn archetype:generate -DgroupId=com.microee.traditex.inbox.app -DartifactId=microee-traditex-inbox-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local
### 交易微服务 -- traditex-inbox oem/领域模型 entity/数据库实体类, model/DababaseObject, vo/ViewObject, po/ParamObject, bo/BusinessObject
$ mvn archetype:generate -DgroupId=com.microee.traditex.inbox.oem -DartifactId=microee-traditex-inbox-oem -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local
### 交易微服务 -- traditex-inbox up/管理所有长链接
$ mvn archetype:generate -DgroupId=com.microee.traditex.inbox.up -DartifactId=microee-traditex-inbox-up -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local
### 交易微服务 -- traditex-inbox rmi/远程调用
$ mvn archetype:generate -DgroupId=com.microee.traditex.inbox.rmi -DartifactId=microee-traditex-inbox-rmi -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local
### 交易微服务 -- traditex-inbox mock/模拟第三方接口
$ mvn archetype:generate -DgroupId=com.microee.traditex.inbox.mock -DartifactId=microee-traditex-inbox-mock -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local

### 交易微服务 -- traditex-liquid 流动性摆盘多账户，多币种
mvn archetype:generate -DgroupId=com.microee.traditex.liquid -DartifactId=microee-traditex-liquid -DarchetypeArtifactId=pom-root -DinteractiveMode=false -DarchetypeGroupId=org.codehaus.mojo.archetypes -DarchetypeVersion=RELEASE -DarchetypeCatalog=local
### 交易微服务 -- traditex-liquid interfaces/服务对外接口
$ mvn archetype:generate -DgroupId=com.microee.traditex.liquid.interfaces -DartifactId=microee-traditex-liquid-interfaces -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local
### 交易微服务 -- traditex-liquid app
$ mvn archetype:generate -DgroupId=com.microee.traditex.liquid.app -DartifactId=microee-traditex-liquid-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local
### 交易微服务 -- traditex-liquid oem/领域模型, entity/数据库实体类, model/DababaseObject, vo/ViewObject, po/ParamObject, bo/BusinessObject
$ mvn archetype:generate -DgroupId=com.microee.traditex.liquid.oem -DartifactId=microee-traditex-liquid-oem -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local
### 交易微服务 -- traditex-liquid rmi/远程调用
$ mvn archetype:generate -DgroupId=com.microee.traditex.liquid.rmi -DartifactId=microee-traditex-liquid-rmi -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local

### 交易微服务 -- traditex-web 门户
mvn archetype:generate -DgroupId=com.microee.traditex.web -DartifactId=microee-traditex-web -DarchetypeArtifactId=pom-root -DinteractiveMode=false -DarchetypeGroupId=org.codehaus.mojo.archetypes -DarchetypeVersion=RELEASE -DarchetypeCatalog=local
### 交易微服务 -- traditex-web 门户应用
mvn archetype:generate -DgroupId=com.microee.traditex.web.app -DartifactId=microee-traditex-web-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local
### 交易微服务 -- traditex-web oem/领域模型, entity/数据库实体类, model/DababaseObject, vo/ViewObject, po/ParamObject, bo/BusinessObject
mvn archetype:generate -DgroupId=com.microee.traditex.web.oem -DartifactId=microee-traditex-web-oem -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=local
      
      
      
      
      
      
      
      
      
   