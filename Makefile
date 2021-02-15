
target:
	mkdir target  2>/dev/null


install-inbox:
	mvn -f microee-traditex-inbox/microee-traditex-inbox-oem/pom.xml clean install
	mvn -f microee-traditex-inbox/microee-traditex-inbox-interfaces/pom.xml clean install
	mvn -f microee-traditex-inbox/microee-traditex-inbox-up/pom.xml clean install
	mvn -f microee-traditex-inbox/microee-traditex-inbox-rmi/pom.xml clean install

install-liquid:
	mvn -f microee-traditex-liquid/microee-traditex-liquid-oem/pom.xml clean install
	mvn -f microee-traditex-liquid/microee-traditex-liquid-interfaces/pom.xml clean install
	mvn -f microee-traditex-liquid/microee-traditex-liquid-rmi/pom.xml clean install

install-web:
	mvn -f microee-traditex-web/microee-traditex-web-oem/pom.xml clean install

install-hello:
	mvn -f microee-traditex-hello/microee-traditex-hello-oem/pom.xml clean install
	
package-traditex-inbox-dist: target
	mvn -f microee-traditex-inbox/microee-traditex-inbox-app/pom.xml clean package
	cp microee-traditex-inbox/microee-traditex-inbox-app/target/microee-traditex-inbox-app-1.0-SNAPSHOT.jar target/

package-traditex-liquid-dist: target
	mvn -f microee-traditex-liquid/microee-traditex-liquid-app/pom.xml clean package
	cp microee-traditex-liquid/microee-traditex-liquid-app/target/microee-traditex-liquid-app-1.0-SNAPSHOT.jar target/

package-traditex-hello-dist: target
	mvn -f microee-traditex-hello/microee-traditex-hello-app/pom.xml clean package
	cp microee-traditex-hello/microee-traditex-hello-app/target/microee-traditex-hello-app-1.0-SNAPSHOT.jar target/

package-traditex-web-dist: target
	mvn -f microee-traditex-web/microee-traditex-web-app/pom.xml clean package
	cp microee-traditex-web/microee-traditex-web-app/target/microee-traditex-web-app-1.0-SNAPSHOT.jar target/
	
install-all: install-inbox install-liquid install-hello install-web
	echo 'install all dependencies'

build: package-traditex-inbox-dist package-traditex-liquid-dist package-traditex-hello-dist package-traditex-web-dist
	echo 'build successful'




