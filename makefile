default:
	@make -s init

init:
	javac -d . *.java

host:
	@make init
	@java socket/Host

client:
	@make init
	@java socket/Client

tests:
	@make -si init
	@java test/Test
