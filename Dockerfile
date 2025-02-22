FROM openjdk:21

WORKDIR /usrapp/bin

ENV PORT=6000
ENV CLASSES_DIR=/usrapp/bin/classes/edu/escuelaing/arep/controller

COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency
COPY /target/classes/static /usrapp/bin/target/classes/static
COPY /target/classes/newFolder /usrapp/bin/target/classes/newFolder

CMD ["java","-cp","./classes:./dependency/*","edu.escuelaing.arep.WebApplication"]