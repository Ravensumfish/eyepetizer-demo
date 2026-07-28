# Eyepetizer

基于开眼API，多模块开发，模块内部采取MVVM架构开发的仿开眼app

## 个人负责部分展示

| <img width="612" height="1354" alt="Image" src="https://github.com/user-attachments/assets/da34120e-3e4c-481a-b920-1ca5f435e149" /> | <img width="594" height="1336" alt="Image" src="https://github.com/user-attachments/assets/b0b5a5e9-c420-4293-8e49-369f0ef935f3" />| <img width="614" height="1350" alt="Image" src="https://github.com/user-attachments/assets/cd44cf8d-37f2-46f8-b7a6-f883005632d4" /> |
|---------------------------------------------------------------------------------------------------------------------------------------| ------------------------------------------------------------ | ------------------------------------------------------------ |
|<img width="618" height="1352" alt="Image" src="https://github.com/user-attachments/assets/ee865402-c0ab-4089-b212-3179865d6be6" />                                                   | <img width="610" height="1358" alt="Image" src="https://github.com/user-attachments/assets/aed91d2b-a25c-4c3e-a018-d98353414799" /> | <img width="614" height="1354" alt="Image" src="https://github.com/user-attachments/assets/abe758e2-f01f-4413-bd62-6c49daee7706" /> |
| <img width="614" height="1356" alt="Image" src="https://github.com/user-attachments/assets/fe773370-1641-4c5d-b2f1-67edde00396c" />                                                                                          | <img width="605" height="1330" alt="Image" src="https://github.com/user-attachments/assets/555b07d2-555f-4242-9ba9-63bb0d0fc711" />| <img width="604" height="1330" alt="Image" src="https://github.com/user-attachments/assets/17c8cc77-e54f-4b53-ba4d-50c7f192028a" />             |



## 功能概览

- 首页视频列表（下拉刷新 / 上拉加载更多）
- 日报视频列表
- 发现页面搜索，分类列表及详情
- 主题播单及详情
- 本地登录注册
- 收藏点赞，设置头像昵称等
- 搜索，历史保存，推荐搜索功能，视频排行榜

## 技术栈

| 类别       | 技术                                        |
| ---------- | ------------------------------------------- |
| 语言       | Kotlin 2.2.10                               |
| 网络       | okhttp 5.3.2+retrofit 2.11.0                |
| 异步       | RxJava 3.1.9                                |
| 图片       | glide 5.0.5                                 |
| 模块间通信 | The Router                                  |
| 模块内通信 | navigation 2.9.8                            |
| 调试       | LeakCanary 2.14（主要用于查找内存泄漏问题） |

## 项目结构

```
Eyepetizer/
├── app/                  # 壳工程，组装所有模块
├── build-logic/          # 约定插件
├── core/
│   ├── ui/               # 可复用的BaseRvAdapter
│   ├── net/              # 网络层封装
│   └── utils/            # 可复用的工具类
├── feature/
│   ├── home/      #整体activity
│   │   ├── home/      # 首页
│   │   ├── daily/       # 日报
│   │   ├── discovery/    # 发现
│   │   └── mine/        # 我的
│   ├── search/                 # 搜索模块
│   └── video/    #视频详情页模块
└── gradle/
    └── libs.versions.toml # 版本目录
```



### 模块间通信

使用The Router，在跳转请求发送方，如各Fraagment传参，如

```
adapter.onVideoClick={ Data->
            TheRouter
                .build("/feature/video/VideoActivity")
                .withInt("id",Data.id)
                .withString("title",Data.title)
                .withString("name",Data.author.name)
                .withString("icon",Data.author.icon)
                .withString("category",Data.category)
                .withString("description",Data.description)
                .withString("playUrl",Data.playUrl)
                .withInt("collectionCount",Data.consumption.collectionCount)
                .withInt("shareCount",Data.consumption.shareCount)
                .withInt("replyCount",Data.consumption.replyCount)
                .navigation()
        }
```

在接收方定义路径，接受参数等，如

```
@Route(path = "/feature/video/VideoActivity")

 @Autowired
     var id :Int = 0
    @Autowired
     var title : String = ""
    @Autowired
     var icon:String=""

```

### 模块内通信

使用navigation，在nav_graph.xml中注册，如

```
<fragment
        android:id="@+id/fragment_discovery"
        android:name="com.example.home.discovery.DiscoveryFragment"/>



    <fragment
        android:id="@+id/categoryDetailFragment"
        android:name="com.example.home.discovery.category.ui.CategoryDetailFragment"
        android:label="分类详情">
        <argument
            android:name="id"
            app:argType="integer" />
    </fragment>
```

在activity/fragment的点击事件中绑定控件id实现跳转以及传参，如

```
 setOnClickListener(
            binding.flHome,
            binding.flDaily,
            binding.flDiscovery,
            binding.flMine
        ) { clickedFl ->
            resetBtnState()

            when (clickedFl) {
                binding.flHome -> {
                    binding.ivHome.visibility = VISIBLE
                    binding.tvHome.visibility = GONE
                    onSelectListener?.onSelected(0)
                }
                binding.flDaily -> {
                    binding.ivDaily.visibility = VISIBLE
                    binding.tvDaily.visibility = GONE
                    onSelectListener?.onSelected(1)
                }
            }
            
            
             categoryAdapter.itemClick={_,item->
            val bundle= bundleOf("id" to item.id)

            findNavController().navigate(R.id.categoryDetailFragment,bundle)
        }
```

### 网络层

使用Okhttp+Retrofit+Rxjava组合，在core模块封装Retrofit单例，在模块内创建NetRepository以及接口api实例,得到返回类型为 Observable<T>以及Observable<MutableList<T>>的数据流，在ViewModel里传给Observer，使用NetRepository中定义的方法走订阅事件流，包括使用操作符，如map和filter处理筛选数据，将其转化为LiveData提供给Fragment，方法使用subscribeOn切换子线程执行网络请求，使用observeOn切换主线程更新UI。

调用链

```
  API
    -> NetRepository //集成所有RxJava 网络请求方法
    -> ViewModel //具体数据接收和筛选逻辑
    -> Activity/Fragment //更新ui
```

数据流

```
View (Activity/Fragment)
  ↕ observe LiveData
ViewModel
  ↓ 调用
NetRepository 
  ↓ Retrofit
   API
   ↓ 使用Okhttp发送网络请求
  服务端
   ↓ 返回JSON
  Gson
   ↓ 反序列化为数据类
RxJava 
   ↓ 切换主线程 
View 更新界面 
```

## 个人负责部分

- 首页视频列表，下拉刷新和上拉加载功能
- 日报视频列表，刷新和加载功能
- 发现页面构建
- 分类列表及其详情页，含封面和视频列表
- 主题播单及其详情页
- app启动页面设置以及问题排查处理
- 模块内通信部署

## 心得体会

这次开发任务让我更加熟悉了安卓开发中常用功能的实现方法，对嵌套复杂的数据类的处理，对于滑动嵌套时出现的问题的解决，对于以前掌握不够熟练的部分也在实操中加深了理解，提高了熟练度，此外，与前两次考核不同的是，双人合作开发让我更加熟悉了git的相关用法，关于多分支开发，如何正确push,pull与merge，处理冲突等等这次有了真正的实操经验。包括在多模块，多人开发中如何划分不同模块，如何分工能让冲突出现的可能性降到最低，我也从这次的经历中学习领悟到。同时，我深切感受到编译器以及各类技术更新迭代速度很快，在开发过程中可能会在适配方面出现各种问题，应当提高信息检索能力，以平和心态面对突发状况，勇于并善于学习新的知识，快速上手。最后，要感谢我的搭档，没有她就没有我们的项目的顺利推进，我也从她身上学到了很多。

## 不足之处

面对接口返回的复杂的数据，我采取的厘清使用思路的方法不够高效，在处理这方面时浪费时间有点过多了。此外由于接口内字段失效问题，分类详情页里顶部封面图以及主题播单的封面图和概述无法正常加载，也没有采用解决这种问题的补救措施。对于主题播单详情页里较为特殊的ui结构自己实现的思路不够顺畅。在开发刚开始时设置的版本目录在模块内导入依赖时没有真正用上。这些不足之处也为我指明了进一步学习的方向和目标。

