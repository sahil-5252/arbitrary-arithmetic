# Makefile to compile AInteger and AFloat into a jar

PACKAGE_DIR = arbitraryarithmetic
SRC_FILES = $(PACKAGE_DIR)/AInteger.java $(PACKAGE_DIR)/AFloat.java
JAR_NAME = aarithmetic.jar
CLASS_FILES = $(PACKAGE_DIR)/AInteger.class $(PACKAGE_DIR)/AFloat.class

all: compile jar

compile:
	javac $(SRC_FILES)

jar: compile
	jar cf $(JAR_NAME) $(CLASS_FILES)

run:
	java -cp $(JAR_NAME) arbitraryarithmetic.AFloat

clean:
	rm -f $(PACKAGE_DIR)/*.class $(JAR_NAME)
