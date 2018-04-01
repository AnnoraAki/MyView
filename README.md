# MyView
堆放各种自己写的自定义view...<br/>
附使用方法，免得自己也忘了...

## CircleRingView
>其实这个是红岩下半学期第一次作业...<br/>

* 效果展示<br/>
![](https://github.com/Cchanges/MyView/blob/master/gif/Animation1.gif)
<br/>附带的设置参数<br/><br/>
* circleNum<br/>
设定圆环的个数，最多4个<br/>
代码中使用`setCircleNum(int num)`
* colors<br/>
设定圆环的颜色（从外到内）<br/>
使用`String`填写颜色字符串，并使用`#+颜色代码`，与`circleNum`数目相对应<br/>
代码中使用`setcolors(String colors)`
* processes<br/>
设定圆环进程（从外到内），不用输入%<br/>
使用`String`填写进度字符串，与xml设置同理，与`circleNum`数目相对应<br/>
代码中使用`setcolors(String colors)`
* time<br/>
设置动画时间，单位为s
代码中使用`setTime(int time)`
