# 开启组件化

## 组件清单文件

在项目根目录新建文件 `component.gradle`，`component.gradle` 中配置了所有组件的信息，这些配置信息在开发过程中是不会改变的，配置详细如下：

- name: 组件名字，可自定义
- remotePath: 远程依赖路径
- localPath: 本地依赖路径
- entryActivity: 入口 Activity
- service: 初始化服务路径

```java
ext.comps = [
        [
                name         : 'blog',
                remotePath   : 'com.zfy.blog:3.5.0',
                localPath    : 'component_blog',
                entryActivity: "com.zfy.blog.BlogActivity",
                service      : "/blog/service/init"
        ],
        [
                name         : 'main',
                remotePath   : 'com.zfy.main:3.5.0',
                localPath    : 'component_main',
                entryActivity: "com.zfy.main.MainActivity",
                service      : "/main/service/init"
        ],
        [
                name         : 'music',
                remotePath   : 'com.zfy.music:3.5.0',
                localPath    : 'component_music',
                entryActivity: "com.zfy.music.MusicActivity",
                service      : "/music/service/init"
        ]
]
```


## 组件配置

因为每个人在开发开发的组件不同，编译时依赖的组件也不同，因此这些会经常变动的配置我们将它抽离出来，放在 `gradle.propertis` 中，同事忽略该文件，每个人在本地维护该文件即可；

- entryComponent 入口组件，与上面配置组件清单的 name
- xxxxCompile 组件是否参与编译
- xxxxRemote 组件是否使用远程依赖

其中 `xxxx` 即为组件的 `name`


```
entryComponent=music

mainCompile=true
mainRemote=false

musicCompile=true
musicRemote=false

blogCompile=false
blogRemote=false
```

更改 `gradle.propertis` 会自动触发 `sync` 同步后即可进行开发；




