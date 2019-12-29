:%s/^[ ]\+/^I/
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES =	\
	risingCity.java \
	RBT.java
default:classes

classes:$(CLASSES:.java=.class)

clean:
	$(RM) *.class
