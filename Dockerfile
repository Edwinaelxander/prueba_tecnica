# imagen oficial de Tomcat
FROM tomcat:9.0-jdk11-openjdk

# Autor de la imagen
LABEL autor=codifacil.club

#  archivo WAR al directorio de despliegue de Tomcat
COPY target/pruebaTecnica-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/pruebaTecnica.war

# Exponer el puerto 8080 para acceder a la aplicación
EXPOSE 8080

