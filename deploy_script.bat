ssh soa "rm -rf ~/wildfly-21.0.0.Final/standalone1/deployments/first-service.*"
ssh soa "rm -rf ~/wildfly-21.0.0.Final/standalone2/deployments/second-service.*"

scp "first-service/target/first-service.war" soa:~/wildfly-21.0.0.Final/standalone1/deployments
scp "second-service/target/second-service.war" soa:~/wildfly-21.0.0.Final/standalone2/deployments

