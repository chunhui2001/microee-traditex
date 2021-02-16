
# 当前 Makefile 文件物理路径
ROOT_DIR:=$(shell dirname $(realpath $(firstword $(MAKEFILE_LIST))))

target:
	rm -rf $(ROOT_DIR)/target
	mkdir $(ROOT_DIR)/target  2>/dev/null

install-inbox:
	mvn -f $(ROOT_DIR)/microee-traditex-inbox/microee-traditex-inbox-oem/pom.xml clean install
	mvn -f $(ROOT_DIR)/microee-traditex-inbox/microee-traditex-inbox-interfaces/pom.xml clean install
	mvn -f $(ROOT_DIR)/microee-traditex-inbox/microee-traditex-inbox-up/pom.xml clean install
	mvn -f $(ROOT_DIR)/microee-traditex-inbox/microee-traditex-inbox-rmi/pom.xml clean install

install-liquid:
	mvn -f $(ROOT_DIR)/microee-traditex-liquid/microee-traditex-liquid-oem/pom.xml clean install
	mvn -f $(ROOT_DIR)/microee-traditex-liquid/microee-traditex-liquid-interfaces/pom.xml clean install
	mvn -f $(ROOT_DIR)/microee-traditex-liquid/microee-traditex-liquid-rmi/pom.xml clean install

install-web:
	mvn -f $(ROOT_DIR)/microee-traditex-web/microee-traditex-web-oem/pom.xml clean install

install-hello:
	mvn -f $(ROOT_DIR)/microee-traditex-hello/microee-traditex-hello-oem/pom.xml clean install
	
package-traditex-inbox-dist: target
	mvn -f $(ROOT_DIR)/microee-traditex-inbox/microee-traditex-inbox-app/pom.xml clean package
	cp $(ROOT_DIR)/microee-traditex-inbox/microee-traditex-inbox-app/target/microee-traditex-inbox-app-1.0-SNAPSHOT.jar $(ROOT_DIR)/target/

package-traditex-liquid-dist: target
	mvn -f $(ROOT_DIR)/microee-traditex-liquid/microee-traditex-liquid-app/pom.xml clean package
	cp $(ROOT_DIR)/microee-traditex-liquid/microee-traditex-liquid-app/target/microee-traditex-liquid-app-1.0-SNAPSHOT.jar $(ROOT_DIR)/target/

package-traditex-hello-dist: target
	mvn -f $(ROOT_DIR)/microee-traditex-hello/microee-traditex-hello-app/pom.xml clean package
	cp $(ROOT_DIR)/microee-traditex-hello/microee-traditex-hello-app/target/microee-traditex-hello-app-1.0-SNAPSHOT.jar $(ROOT_DIR)/target/

package-traditex-web-dist: target
	mvn -f $(ROOT_DIR)/microee-traditex-web/microee-traditex-web-app/pom.xml clean package
	cp $(ROOT_DIR)/microee-traditex-web/microee-traditex-web-app/target/microee-traditex-web-app-1.0-SNAPSHOT.jar $(ROOT_DIR)/target/
	
install-all: install-inbox install-liquid install-hello install-web
	echo 'install all dependencies'

build: package-traditex-inbox-dist package-traditex-liquid-dist package-traditex-hello-dist package-traditex-web-dist
	echo 'build successful'




