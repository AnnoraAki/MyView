# MyView
堆放各种自己写的自定义view...<br/>
附使用方法，免得自己也忘了...

## CircleRingView
>其实这个是红岩下半学期第一次作业...<br/>

>更新日志1.0<br/>
>更改了重复代码<br/>
>增加了对于onMeasure的复写，适配wrap_content<br/>
>对于其他特殊情况的修改
* 效果展示<br/>
![](https://github.com/Cchanges/MyView/blob/master/gif/Animation1.gif)
<br/>附带的设置参数<br/><br/>

* circleNum<br/>
设定圆环的个数，最多4个<br/>
代码中使用`setCircleNum(int num)`
* colors<br/>
设定圆环的颜色（从外到内）<br/>
使用arrays资源文件夹，使用`String-array`的形式设置颜色参数即可<br/>
代码中使用`setcolors(int arrayColorsRecourseId)`
* processes<br/>
设定圆环进程（从外到内），不用输入%<br/>
使用arrays资源文件夹，使用`String-array`的形式设置颜色参数即可<br/>
代码中使用`setcolors(int arrayProcessesRecourseId)`
* time<br/>
设置动画时间，单位为s
代码中使用`setTime(int time)`
