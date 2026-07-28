# 项目名称

> 仿 Eyepetizer 开眼APP的安卓开源app

---

## 演示

| <img width="1216" height="2640" alt="Screenshot_20260728_072604" src="https://github.com/user-attachments/assets/7ca88e4b-972c-487f-9a49-6f071506956b" />|<img width="1216" height="2640" alt="Screenshot_20260728_072642" src="https://github.com/user-attachments/assets/3b49a687-8fd9-4bfb-980f-94922a6b7c91" />| <img width="1216" height="2640" alt="Screenshot_20260728_072709" src="https://github.com/user-attachments/assets/66dcc944-b872-4a88-a952-7817dd380573" />|
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| <img width="1216" height="2640" alt="Screenshot_20260728_072718" src="https://github.com/user-attachments/assets/94a22506-8b8f-4ef2-bc21-2734ee0c89c8" />| <img width="1216" height="2640" alt="Screenshot_20260728_072728" src="https://github.com/user-attachments/assets/a9fc686d-76fb-49e4-9a74-9ed0c7e95387" />| <img width="1216" height="2640" alt="Screenshot_20260728_072749" src="https://github.com/user-attachments/assets/b9082329-8d82-406b-baf9-41c825932a0f" />|
| <img width="1216" height="2640" alt="Screenshot_20260728_072816" src="https://github.com/user-attachments/assets/e8635588-2706-4727-b730-44b0dac8190b" />|<img width="1216" height="2640" alt="Screenshot_20260728_072855" src="https://github.com/user-attachments/assets/c07b45e7-bc77-4150-8bfe-837efb0d0148" />| <img width="1216" height="2640" alt="Screenshot_20260728_072925" src="https://github.com/user-attachments/assets/d51ea06a-0c25-45cc-8fc6-75d7528f3666" />|
|<img width="1216" height="2640" alt="Screenshot_20260728_073426" src="https://github.com/user-attachments/assets/86b010e7-d888-422b-8e2d-be8da28ea550" />| <img width="1216" height="2640" alt="Screenshot_20260728_083028" src="https://github.com/user-attachments/assets/6f623b84-4e32-4996-beb9-c069d1bea766" />| <img width="1216" height="2640" alt="Screenshot_20260728_083239" src="https://github.com/user-attachments/assets/84ddd48c-8370-4e71-8fbc-f4ff1da8268d" />|

<img width="2640" height="1216" alt="Screenshot_20260728_072936" src="https://github.com/user-attachments/assets/e52ea57c-5ab6-414a-851c-2e578a74ea19" />


##  项目介绍

​	这是作为一个Android初学者的练手项目，基于 AGP 9.2+ 环境开发，全kotlin语言，仿写开眼APP，具有加载视频，日报，搜索等等功能。

​	这是我和另一位开发者合作完成的项目，下面只介绍我参与的部分。

---

##  功能

- **工具类** - 内置一些常用组件和工具函数。
- **路由** - 使用TheRouter实现模块间通信。
- **搜索** - 关键词搜索，可以显示视频、作者、图文、话题、用户列表。可以保存搜索记录，可以显示搜索热词，可以预览周排行。
- **排行榜** - 查看周排行，月排行，总排行前十条视频。
- **视频播放** - 粗糙的视频播放功能，支持横屏，显示相关信息（标题、简介、作者等），可查看相关视频，支持收藏、点赞、分享。
- **登录与个人主页**：实现了基于SharedPreference的简单本地登录，还有个人头像、昵称，简介的修改与保存，可以查看个人收藏列表。

---

##  技术栈

| 类别     | 技术              | 职责                             |
| -------- | ----------------- | -------------------------------- |
| 语言     | Kotlin            | 版本 2.2.10                      |
| 网络请求 | Retrofit + OkHttp | HTTP接口调用与网络连接管理       |
| 数据解析 | Gson              | JSON与Java对象互转               |
| 本地存储 | SharedPreferences | 轻量级键值对数据持久化           |
| 图片加载 | Glide             | 网络图片加载、缓存与生命周期管理 |
| 视频播放 | GSYVideoPlayer    | 全功能视频播放器UI组件           |
| 页面路由 | TheRouter         | 组件化跨模块页面跳转             |
| 架构模式 | MVVM              | Model-View-ViewModel 数据驱动UI  |
| 线程异步 | RxJava            | 网络请求线程切换与流式响应       |

## 项目结构

```
Eyepetizer/
├── app/                    # 主壳App
├── core/                   # 核心基础层
│   ├── utils/              # 公共工具类、Base类
│   ├── net/            	# 网络层封装（Retrofit+OkHttp）
│   ├── data_store/         # 数据库/本地存储
│   └── ui/             	# 相关复用率高的ui组件
├── feature/                # 功能特性层（按业务拆分）
│   ├── home/               # 首页模块
│   ├── search/             # 搜索模块
│   └── video/              # 视频详情/播放模块
└── build-logic/            # Gradle插件/构建逻辑

```

### 架构设计

#### **模块化组件化**

​	将非app模块都声明为 library，由app统一管理，组装。使用TheRouter进行模块间跳转。

#### **网络层**

​	网络层采用 Retrofit + OkHttp + RxJava + Gson 组合，通过单例模式统一管理。

​	网络层调用链：

```
APIService //接口定义
    -> Repository //数据仓库
    -> ViewModel //业务逻辑
    -> Activity/Fragment //UI展示
```

​	底层网络使用 OkHttpClient 配置，GsonConverterFactory 将JSON自动映射为Kotlin数据类，RxJava3CallAdapterFactory 将网络接口返回值包装为Observable，支持RxJava流式操作。使用 Retrofit注解声明所有网络接口，返回类型统一为 `Observable<T>`作为可观察数据流，储存的T类型数据将会被observe接收。Repository作为数据中间层，负责调用API并处理原始响应，可以直接返回Observable给ViewModel，可以对其中的数据进行拆包处理（map、filter操作符）。

​	数据流：

```
[UI层] 
   ↓ 触发请求 (点击搜索/刷新)
[ViewModel] 
   ↓ 调用Repository方法
[Repository] 
   ↓ 执行Retrofit接口
[APIService] 
   ↓ 通过OkHttp发送请求
[服务端] 
   ↓ 返回JSON
[Gson] 
   ↓ 反序列化为数据类
[RxJava] 
   ↓ 切换主线程 (observeOn)
[UI层] 更新界面 (LiveData)
```

#### **UI层**

​	使用MVVM架构，viewModel处理所有网络请求传来的流，将其转化为LiveData供Activity/Fragment使用。在其中配置apiService与repository，以便发送网路请求，并创建disposable管理订阅，可以在vm销毁时一次性取消所有未完成的网络请求，防止内存泄漏。LIveData分为可变与只读类型，实现单一职责。调用方法，使用repository中定义的方法执行网络请求，subscribeOn切换子线程执行请求，observeOn切换主线程，在subscribe中更新可变LiveData，只读LiveData通知observe，实现UI更新。

#### **单Activity多Fragment**

​	对于单Activity多Fragment，使用fragmentManager统一管理，可执行replace，show，hide等方法实现多页面管理，包括回退栈的运用来协调各个页面。参数传递使用bundle，接口回调或者viewModel统一管理。



### 安装

​	见项目apk文件。

​	gradle版本 ：9.4.1

​	minSdk：24

## 收获

### 体会

​	这次仿开眼项目的开发让我受益匪浅，通过项目实践对许多Android相关概念与技术有了更深入的了解，并且合作进行，更熟悉了github远程仓库与分支的用法，在协调合作中减少合并冲突，增强协作能力。开始接触这套技术组合时，各种概念交织在一起让我有些应接不暇，但随着项目的推进，我逐渐有了体会。在写代码的过程中，我也慢慢养成了一些好习惯，比如注意订阅的生命周期，避免因为疏忽造成内存泄漏，思考哪些逻辑应该放在ViewModel里、哪些应该放在Repository里。这些细节单拎出来都很小，但积累起来，代码的健壮性和可维护性就有了质的区别，虽然现在还是有点硬编码，在日后的安卓开发过程中应用统一的地方管理常量和配置，一些可以使用多态的场景就使用多态。这次项目也让我看到了自己的不足和未来的方向。技术更新迭代很快，最重要的是保持学习的习惯和工程化的思维方式。

### 不足

​	代码仍有不少bug，功能也不算完整，api有些凌乱，整理过后只能实现目前的状态，尽力了。内存泄漏上仍有一些地方没能处理，只处理了adapter，binding，fragment和一些其他控件的释放。没有做夜间适配，界面美观度还有待提高，搜索词汇的列表是无序列表，原因是sp默认方法只能存set，要存有序列表需要存json字符串，取出时再转化为string列表。搜索词汇列表布局使用gridlayoutmanager，固定列数，而非自适应。

