PROTO_DIR := src/main/argo-proto
PROTO_SENTINEL := $(PROTO_DIR)/.git
NUM_PROTO_FILES := $$(ls -1 $(PROTO_DIR) | wc -l)
BASHRC_FILE := ${HOME}/.bashrc

ACTUAL_JAVA_PATH := $$(sudo update-alternatives --get-selections | grep '^java\s' | sed 's/\s\+/ /g' | tr -s ' ' | cut -d ' ' -f 3)

JAVA_11_HOME := /usr/lib/jvm/jdk-11
JAVA_11_PATH := $(JAVA_11_HOME)/bin/java
JAVA_11_DOWNLOAD_URL := https://download.java.net/openjdk/jdk11/ri/openjdk-11+28_linux-x64_bin.tar.gz

JAVA_8_HOME := /usr/lib/jvm/java-8-openjdk-amd64
JAVA_8_PATH := $(JAVA_8_HOME)/jre/bin/java


# do not use COMMAs, other wise you get an "Unterminated quoted string" error
define make_header
	@echo "#######################################"
	@echo "# $(1)"
	@echo "#######################################"
endef

define make_warning
	@echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
	@echo "! $(1)"
	@echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
endef

.PHONY: all

all: init-submodules

$(JAVA_11_PATH):
	$(call make_header, "Java 11 does not exist installing it")
	wget $(JAVA_11_DOWNLOAD_URL) -O /tmp/openjdk-11+28_linux-x64_bin.tar.gz
	sudo tar xfvz /tmp/openjdk-11+28_linux-x64_bin.tar.gz --directory /usr/lib/jvm
	rm -f /tmp/openjdk-11+28_linux-x64_bin.tar.gz
install-java-11: $(JAVA_11_PATH)

setup-java-11: install-java-11
	@if [ $(ACTUAL_JAVA_PATH) = $(JAVA_11_PATH) ]; then \
		if [ ${JAVA_HOME} = $(JAVA_11_PATH) ]; then \
			echo "------ System already configured to java 11"; \
		else \
			echo "------ Java set, just add the following to $(BASHRC_FILE):\\n      export JAVA_HOME=$(JAVA_11_HOME)"; \
		fi \
	else \
		echo "------- Configuring system for java 11"; \
		sudo update-alternatives --install /usr/bin/java java $(JAVA_11_PATH) 100; \
		sudo update-alternatives --set java $(JAVA_11_PATH); \
		echo ""; \
		echo "------ Add the following to $(BASHRC_FILE):\\n      export JAVA_HOME=$(JAVA_11_HOME)"; \
	fi

$(JAVA_8_PATH):
	$(call make_header, "Java 8 does not exist -- installing it")
	sudo apt install -y openjdk-8-jdk
install-java-8: $(JAVA_8_PATH)

setup-java-8: install-java-8
	@if [ $(ACTUAL_JAVA_PATH) = $(JAVA_8_PATH) ]; then \
		if [ ${JAVA_HOME} = $(JAVA_8_PATH) ]; then \
			echo "------ System already configured to java 8"; \
		else \
			echo "------ Java set, just add the following to $(BASHRC_FILE):\\n      export JAVA_HOME=$(JAVA_8_HOME)"; \
		fi \
	else \
		echo "------- Configuring system for java 8"; \
		sudo update-alternatives --install /usr/bin/java java $(JAVA_8_PATH) 100; \
		sudo update-alternatives --set java $(JAVA_8_PATH); \
		echo ""; \
		echo "------ Add the following to $(BASHRC_FILE):\\n      export JAVA_HOME=$(JAVA_8_HOME)"; \
	fi

$(PROTO_SENTINEL):
	$(call make_header, "Initializing submodules...")
	@git submodule init
	@git submodule update --recursive
	@echo "   -- done"

init-submodules: $(PROTO_SENTINEL)

clean:
	JAVA_HOME=$(JAVA_11_HOME) mvn clean

build: init-submodules 
	JAVA_HOME=$(JAVA_11_HOME) mvn test-compile

build-protobuf:
	JAVA_HOME=$(JAVA_11_HOME) mvn protobuf:compile

