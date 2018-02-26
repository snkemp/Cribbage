default:
	javac -d . *.java

host:
	@make default
	java socket/CribbageHost

client:
	@make default
	java socket/CribbageClient

test:
	javac -d . *.java
	java socket/CribbageHost &
	java socket/CribbageClient
