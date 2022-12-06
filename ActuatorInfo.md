# Actuator Info Preview

Node ``app`` is taken from ``info.*`` in ``application.properties`` file
Node  ``build`` is taken from ``springBoot.buildInfo.properties`` in ``build.gradle`` file

````json
{
    "app": {
        "env": "dev",
        "java": {
            "version": "17"
        },
        "name": "swordfish",
        "type": "Spring Boot"
    },
    "build": {
        "artifact": "swordfish",
        "customKey": "customValue",
        "group": "com.mk",
        "name": "Project Swordfish",
        "time": "2022-11-17T00:42:19.641Z",
        "version": "1.0.0"
    },
    "java": {
        "jvm": {
            "name": "OpenJDK 64-Bit Server VM",
            "vendor": "Amazon.com Inc.",
            "version": "17.0.5+8-LTS"
        },
        "runtime": {
            "name": "OpenJDK Runtime Environment",
            "version": "17.0.5+8-LTS"
        },
        "vendor": {
            "name": "Amazon.com Inc.",
            "version": "Corretto-17.0.5.8.1"
        },
        "version": "17.0.5"
    },
    "os": {
        "arch": "amd64",
        "name": "Windows 11",
        "version": "10.0"
    }
}
````